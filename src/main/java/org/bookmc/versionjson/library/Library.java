package org.bookmc.versionjson.library;

import com.google.gson.JsonObject;

public record Library(String name, String url) {
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public JsonObject toJsonObject() {
        JsonObject object = new JsonObject();
        object.addProperty("name", name);
        object.addProperty("url", url);
        return object;
    }
}
