package com.rfamorim.stonegame.game.di;


import com.rfamorim.stonegame.game.*;


public class GameFactory {

    private static Player providePlayer(String name) {
        return new Player(name);
    }

    public static Game provideGame(String playerName1, String playerName2, Board board) {
        Player p1 = providePlayer(playerName1);
        Player p2 = providePlayer(playerName2);
        return new Game(p1, p2, board);
    }

    public static Game provideGame(String playerName1, String playerName2) {
        Player p1 = providePlayer(playerName1);
        Player p2 = providePlayer(playerName2);
        int totalPits = 14;
        int stonesPerPit = 6;
        Board board = provideBoard(totalPits, stonesPerPit, p1, p2);
        return new Game(p1, p2, board);
    }

    public static Board provideBoard(int totalPits, int stoneCount, Player player1, Player player2) {
        Board.CircularArrayList<Pit> pits = new Board.CircularArrayList<>();
        PitBuilder builder = new PitBuilder();
        boolean bigPit;
        for (int i = 0; i < totalPits; i++) {
            bigPit = i == totalPits - 1 || i == totalPits / 2 - 1;
            builder.setBigPit(bigPit)
                    .setStoneCount(bigPit ? 0 : stoneCount)
                    .setPlayer(i < totalPits / 2 ? player1 : player2)
                    .createPit();
            pits.add(builder.createPit());
        }
        return new Board(pits);
    }

    public static Board provideEndGameBoard(int totalPits, int stoneCount, Player player1, Player player2) {
        Board.CircularArrayList<Pit> pits = new Board.CircularArrayList<>();
        PitBuilder builder = new PitBuilder();
        boolean bigPit;
        for (int i = 0; i < totalPits; i++) {
            bigPit = i == totalPits - 1 || i == totalPits / 2 - 1;

            if (i < totalPits/2) {
                builder.setBigPit(bigPit)
                        .setStoneCount(bigPit ? 7 * stoneCount : 0)
                        .setPlayer(i < totalPits / 2 ? player1 : player2)
                        .createPit();
                pits.add(builder.createPit());
            } else {
                builder.setBigPit(bigPit)
                        .setStoneCount(bigPit ? 0 : stoneCount - 1)
                        .setPlayer(i < totalPits / 2 ? player1 : player2)
                        .createPit();
                pits.add(builder.createPit());
            }
        }
        return new Board(pits);
    }

}
