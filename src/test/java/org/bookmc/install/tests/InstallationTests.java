package org.bookmc.install.tests;

import org.apache.commons.io.IOUtils;
import org.bookmc.api.install.BookInstall;
import org.bookmc.api.install.utils.MappingUtils;
import org.bookmc.install.DefaultBookInstall;
import org.bookmc.mojang.MojangInstallationPlatform;
import org.bookmc.mojang.utils.MojangDirectoryUtils;
import org.bookmc.versionjson.VersionJson;
import org.bookmc.versionjson.library.Library;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class InstallationTests {
    @Test
    public void installMinecraftViaNormalLauncher() throws IOException {
        if (System.getProperty("user.name").equals("runner")) {
            return;
        }

        BookInstall install = new DefaultBookInstall();

        String gameVersion = "1.18_experimental-snapshot-5";

        String minecraftHookVersion = "0.6.0+build.17";
        String loaderVersion = "0.10.0+build.19";

        Library[] libraries = new Library[]{
            // Jitpack libraries
            new Library(String.format("org.bookmc:minecraft:%s:mc%s", minecraftHookVersion, gameVersion), "https://maven.bookmc.org/"),
            new Library("org.bookmc:book-loader:" + loaderVersion, "https://maven.bookmc.org/"),
        };

        byte[] versionJson = VersionJson.createToBytes("book-minecraft-" + minecraftHookVersion + "-" + gameVersion + "-loader." + loaderVersion, gameVersion, "pending", "org.bookmc.loader.impl.launch.QuiltClient", libraries);

        String icon = "data:image/png;base64," + Base64.getEncoder().encodeToString(IOUtils.resourceToByteArray("/assets/bookmc/logo.png"));

        install.install(new MojangInstallationPlatform(), versionJson, icon);
    }
}
