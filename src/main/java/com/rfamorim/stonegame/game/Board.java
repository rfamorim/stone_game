package com.rfamorim.stonegame.game;

import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final String BIG_PIT_ERROR = "Can't play big pit";
    public static final String EMPTY_PIT_ERROR = "Empty pit";
    public static final String PIT_OWNER_ERROR = "Not player's pit";

    private final List<Pit> pitList;

    public Board(CircularArrayList<Pit> pitList) {
        this.pitList = pitList;
    }

    public List<Pit> getPitList() {
        return pitList;
    }

    public boolean isPitFromPlayer(@NotNull Pit pit, @NotNull Player player) {
        return pit.getPlayer().equals(player);
    }

    public Pit moveStones(int pitIndex, @NotNull Player currentPlayer) {
        Pit pit = pitList.get(pitIndex);
        Pit currentPit = null;
        int stones = pit.getStoneCount();
        int nextPit = pitIndex;
        pit.setStoneCount(0);

        while (stones > 0) {
            nextPit += 1;
            currentPit = pitList.get(nextPit);
            if (!currentPit.getBigPit() || currentPit.getPlayer().equals(currentPlayer)) {
                currentPit.incrementStoneCount();
                stones--;
            }
        }

        if (stoppedAtEmptyPit(currentPit, currentPlayer)) {
            robStonesFromOtherPit(currentPit, nextPit);
        }

        return currentPit;

    }

    private boolean stoppedAtEmptyPit(@NotNull Pit pit, @NotNull Player player) {
        return !pit.getBigPit() && pit.getPlayer().equals(player) && pit.getStoneCount() == 1;
    }

    private void robStonesFromOtherPit(@NotNull Pit currentPit, int pitIndex) {
        Pit opponentsPit = pitList.get(pitList.size() - pitIndex - 2);
        currentPit.setStoneCount(currentPit.getStoneCount() + opponentsPit.getStoneCount());
        opponentsPit.setStoneCount(0);
    }

    public void validatePitIndex(int pitIndex, @NotNull Player currentPlayer) throws InvalidParameterException {
        Pit pit = pitList.get(pitIndex);
        if (pit.getBigPit()) {
            throw new InvalidParameterException(BIG_PIT_ERROR);
        }
        if (pit.getStoneCount() == 0) {
            throw new InvalidParameterException(EMPTY_PIT_ERROR);
        }
        if (!isPitFromPlayer(pit, currentPlayer)) {
            throw new InvalidParameterException(PIT_OWNER_ERROR);
        }
    }

    public static class CircularArrayList<T> extends ArrayList<T> {
        @Override
        public T get(int index) {
            return super.get(index % size());
        }
    }

}
