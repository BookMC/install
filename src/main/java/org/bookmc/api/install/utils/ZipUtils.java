package org.bookmc.api.install.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {
    public static File downloadZip(URL url, File dest) {
        try {
            if (dest.getParentFile() == null) {
                throw new IllegalStateException("No parent file!");
            }
            File parentFile = dest.getParentFile();
            parentFile.mkdirs();

            File zipLocation = new File(dest.getAbsolutePath() + ".zip");
            zipLocation.createNewFile();

            DownloadUtils.download(url, zipLocation);

            ZipFile file = new ZipFile(zipLocation);
            Enumeration<? extends ZipEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                byte[] bytes = InputStreamUtils.readInputStream(file.getInputStream(entry));

                if (bytes.length > 0) {

                    File f = new File(dest + "/extracted", entry.getName());
                    if (f.getParentFile() != null) {
                        f.getParentFile().mkdirs();
                    }
                    f.createNewFile();

                    try (FileOutputStream fos = new FileOutputStream(f)) {
                        fos.write(bytes, 0, bytes.length);
                    }
                }
            }

            System.gc(); // Locks moment
            zipLocation.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(dest + "/extracted");
    }
}
