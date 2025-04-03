package dev.nateweisz.fragment.utils;

import org.jetbrains.annotations.NotNull;

public class FileUtils {

    /**
     *
     * @param provided - the provided user path, with or without the extension
     * @param extension - the expected file extension
     * @return the file path with the extension
     */
    public static String fileNameWithExtension(@NotNull String provided, @NotNull String extension) {
        if (provided.endsWith("." + extension)) {
            return provided;
        }

        return provided + "." + extension;
    }
}
