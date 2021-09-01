package org.bookmc.installer.impl.mojang;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bookmc.installer.api.install.platform.InstallationPlatform;
import org.bookmc.installer.impl.mojang.utils.MojangDirectoryUtils;

import java.io.*;

public class MojangInstallationPlatform implements InstallationPlatform {
    private final File directory;

    private final Gson gson = new Gson();
    private JsonObject versionJson;

    public MojangInstallationPlatform(File directory) {
        this.directory = directory;
    }

    public MojangInstallationPlatform() {
        directory = MojangDirectoryUtils.providePlatformDirectory();
    }

    @Override
    public void init(byte[] versionJson) {
        try (InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(versionJson))) {
            this.versionJson = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File getMinecraftDirectory() {
        return directory;
    }

    @Override
    public File createVersionFolder(String name) throws FileNotFoundException {
        File folder = new File(directory, "versions/" + name);
        if (!folder.getParentFile().exists()) {
            throw new FileNotFoundException("Please install Minecraft to continue");
        }
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                throw new IllegalStateException("Failed to create a version folder");
            }
        }

        return folder;
    }

    @Override
    public void appendLaunchProfile(String name, String lastVersionId, String type, String created, String lastUsed, String icon) {
        File launcherProfiles = new File(directory, "launcher_profiles.json");

        try (FileReader reader = new FileReader(launcherProfiles)) {
            JsonObject launcherProfilesJson = JsonParser.parseReader(reader).getAsJsonObject();

            JsonObject profiles = launcherProfilesJson.getAsJsonObject("profiles");

            JsonObject newProfile = new JsonObject();

            newProfile.addProperty("lastVersionId", lastVersionId);
            newProfile.addProperty("lastUsed", lastUsed);
            newProfile.addProperty("created", created);
            newProfile.addProperty("name", name);
            newProfile.addProperty("type", type);
            newProfile.addProperty("icon", icon);

            if (profiles.has(name)) {
                profiles.remove(name);
            }

            profiles.add(name, newProfile);

            try (FileWriter writer = new FileWriter(launcherProfiles)) {
                writer.write(gson.toJson(launcherProfilesJson));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getId() {
        if (versionJson == null) {
            throw new IllegalStateException("MojangInstallationPlatform: You did not call the \"init\" method!");
        }
        return versionJson.get("id").getAsString();
    }
}
