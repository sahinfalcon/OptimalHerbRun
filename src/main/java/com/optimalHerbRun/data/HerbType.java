package com.optimalHerbRun.data;

import lombok.Getter;

@Getter
public enum HerbType {
    GUAM("Guam"),
    MARRENTILL("Marrentill"),
    TARROMIN("Tarromin"),
    HARRALANDER("Harralander"),
    RANARR("Ranarr"),
    TOADFLAX("Toadflax"),
    IRIT("Irit"),
    AVANTOE("Avantoe"),
    KWUARM("Kwuarm"),
    SNAPDRAGON("Snapdragon"),
    CADANTINE("Cadantine"),
    LANTADYME("Lantadyme"),
    DWARF_WEED("Dwarf weed"),
    TORSTOL("Torstol");

    private final String name;

    HerbType(String name) {
        this.name = name;
    }

}