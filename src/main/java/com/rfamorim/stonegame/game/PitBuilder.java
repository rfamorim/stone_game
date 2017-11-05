package com.rfamorim.stonegame.game;

public class PitBuilder {
    private int stoneCount;
    private boolean bigPit;
    private Player player;

    public PitBuilder setStoneCount(int stoneCount) {
        this.stoneCount = stoneCount;
        return this;
    }

    public PitBuilder setBigPit(boolean bigPit) {
        this.bigPit = bigPit;
        return this;
    }

    public PitBuilder setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public Pit createPit() {
        return new Pit(stoneCount, bigPit, player);
    }
}