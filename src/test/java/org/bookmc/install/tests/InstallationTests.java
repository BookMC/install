package org.bookmc.install.tests;

import org.bookmc.api.install.BookInstall;
import org.bookmc.api.install.utils.MappingUtils;
import org.bookmc.install.DefaultBookInstall;
import org.bookmc.mojang.MojangInstallationPlatform;
import org.bookmc.mojang.utils.MojangDirectoryUtils;
import org.bookmc.versionjson.VersionJson;
import org.bookmc.versionjson.library.Library;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class InstallationTests {
    @Test
    public void installMinecraftViaNormalLauncher() throws IOException {
        if (System.getProperty("user.name").equals("runner")) {
            assert true;
            return;
        }

        BookInstall install = new DefaultBookInstall();

        String[] mcArgs = new String[]{"--username", "${auth_player_name}", "--version", "${version_name}", "--gameDir", "${game_directory}", "--assetsDir", "${assets_root}", "--assetIndex", "${assets_index_name}", "--uuid", "${auth_uuid}", "--accessToken", "${auth_access_token}", "--userProperties", "${user_properties}", "--userType", "${user_type}"};

        Library[] libraries = new Library[]{
            // Jitpack libraries
            new Library("org.bookmc:client:0.2.8", "https://maven.bookmc.org/"),
            new Library("org.bookmc:book-loader:0.8.11", "https://maven.bookmc.org/"),
        };

        String version = "1.8.9";

        byte[] versionJson = VersionJson.createToBytes("org.bookmc.loader.impl.launch.QuiltClient", mcArgs, "BookMC-" + version, "BookMC", version, libraries);

        String icon = "data:image/png;base64," + Base64.getEncoder().encodeToString(getResourceAsBytes("assets/bookmc/logo.png"));

        assert install.install(new MojangInstallationPlatform(), versionJson, icon);

        File directory = new File(MojangDirectoryUtils.providePlatformDirectory(), ".book");
        directory.mkdir();
        MappingUtils.downloadSearge(version, directory);
    }

    private byte[] getResourceAsBytes(String name) {
        try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream(name)) {
            if (stream != null) {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = stream.read(buffer, 0, buffer.length)) != -1) {
                        baos.write(buffer, 0, read);
                    }
                    return baos.toByteArray();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("Could not find " + name);
    }
}
