package org.bookmc.api.install.platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An interface to provide platform specific values to the API.
 *
 * @author ChachyDev
 * @since 0.0.1
 */
public interface InstallationPlatform {
    void init(byte[] versionJson);

    void appendLaunchProfile(String name, String lastVersionId, String type, String created, String lastUsed, String icon);

    File createVersionFolder(String name) throws FileNotFoundException;

    Library[] getRequiredLibraries(byte[] versionJson) throws MalformedURLException;

    File getMinecraftDirectory();

    String getId();

    default File getLibrariesFolder() {
        return new File(getMinecraftDirectory(), "libraries");
    }
}
