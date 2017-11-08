package com.rfamorim.stonegame.game;

import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.util.List;

public class Game {

    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Player winnerPlayer;
    private Board board;

    public Game(@NotNull Player player1, @NotNull Player player2, @NotNull Board board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
        this.currentPlayer = player1;
//        this.winnerPlayer;
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

    public Player getWinnerPlayer() {
        return winnerPlayer;
    }

    public Board getBoard() {
        return board;
    }

    void setWinnerPlayer(Player player) { this.winnerPlayer = player; }

    public void play(int pitIndex) throws InvalidParameterException {
        board.validatePitIndex(pitIndex, currentPlayer);
        Pit lastPit = board.moveStones(pitIndex, currentPlayer);

        if (!(board.isPitFromPlayer(lastPit, currentPlayer) && lastPit.getBigPit())) {
            toggleCurrentPlayer();
        }

        if (isGameOver()) {
            winnerPlayer = board.getWinnerPlayer(player1, player2);
        };
    }

    public void toggleCurrentPlayer() {
        currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
    }

    private boolean isGameOver() {
        List<Pit> pitList = board.getPitList();
        int player1StoneCount = 0;
        int player2StoneCount = 0;

        for (int i = 0; i < pitList.size(); i++) {
            Pit pit = pitList.get(i);

            if (!pit.getBigPit()) {
                if (pit.getPlayer().equals(player1)) {
                    player1StoneCount += pit.getStoneCount();
                } else {
                    player2StoneCount += pit.getStoneCount();
                }
            }
        }

        if (player1StoneCount == 0 || player2StoneCount == 0) { return true; }
        return false;
    }

}
