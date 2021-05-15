package org.bookmc.install.tests;

import org.bookmc.api.install.BookInstall;
import org.bookmc.install.DefaultBookInstall;
import org.bookmc.mojang.MojangInstallationPlatform;
import org.junit.jupiter.api.Test;

public class InstallationTests {
    @Test
    public void installMinecraftViaNormalLauncher() {
        BookInstall install = new DefaultBookInstall();

        assert install.installLocal(new MojangInstallationPlatform(), "1.8.9", "furnace");
    }
}
