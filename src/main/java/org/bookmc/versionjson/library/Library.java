package org.bookmc.versionjson.library;

public class Library {
    private final String name;
    private final String url;

    public Library(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
