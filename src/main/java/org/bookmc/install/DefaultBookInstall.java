package org.bookmc.install;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bookmc.api.install.BookInstall;
import org.bookmc.api.install.platform.InstallationPlatform;
import org.bookmc.api.install.platform.Library;
import org.bookmc.api.install.utils.DownloadUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

public class DefaultBookInstall implements BookInstall {
    private final Logger logger = LogManager.getLogger(this);

    @Override
    public boolean install(InstallationPlatform platform, byte[] versionJson, String icon) throws FileNotFoundException, MalformedURLException {
        platform.init(versionJson);
        // Create our new folder
        File librariesFolder = platform.getLibrariesFolder();
        File versionFolder = platform.createVersionFolder(platform.getId());

        for (Library library : platform.getRequiredLibraries(versionJson)) {
            URL url = library.getUrl();

            if (url == null) {
                throw new IllegalStateException("The local file for " + library.getName() + " could not be found!");
            }

            String path = url.getPath().replace("/maven2", "");
            String name = path.substring(path.lastIndexOf('/') + 1).replace(".jar", "");

            logger.info("Attempting to " + (library.isLocal() ? "copy" : "download") + " " + url);

            File file =  library.isLocal() ? new File(librariesFolder, library.getPath()) : new File(librariesFolder, path.substring(0, path.lastIndexOf('/')));

            DownloadUtils.downloadJar(url, name, file);
        }

        File versionJsonFile = new File(versionFolder, platform.getId() + ".json");

        try (FileOutputStream buffer = new FileOutputStream(versionJsonFile)) {
            buffer.write(versionJson);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // We've done our part! Let the platform do it's stuff
        Instant now = Instant.ofEpochMilli(System.currentTimeMillis());
        platform.appendLaunchProfile(platform.getId(), platform.getId(), "custom", now.toString(), now.toString(), icon);

        return true;
    }
}
