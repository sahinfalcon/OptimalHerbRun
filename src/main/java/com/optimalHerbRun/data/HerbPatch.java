package com.optimalHerbRun.data;

import lombok.Data;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

import java.time.Duration;
import java.time.Instant;

@Data
public class HerbPatch {
    private final WorldPoint location;
    private String growthStage = "Unknown";
    private boolean isDiseased;
    private Instant plantedTime;
    private boolean isProtected;
    private static final Duration GROWTH_TIME = Duration.ofMinutes(16);


    public void updateState(int value) {
        String oldStage = growthStage;
        switch (value) {
            case 0:
                return; // Ignore 0 value
            case 1:
            case 2:
                setGrowthStage("Raking");
                break;
            case 3:
                setGrowthStage("Empty");
                break;
            case 4:
                setGrowthStage("Stage 1");
                if (!oldStage.equals("Stage 1")){
                    plantedTime = Instant.now();
                }
                break;
            case 5:
                setGrowthStage("Stage 2");
                break;
            case 6:
                setGrowthStage("Stage 3");
                break;
            case 7:
                setGrowthStage("Stage 4");
                break;
            case 8:
                setGrowthStage("Ready");
                break;
            case 9:
            case 10:
                setGrowthStage("Harvesting");
                break;
            case 129:
            case 170:
            case 171:
            case 172:
                setGrowthStage("Dead");
                setDiseased(true);
                break;
            default:
                setGrowthStage("Unknown");
        }
    }

    public boolean isDiseaseFree(Client client) {
        HerbPatchLocation patchLocation = HerbPatchLocation.getForLocation(location);
        return patchLocation != null && patchLocation.isDiseaseFree(client);
    }

    public String getLocationName() {
        return HerbPatchLocation.getNameForLocation(location);
    }

    public String getTimeElapsed(){
        if (plantedTime == null) return "Time Unknown";
        long minutes = Duration.between(plantedTime, Instant.now()).toMinutes();
        return minutes + " mins";
    }

    public String getStageEstimate() {
        if (plantedTime == null) return "";
        long mins = Duration.between(plantedTime, Instant.now()).toMinutes();
        long nextStage = 20 - (mins % 20);
        return "~" + nextStage + "m";
    }
}