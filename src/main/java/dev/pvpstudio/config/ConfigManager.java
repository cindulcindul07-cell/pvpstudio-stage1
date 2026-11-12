package dev.pvpstudio.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import dev.pvpstudio.client.PvpStudioClient;
import dev.pvpstudio.util.PvpPaths;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Loads and saves config.json. Never throws to callers: failures fall back to defaults. */
public final class ConfigManager {
    private ConfigManager() {}

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ModConfig config = new ModConfig();

    public static ModConfig get() { return config; }

    public static void load() {
        Path file = PvpPaths.config();
        try {
            PvpPaths.ensureDirs();
            if (!Files.exists(file)) {
                config = new ModConfig();
                save();
                return;
            }
            String text = Files.readString(file, StandardCharsets.UTF_8);
            JsonObject obj = JsonParser.parseString(text).getAsJsonObject();
            boolean migrated = ConfigMigrations.migrate(obj);
            ModConfig loaded = GSON.fromJson(obj, ModConfig.class);
            if (loaded == null) throw new JsonParseException("Config was empty");
            loaded.sanitize();
            if (migrated) backup(file, "pre-migration");
            config = loaded;
            if (migrated) save();
        } catch (IOException | RuntimeException e) {
            PvpStudioClient.LOGGER.error("Could not read config, using defaults", e);
            backup(file, "corrupt");
            config = new ModConfig();
            save();
        }
    }

    public static void save() {
        Path file = PvpPaths.config();
        try {
            Files.createDirectories(file.getParent());
            Path tmp = file.resolveSibling("config.json.tmp");
            Files.writeString(tmp, GSON.toJson(config), StandardCharsets.UTF_8);
            try {
                Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            PvpStudioClient.LOGGER.error("Could not save config", e);
        }
    }

    private static void backup(Path file, String reason) {
        try {
            if (!Files.exists(file)) return;
            Files.createDirectories(PvpPaths.backups());
            Path dest = PvpPaths.backups().resolve("config-" + reason + "-" + System.currentTimeMillis() + ".json");
            Files.copy(file, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            PvpStudioClient.LOGGER.warn("Could not back up config", e);
        }
    }
}
