package org.bookmc.install;

import org.bookmc.api.install.BookInstall;
import org.bookmc.api.install.platform.InstallationPlatform;
import org.bookmc.api.install.utils.DownloadUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

public class DefaultBookInstall implements BookInstall {
    @Override
    public boolean install(InstallationPlatform platform, String version, byte[] versionJson, String icon) throws FileNotFoundException, MalformedURLException {
        platform.init(versionJson);
        // Create our new folder
        File librariesFolder = platform.getLibrariesFolder();
        File versionFolder = platform.createVersionFolder("BookMC-" + version);

        for (URL library : platform.getRequiredLibraries(versionJson)) {
            String path = library.getPath();
            String name = path.substring(path.lastIndexOf('/') + 1).replace(".jar", "");

            System.out.println("Downloading " + name);
            DownloadUtils.downloadJar(library, name, new File(librariesFolder, path.substring(0, path.lastIndexOf('/'))));
            System.out.println("Successfully downloaded " + name);
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
        platform.appendLaunchProfile("BookMC-1.8.9", "BookMC-1.8.9", "", now.toString(), now.toString(), icon);

        return true;
    }
}
