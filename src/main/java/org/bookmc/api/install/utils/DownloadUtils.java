package org.bookmc.api.install.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class DownloadUtils {
    public static void download(URL url, String name, String extension, File directory) {
        File file = new File(directory, name + "." + extension);
        if (!file.exists()) {
            file.getParentFile().mkdirs();

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream buffer = new FileOutputStream(file)) {
            byte[] data = new byte[1024];
            int read;
            while ((read = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
        } catch (IOException ignored) {
        }

    }

    public static void downloadJar(URL url, String name, File directory) {
        download(url, name, "jar", directory);
    }
}
