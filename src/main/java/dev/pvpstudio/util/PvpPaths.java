package dev.pvpstudio.util;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** All file locations used by the mod. Everything lives under config/pvpstudio/. */
public final class PvpPaths {
    private PvpPaths() {}

    public static Path root()     { return FabricLoader.getInstance().getConfigDir().resolve("pvpstudio"); }
    public static Path config()   { return root().resolve("config.json"); }
    public static Path packs()    { return root().resolve("packs"); }
    public static Path profiles() { return root().resolve("profiles"); }
    public static Path backups()  { return root().resolve("backups"); }

    public static void ensureDirs() throws IOException {
        Files.createDirectories(packs());
        Files.createDirectories(profiles());
        Files.createDirectories(backups());
    }
}
