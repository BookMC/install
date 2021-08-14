package org.bookmc.api.install.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MappingUtils {
    private static final String SEARGE_MAPPINGS = "http://export.mcpbot.bspk.rs/mcp/%s/mcp-%s-srg.zip";

    public static String downloadSearge(String version, File directory) throws IOException {
        File file = new File(directory, "searge-mappings.bookmc.srg");

        if (file.exists()) {
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        }

        file.getParentFile().mkdirs();

        File folder = ZipUtils.downloadZip(new URL(String.format(SEARGE_MAPPINGS, version, version)), directory);

        File[] files = folder.listFiles();

        if (files != null) {
            for (File f : files) {
                if (f.getName().equals("joined.srg")) {
                    String content = FileUtils.readFileToString(f, StandardCharsets.UTF_8);
                    FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
                    FileUtils.deleteDirectory(folder);
                    return content;
                }
            }
        }

        throw new IllegalStateException("Failed to download mappings!");
    }
}
