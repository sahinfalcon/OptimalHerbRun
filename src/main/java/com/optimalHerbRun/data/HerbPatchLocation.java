package com.optimalHerbRun.data;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;
import java.util.Arrays;

@Getter
public enum HerbPatchLocation {
    FARMING_GUILD(33979, new WorldPoint(1239, 3727, 0), "Farming Guild"),
    HARMONY(9372, new WorldPoint(3790, 2838, 0), "Harmony Island"),
    VALAMORE(50697, new WorldPoint(1582, 3095, 0), "Valamore"),
    HOSIDIUS(27115, new WorldPoint(1738, 3551, 0), "Hosidius"),
    CATHERBY(8151, new WorldPoint(2813, 3463, 0), "Catherby"),
    MORYTANIA(8153, new WorldPoint(3605, 3529, 0), "Morytania"),
    FALADOR(8150, new WorldPoint(3058, 3311, 0), "Falador"),
    ARDOUGNE(8152, new WorldPoint(2670, 3374, 0), "Ardougne");

    private final int objectId;
    private final WorldPoint location;
    private final String name;

    HerbPatchLocation(int objectId, WorldPoint location, String name) {
        this.objectId = objectId;
        this.location = location;
        this.name = name;
    }

    public static String getNameForLocation(WorldPoint point) {
        return Arrays.stream(values())
                .filter(loc -> loc.getLocation().equals(point))
                .findFirst()
                .map(HerbPatchLocation::getName)
                .orElse("Unknown");
    }
}