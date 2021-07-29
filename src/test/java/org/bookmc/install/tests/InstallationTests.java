package org.bookmc.install.tests;

import org.bookmc.api.install.BookInstall;
import org.bookmc.install.DefaultBookInstall;
import org.bookmc.mojang.MojangInstallationPlatform;
import org.bookmc.versionjson.VersionJson;
import org.bookmc.versionjson.library.Library;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class InstallationTests {
    @Test
    public void installMinecraftViaNormalLauncher() throws MalformedURLException, FileNotFoundException {
        if (System.getProperty("user.name").equals("runner")) {
            assert true;
            return;
        }

        BookInstall install = new DefaultBookInstall();

        String[] mcArgs = new String[]{"--username", "${auth_player_name}", "--version", "${version_name}", "--gameDir", "${game_directory}", "--assetsDir", "${assets_root}", "--assetIndex", "${assets_index_name}", "--uuid", "${auth_uuid}", "--accessToken", "${auth_access_token}", "--userProperties", "${user_properties}", "--userType", "${user_type}", "--tweakClass", "org.bookmc.loader.impl.tweaker.BookMCClientLoader"};

        Library[] libraries = new Library[]{
            // Jitpack libraries
            new Library("com.github.BookMC.services:services:61152c01b9", "https://jitpack.io/"),
            new Library("com.github.BookMC.minecraft:client:48baca2f8a", "https://jitpack.io/"),
            new Library("com.github.BookMC:LegacyLauncher:2cbe27165a", "https://jitpack.io/"),
            new Library("com.github.BookMC:book-loader:0.4.1", "https://jitpack.io/"),
        };

        String version = "1.8.9";

        byte[] versionJson = VersionJson.createToBytes("net.minecraft.launchwrapper.Launch", mcArgs, "BookMC-" + version, version, libraries);

        assert install.install(new MojangInstallationPlatform(), versionJson, "furnace");
    }
}
