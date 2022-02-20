package ru.hzerr;

public enum Mods {
    CHEATING_ESSENTIALS("CheatingEssentials_v5.1.0a.jar"),
    FORGE_WURST("ForgeWurst-0.11-MC1.12.2.jar");

    private String name;

    Mods(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
