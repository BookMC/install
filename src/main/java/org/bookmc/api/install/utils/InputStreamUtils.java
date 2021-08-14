package org.bookmc.api.install.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamUtils {
    public static byte[] readInputStream(InputStream stream) {
        try (InputStream inputStream = stream) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    baos.write(buffer, 0, read);
                }
                return baos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }
}
