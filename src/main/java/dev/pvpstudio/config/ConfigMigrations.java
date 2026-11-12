package dev.pvpstudio.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/** Upgrades old config JSON to the current schema, one version step at a time. */
public final class ConfigMigrations {
    private ConfigMigrations() {}

    public static final int CURRENT_VERSION = 1;

    /** @return true if the object was changed. Throws if the file is from a newer mod version. */
    public static boolean migrate(JsonObject root) {
        int v = root.has("schemaVersion") && root.get("schemaVersion").isJsonPrimitive()
                ? root.get("schemaVersion").getAsInt() : 0;
        if (v > CURRENT_VERSION) {
            throw new JsonParseException("Config schema " + v + " is newer than supported " + CURRENT_VERSION);
        }
        boolean changed = false;
        while (v < CURRENT_VERSION) {
            switch (v) {
                case 0 -> { /* v0 -> v1: unversioned files simply gain schemaVersion */ }
                // case 1 -> { rename/move fields here when schema 2 exists }
                default -> throw new JsonParseException("No migration from schema " + v);
            }
            v++;
            changed = true;
        }
        root.addProperty("schemaVersion", CURRENT_VERSION);
        return changed;
    }
}
