package org.bookmc.installer.impl.install;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bookmc.installer.api.install.BookInstall;
import org.bookmc.installer.api.install.platform.InstallationPlatform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
