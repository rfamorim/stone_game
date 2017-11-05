package com.rfamorim.stonegame.game;

import javax.validation.constraints.NotNull;

public class Pit {

    private int stoneCount;
    private boolean bigPit = false;
    private Player player;

    public Pit(int stoneCount, boolean bigPit, @NotNull Player player) {
        this.stoneCount = stoneCount;
        this.bigPit = bigPit;
        this.player = player;
    }

    public int getStoneCount() {
        return stoneCount;
    }

    void setStoneCount(int stoneCount) {
        this.stoneCount = stoneCount;
    }

    public boolean getBigPit() {
        return bigPit;
    }

    public Player getPlayer() {
        return player;
    }

    void incrementStoneCount() {
        stoneCount++;
    }
}
