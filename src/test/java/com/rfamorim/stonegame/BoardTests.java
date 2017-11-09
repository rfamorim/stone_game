package com.rfamorim.stonegame;


import com.rfamorim.stonegame.game.Board;
import com.rfamorim.stonegame.game.Pit;
import com.rfamorim.stonegame.game.Player;
import com.rfamorim.stonegame.game.di.GameFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.InvalidParameterException;
import java.util.List;

import static com.rfamorim.stonegame.game.Board.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

public class BoardTests {

    private static final int TOTAL_PITS = 14;
    private static final int STONE_PER_PIT = 6;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private Player player1;
    @Mock
    private Player player2;
    @Mock
    private CircularArrayList<Pit> pits;

    private Board board;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        board = GameFactory.provideBoard(TOTAL_PITS, STONE_PER_PIT, player1, player2);
    }
    
    @Test
    public void test_move_stone() {
        // First move: player1 at pit-0
        board.moveStones(0, player1);
        assertEquals(board.getPitList().get(0).getStoneCount(), 0);
        assertEquals(board.getPitList().get(1).getStoneCount(), STONE_PER_PIT + 1);
        assertEquals(board.getPitList().get((TOTAL_PITS/2) - 2).getStoneCount(), STONE_PER_PIT + 1);
        assertEquals(board.getPitList().get((TOTAL_PITS/2) - 1).getStoneCount(), 1);

        assertEquals(board.getPitList().get(TOTAL_PITS/2).getStoneCount(), STONE_PER_PIT);
        assertEquals(board.getPitList().get(TOTAL_PITS - 1).getStoneCount(), 0);

        // Second move: player1 at pit-1
        board.moveStones(1, player1);
        assertEquals(board.getPitList().get(0).getStoneCount(), 0);
        assertEquals(board.getPitList().get(1).getStoneCount(), 0);
        assertEquals(board.getPitList().get((TOTAL_PITS/2) - 2).getStoneCount(), STONE_PER_PIT + 2);
        assertEquals(board.getPitList().get((TOTAL_PITS/2) - 1).getStoneCount(), 2);

        assertEquals(board.getPitList().get(TOTAL_PITS/2).getStoneCount(), STONE_PER_PIT + 1);
        assertEquals(board.getPitList().get(TOTAL_PITS - 1).getStoneCount(), 0);

        // Third move: player2 at pit-{TOTAL_PITS/2}
        board.moveStones(TOTAL_PITS/2, player2);
        assertEquals(board.getPitList().get(0).getStoneCount(), 1);
        assertEquals(board.getPitList().get(1).getStoneCount(), 0);
        assertEquals(board.getPitList().get((TOTAL_PITS/2) - 2).getStoneCount(), STONE_PER_PIT + 2);
        assertEquals(board.getPitList().get((TOTAL_PITS/2) - 1).getStoneCount(), 2);

        assertEquals(board.getPitList().get(TOTAL_PITS/2).getStoneCount(), 0);
        assertEquals(board.getPitList().get(TOTAL_PITS - 1).getStoneCount(), 1);

        // Fourth move: player1 at pit-{(TOTAL_PITS/2) - 2}. When completes a cycle and should not change the player2's bitPit count
        board.moveStones((TOTAL_PITS/2) - 2, player1);
        assertEquals(board.getPitList().get(0).getStoneCount(), 2);
        assertEquals(board.getPitList().get(1).getStoneCount(), 0);
        assertEquals(board.getPitList().get((TOTAL_PITS/2) - 2).getStoneCount(), 0);
        assertEquals(board.getPitList().get((TOTAL_PITS/2) - 1).getStoneCount(), 3);

        assertEquals(board.getPitList().get(TOTAL_PITS/2).getStoneCount(), 1);
        assertEquals(board.getPitList().get(TOTAL_PITS - 1).getStoneCount(), 1);
    }

    @Test
    public void test_pit_index_validation_from_opponents() {
        exception.expect(InvalidParameterException.class);
        exception.expectMessage(PIT_OWNER_ERROR);
        board.validatePitIndex(0, player2);
    }

    @Test
    public void test_pit_index_validation_moving_bit_pit() {
        exception.expect(InvalidParameterException.class);
        exception.expectMessage(BIG_PIT_ERROR);
        board.validatePitIndex(6, player1);
    }

    @Test
    public void test_pit_index_validation_moving_on_empty_pit() {
        Board board = new Board(pits);
        Pit dummyPit = new Pit(0, false, player1);
        when(pits.get(anyInt())).thenReturn(dummyPit);
        exception.expect(InvalidParameterException.class);
        exception.expectMessage(EMPTY_PIT_ERROR);
        board.validatePitIndex(6, player1);
    }

    @Test
    public void test_board_is_created_correctly() {
        // the list must have 6 pits and a big pit for player 1, the 7th. a big pit(the last one) for player two and the rest small
        // pits for player 2.
        int half = TOTAL_PITS / 2 - 1;
        List<Pit> pitList = board.getPitList();
        Pit pit;
        for (int i = 0; i < TOTAL_PITS; i++) {
            pit = pitList.get(i);
            assertEquals(pit.getPlayer(), i <= half ? player1 : player2);
            if (i == half || i == TOTAL_PITS - 1) {
                assertEquals(pit.getStoneCount(), 0);
                assertTrue(pit.getBigPit());
            } else {
                assertEquals(pit.getStoneCount(), STONE_PER_PIT);
                assertFalse(pitList.get(i).getBigPit());
            }
        }
    }

    @Test
    public void test_end_of_game_moving_stones() {
        board = GameFactory.provideEndGameBoard(TOTAL_PITS, STONE_PER_PIT, player1, player2);
        assertEquals(board.moveStonesToBigPit(player1, player2), player1);
    }

    @Test
    public void test_get_winner_player_method() {
        board = GameFactory.provideEndGameBoard(TOTAL_PITS, STONE_PER_PIT, player1, player2);
        assertEquals(board.getWinnerPlayer(player1, player2), player1);
    }

}
