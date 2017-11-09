package com.rfamorim.stonegame;

import com.rfamorim.stonegame.game.Board;
import com.rfamorim.stonegame.game.Game;
import com.rfamorim.stonegame.game.Pit;
import com.rfamorim.stonegame.game.Player;
import com.rfamorim.stonegame.game.di.GameFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

public class GameTests {

    private static final String PLAYER1 = "ZICO";
    private static final String PLAYER2 = "PELÉ";

    private Game game;
    @Mock
    private Board board;
    @Mock
    private Pit pit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        game = GameFactory.provideGame(PLAYER1, PLAYER2, board);
    }

    @Test
    public void test_create_game() {
        assertEquals(game.getPlayer1().getName(), PLAYER1);
        assertEquals(game.getPlayer2().getName(), PLAYER2);
        assertEquals(game.getPlayer1(), game.getCurrentPlayer());
        assertEquals(game.getCurrentPlayer().getName(), PLAYER1);
        assertEquals(game.getWinnerPlayer(), null);
    }

    @Test
    public void test_toggle_current_player() {
        game.toggleCurrentPlayer();
        assertEquals(game.getPlayer2(), game.getCurrentPlayer());
    }

    @Test
    public void test_ended_in_players_big_pit() {
        when(pit.getBigPit()).thenReturn(true);
        when(board.isPitFromPlayer(pit, game.getPlayer1())).thenReturn(true);
        when(board.moveStones(anyInt(), any(Player.class))).thenReturn(pit);
        try {
            // So it's still player 1 round
            game.play(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(game.getPlayer1(), game.getCurrentPlayer());
    }

    @Test
    public void test_ended_in_small_pit() {
        when(pit.getBigPit()).thenReturn(false);
        when(board.isPitFromPlayer(pit, game.getPlayer1())).thenReturn(true);
        when(board.moveStones(anyInt(), any(Player.class))).thenReturn(pit);
        try {
            game.play(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(game.getPlayer2(), game.getCurrentPlayer());
    }

    @Test
    public void test_ended_in_small_opponents_pit() {
        when(pit.getBigPit()).thenReturn(true);
        when(board.isPitFromPlayer(pit, game.getPlayer1())).thenReturn(false);
        when(board.moveStones(anyInt(), any(Player.class))).thenReturn(pit);
        try {
            game.play(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(game.getPlayer2(), game.getCurrentPlayer());
    }

    @Test
    public void test_ended_in_bit_opponents_pit() {
        when(pit.getBigPit()).thenReturn(false);
        when(board.isPitFromPlayer(pit, game.getPlayer1())).thenReturn(false);
        when(board.moveStones(anyInt(), any(Player.class))).thenReturn(pit);
        try {
            game.play(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(game.getPlayer2(), game.getCurrentPlayer());
    }

}
