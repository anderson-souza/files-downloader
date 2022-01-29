package com.aps.model;

public enum Environment {
    DEV("DESENVOLVIMENTO",
        new String[]{"https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.3.0-alpha0/"}),
    HOM("HOMOLOGAÇÃO",
        new String[]{"https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.2.9/"}),
    PRD("PRODUÇÃO",
        new String[]{"https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.2.5/",
            "https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.2.6/"});

    Environment(final String name, final String[] urls) {
        this.name = name;
        this.urls = urls;
    }

    private String name;
    private String[] urls;

    public String getName() {
        return name;
    }

    public String[] getUrls() {
        return urls;
    }

    @Override
    public String toString() {
        return getName();
    }
}
