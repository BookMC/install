package org.bookmc.install.tests;

import org.bookmc.api.install.BookInstall;
import org.bookmc.install.DefaultBookInstall;
import org.bookmc.mojang.MojangInstallationPlatform;
import org.bookmc.versionjson.VersionJson;
import org.bookmc.versionjson.library.Library;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

public class InstallationTests {
    @Test
    public void installMinecraftViaNormalLauncher() throws MalformedURLException, FileNotFoundException {
        BookInstall install = new DefaultBookInstall();

        String mcArgs = "--username ${auth_player_name} --version ${version_name} --gameDir ${game_directory} --assetsDir ${assets_root} --assetIndex ${assets_index_name} --uuid ${auth_uuid} --accessToken ${auth_access_token} --userProperties ${user_properties} --userType ${user_type} --tweakClass org.bookmc.loader.client.BookMCClientLoader";

        Library[] libraries = new Library[]{
            // Jitpack libraries
            new Library("com.github.BookMC.services:services:61152c01b9", "https://jitpack.io/"),
            new Library("com.github.BookMC.minecraft:client:61441a0631", "https://jitpack.io/"),
            new Library("com.github.BookMC:LegacyLauncher:2cbe27165a", "https://jitpack.io/"),
            new Library("com.github.BookMC:tweaker:42ed3ab", "https://jitpack.io/"),
            new Library("com.github.BookMC:loader:dd89085", "https://jitpack.io/"),

            // Maven central libraries
            new Library("org.ow2.asm:asm-commons:9.1", "https://repo1.maven.org/maven2/"),
            new Library("com.google.guava:guava:21.0", "https://repo1.maven.org/maven2/"),
            new Library("org.ow2.asm:asm-tree:9.1", "https://repo1.maven.org/maven2/"),
            new Library("org.ow2.asm:asm-util:9.1", "https://repo1.maven.org/maven2/"),
        };

        String version = "1.8.9";

        byte[] versionJson = VersionJson.createToBytes("net.minecraft.launchwrapper.Launch", mcArgs, "BookMC-" + version, version, libraries);

        assert install.install(new MojangInstallationPlatform(), versionJson, "furnace");
    }
}
