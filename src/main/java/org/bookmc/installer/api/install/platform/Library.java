package org.bookmc.installer.api.install.platform;

import java.net.URL;

public class Library {
    private final String path;
    private final String name;
    private final URL url;

    public Library(URL url, String name, boolean local, String path) {
        this.url = url;
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }
}
