package org.bookmc.installer.impl.mojang.utils;

import java.io.File;
import java.util.Locale;

public class MojangDirectoryUtils {
    public static File providePlatformDirectory() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (os.startsWith("windows")) {
            return new File(System.getenv("APPDATA"), ".minecraft");
        }

        if (os.startsWith("mac") || os.startsWith("darwin")) {
            return new File(System.getProperty("user.home"), "Library/Application Support/minecraft");
        }

        if (os.startsWith("linux")) {
            return new File(System.getProperty("user.home"), ".minecraft");
        }

        throw new UnsupportedOperationException("You are using an unsupported platform!");
    }
}
