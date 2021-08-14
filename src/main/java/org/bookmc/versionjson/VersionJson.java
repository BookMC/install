package org.bookmc.versionjson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bookmc.versionjson.library.Library;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class VersionJson {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

    static {
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String create(String mainClass, String[] minecraftArguments, String id, String name, String version, Library[] libraries) {
        JsonObject object = new JsonObject();

        JsonArray array = new JsonArray();

        for (Library library : libraries) {
            JsonObject libraryObject = new JsonObject();

            libraryObject.addProperty("name", library.getName());
            libraryObject.addProperty("url", library.getUrl());

            array.add(libraryObject);
        }

        object.add("libraries", array);

        object.addProperty("mainClass", mainClass);
        object.addProperty("minecraftArguments", String.join(" ", minecraftArguments));
        object.addProperty("minimumLauncherVersion", 14);

        object.addProperty("id", id);
        object.addProperty("name", name);

        String timestamp = format.format(new Date());

        object.addProperty("releaseTime", timestamp);
        object.addProperty("time", timestamp);

        object.addProperty("type", "release");
        object.addProperty("inheritsFrom", version);
        object.addProperty("jar", version);

        return gson.toJson(object);
    }

    public static byte[] createToBytes(String mainClass, String[] minecraftArguments, String id, String name, String version, Library[] libraries) {
        return create(mainClass, minecraftArguments, id, name, version, libraries).getBytes(StandardCharsets.UTF_8);
    }
}
