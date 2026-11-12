package dev.pvpstudio.util;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Guards against path traversal (zip-slip). Every write that originates from
 * user-supplied pack content must go through {@link #resolveInside}.
 */
public final class SafePaths {
    private SafePaths() {}

    /** Resolves {@code relative} under {@code root}; throws if the result escapes root. */
    public static Path resolveInside(Path root, String relative) throws IOException {
        if (relative == null || relative.isEmpty()) throw new IOException("Empty path");
        if (relative.indexOf('\0') >= 0 || relative.indexOf('\\') >= 0 || relative.startsWith("/")
                || (relative.length() > 1 && relative.charAt(1) == ':')) {
            throw new IOException("Illegal entry name: " + relative);
        }
        Path base = root.toAbsolutePath().normalize();
        Path resolved = base.resolve(relative).normalize();
        if (!resolved.startsWith(base) || resolved.equals(base)) {
            throw new IOException("Path escapes pack folder: " + relative);
        }
        return resolved;
    }

    /** Makes a user-provided name safe to use as a single file or folder name. */
    public static String sanitizeName(String raw) {
        String s = raw == null ? "" : raw.trim().replaceAll("[^A-Za-z0-9._ -]", "_");
        s = s.replaceAll("^\\.+", "_");
        if (s.length() > 64) s = s.substring(0, 64);
        return s.isEmpty() ? "unnamed" : s;
    }
}
