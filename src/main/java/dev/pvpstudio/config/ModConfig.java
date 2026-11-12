package dev.pvpstudio.config;

import java.util.LinkedHashMap;
import java.util.Map;

/** Root of config.json. Plain data holder; Gson reads and writes the public fields. */
public final class ModConfig {
    public int schemaVersion = ConfigMigrations.CURRENT_VERSION;

    public String activeProfile = "default";

    /** slotId -> packId. Missing entry means "vanilla default". Used from Stage 2. */
    public Map<String, String> textureSlots = new LinkedHashMap<>();

    public Ui ui = new Ui();

    public static final class Ui {
        public String theme = "midnight";
        public int accent = 0xFF4F8CFF;          // ARGB
        public int background = 0xE6101218;      // ARGB
        public float scale = 1.0f;
    }

    /** Repairs anything a hand-edited file could have broken. */
    public void sanitize() {
        if (activeProfile == null || activeProfile.isBlank()) activeProfile = "default";
        if (textureSlots == null) textureSlots = new LinkedHashMap<>();
        if (ui == null) ui = new Ui();
        if (ui.theme == null) ui.theme = "midnight";
        if (Float.isNaN(ui.scale)) ui.scale = 1.0f;
        ui.scale = Math.max(0.5f, Math.min(2.0f, ui.scale));
        schemaVersion = ConfigMigrations.CURRENT_VERSION;
    }
}
