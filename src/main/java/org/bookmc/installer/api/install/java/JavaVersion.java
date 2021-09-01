package org.bookmc.installer.api.install.java;

import com.google.gson.JsonObject;

public enum JavaVersion {
    JAVA_8("jre-legacy", 8),
    JAVA_16("java-runtime-alpha", 16);

    private final String component;
    private final int majorVersion;

    JavaVersion(String component, int majorVersion) {
        this.component = component;
        this.majorVersion = majorVersion;
    }

    public JsonObject toJsonObject() {
        JsonObject object = new JsonObject();
        object.addProperty("component", component);
        object.addProperty("majorVersion", majorVersion);
        return object;
    }
}
