package org.bookmc.installer.utils.versionjson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bookmc.installer.api.install.java.JavaVersion;
import org.bookmc.installer.utils.versionjson.library.Library;

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

    public static String create(String id, String inheritsFrom, String releaseType, String mainClass, JavaVersion javaVersion, Library[] libraries) {
        JsonObject object = new JsonObject();

        object.addProperty("id", id);
        object.add("javaVersion", javaVersion.toJsonObject());
        object.addProperty("inheritsFrom", inheritsFrom);

        String timestamp = format.format(new Date());

        object.addProperty("releaseTime", timestamp);
        object.addProperty("time", timestamp);

        object.addProperty("type", releaseType);

        object.addProperty("mainClass", mainClass);


        JsonObject argumentsObject = new JsonObject();
        argumentsObject.add("game", new JsonArray());

        object.add("arguments", argumentsObject);

        JsonArray librariesArray = new JsonArray();

        for (Library library : libraries) {
            librariesArray.add(library.toJsonObject());
        }

        object.add("libraries", librariesArray);

        return gson.toJson(object);
    }

    public static byte[] createToBytes(String id, String inheritsFrom, String releaseType, String mainClass, JavaVersion version, Library[] libraries) {
        return create(id, inheritsFrom, releaseType, mainClass, version, libraries).getBytes(StandardCharsets.UTF_8);
    }
}
