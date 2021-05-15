package org.bookmc.api.install;

import org.bookmc.api.install.platform.InstallationPlatform;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

public interface BookInstall {
    boolean install(InstallationPlatform platform, String version, byte[] versionJson, String icon) throws FileNotFoundException, MalformedURLException;

    default boolean install(InstallationPlatform platform, String version,  String versionJson, String icon) throws FileNotFoundException, MalformedURLException {
        return install(platform, version, versionJson.getBytes(StandardCharsets.UTF_8), icon);
    }

    /**
     * Grabs the data from a local version.json
     *
     * You MUST specify the version in the version.json as LOCAL or it will attempt to download your library from the internet!
     * @param platform The platform which the custom client is being installed to.
     */
    default boolean installLocal(InstallationPlatform platform, String version, String icon) {
        try (InputStream inputStream = getClass().getResourceAsStream("/assets/version/version.json")) {
            byte[] data = new byte[1024];

            int read;

            if (inputStream == null) {
                throw new IllegalStateException("Failed to find the built in version.json! (Make sure there is one located at /assets/version/version.json)");
            }

            try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                while ((read = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, read);
                }

                return install(platform, version, buffer.toByteArray(), icon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
