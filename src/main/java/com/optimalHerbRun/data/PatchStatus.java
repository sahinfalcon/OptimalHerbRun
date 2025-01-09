package com.optimalHerbRun.data;

import com.optimalHerbRun.OptimalHerbRunConfig;
import lombok.Getter;

import java.awt.Color;

@Getter
public enum PatchStatus {
    READY,
    GROWING,
    DEAD,
    PROTECTED,
    UNPROTECTED,
    EMPTY,
    NORMAL;

    public Color getColor(OptimalHerbRunConfig config) {
        switch (this) {
            case READY: return config.readyColor();
            case DEAD: return config.deadColor();
            case GROWING: return config.growingColor();
            default: return Color.WHITE;
        }
    }
}