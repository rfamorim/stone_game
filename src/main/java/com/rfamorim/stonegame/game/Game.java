package com.rfamorim.stonegame.game;

import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;

public class Game {

    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Board board;

    public Game(@NotNull Player player1, @NotNull Player player2, @NotNull Board board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
        currentPlayer = player1;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public void play(int pitIndex) throws InvalidParameterException {
        board.validatePitIndex(pitIndex, currentPlayer);
        Pit lastPit = board.moveStones(pitIndex, currentPlayer);
        if (!(board.isPitFromPlayer(lastPit, currentPlayer) && lastPit.getBigPit())) {
            toggleCurrentPlayer();
        }
    }

    public void toggleCurrentPlayer() {
        currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
    }

}
