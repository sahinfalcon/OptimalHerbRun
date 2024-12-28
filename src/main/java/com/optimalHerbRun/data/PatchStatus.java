package com.optimalHerbRun.data;

import lombok.Getter;

import java.awt.Color;

@Getter
public enum PatchStatus {
    READY(Color.GREEN),
    PROTECTED(Color.CYAN),
    UNPROTECTED(Color.ORANGE),
    DEAD(Color.RED),
    NORMAL(Color.WHITE);

    private final Color color;

    PatchStatus(Color color) {
        this.color = color;
    }

}