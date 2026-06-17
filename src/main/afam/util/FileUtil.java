package afam.util;

import java.util.Locale;

public final class FileUtil {
    private FileUtil() {
    }

    public static String extension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot >= 0 && dot < filename.length() - 1
                ? filename.substring(dot + 1).toLowerCase(Locale.ROOT)
                : "";
    }

    public static String stripExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot > 0 ? filename.substring(0, dot) : filename;
    }

    public static String humanSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        double kb = bytes / 1024.0;
        if (kb < 1024) return String.format(Locale.ITALY, "%.1f KB", kb);
        double mb = kb / 1024.0;
        if (mb < 1024) return String.format(Locale.ITALY, "%.1f MB", mb);
        return String.format(Locale.ITALY, "%.1f GB", mb / 1024.0);
    }
}
