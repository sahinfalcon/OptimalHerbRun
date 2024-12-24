package com.optimalHerbRun.data;

import net.runelite.api.coords.WorldPoint;
import java.time.Instant;
import lombok.Data;

@Data
public class HerbPatch {
    private final WorldPoint location;
    private Instant plantTime;
    private HerbType type;
    private boolean isDiseased;
}