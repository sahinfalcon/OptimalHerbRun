package com.optimalHerbRun.data;

import lombok.Data;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

@Data
public class HerbPatch {
    private final WorldPoint location;
    private boolean isDiseased;
    private boolean isProtected;
    private int currentStage;
    private static final int TOTAL_STAGES = 5;
    private HerbType herbType;

    public String getDisplayState() {
        switch (currentStage) {
            case 0:
            case 3:
                return "Empty";
            case 1:
            case 2:
                return "Weeds";
            case 4:
            case 5:
            case 6:
            case 7:
                int growthStage = currentStage - 3;
                String herbName = herbType != null ? herbType.getName() : "Herb";
                return String.format("%s (%d/5)", herbName, growthStage);
            case 8:
                return herbType != null ? herbType.getName() + " READY" : "READY";
            case 9:
            case 10:
                return "Harvesting";
            case 170:
            case 171:
            case 172:
                return "Dead";
            default:
                return "Unknown";
        }
    }

    public PatchStatus getStatus() {
        // Dead state takes priority
        if (isDiseased || (currentStage >= 170 && currentStage <= 172)) {
            return PatchStatus.DEAD;
        }

        // Ready state
        if (currentStage == 8) {
            return PatchStatus.READY;
        }

        // Protection status for growing patches
        if (currentStage >= 4 && currentStage <= 7) {
            if (isProtected || isDiseaseFree(null)) {
                return PatchStatus.PROTECTED;
            }
            return PatchStatus.UNPROTECTED;
        }

        // Default state for other cases
        return PatchStatus.NORMAL;
    }

    public void updateState(int value) {
        this.currentStage = value;

        // Reset disease state when planting or clearing
        if (value >= 3 && value <= 8) {
            setDiseased(false);
        }
        // Set disease state for dead plants
        else if (value >= 170 && value <= 172) {
            setDiseased(true);
        }
    }

    public boolean isDiseaseFree(Client client) {
        HerbPatchLocation patchLocation = HerbPatchLocation.getForLocation(location);
        return patchLocation != null && patchLocation.isDiseaseFree(client);
    }

    public String getLocationName() {
        return HerbPatchLocation.getNameForLocation(location);
    }
}