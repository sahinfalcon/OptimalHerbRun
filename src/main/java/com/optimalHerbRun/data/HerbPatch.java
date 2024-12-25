package com.optimalHerbRun.data;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import java.time.Instant;
import lombok.Data;

@Slf4j
@Data
public class HerbPatch {
    private final WorldPoint location;
    private Instant plantTime;
    private HerbType type;
    private boolean isDiseased;
    private String growthStage = "Empty";

    public void setDiseased(boolean diseased){
        this.isDiseased = diseased;
        log.info("Patch at {} disease state changed to: {}", location, diseased);
    }
}