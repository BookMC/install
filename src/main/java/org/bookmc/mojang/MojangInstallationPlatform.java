package org.bookmc.mojang;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bookmc.api.install.platform.InstallationPlatform;
import org.bookmc.api.install.platform.Library;
import org.bookmc.mojang.utils.MojangDirectoryUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MojangInstallationPlatform implements InstallationPlatform {
    private final File directory;

    private final Gson gson = new Gson();

    public MojangInstallationPlatform(File directory) {
        this.directory = directory;
    }

    public MojangInstallationPlatform() {
        directory = MojangDirectoryUtils.providePlatformDirectory();
    }

    private JsonObject versionJson;

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
    public Library[] getRequiredLibraries(byte[] versionJson) throws MalformedURLException {
        List<Library> libs = new ArrayList<>();

        JsonObject obj = JsonParser.parseString(new String(versionJson)).getAsJsonObject();
        JsonArray libraries = obj.get("libraries").getAsJsonArray();

        for (int i = 0; i < libraries.size(); i++) {
            JsonObject library = libraries.get(i).getAsJsonObject();
            if (library.has("name") && library.has("url")) {
                String libName = library.get("name").getAsString();
                String[] split = libName.split(":");

                String group = split[0].replace(".", "/");
                String name = split[1];
                String version = split[2];

                String base = library.get("url").getAsString();

                if (base.endsWith("/")) {
                    base = base.substring(0, base.length() - 1);
                }

                String url = String.format("/%s/%s/%s/%s-%s.jar", group, name, version, name, version);

                boolean local = version.equals("LOCAL");

                libs.add(new Library(local ? this.getClass().getResource(url) : new URL(base + url), name, local, url));
            }
        }

        return libs.toArray(new Library[0]);
    }

    @Override
    public String getId() {
        if (versionJson == null) {
            throw new IllegalStateException("MojangInstallationPlatform: You did not call the \"init\" method!");
        }
        return versionJson.get("id").getAsString();
    }
}
