package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    private Pawn pawn;
    private Board board;

    @BeforeEach
    void setUp() {
        Game game = new Game();
        board = new Board(game);
        board.placeChessPieces();

        pawn = new Pawn(Color.WHITE,board.getBoard()[6][0]);
        board.getBoard()[6][0].setCurrentChessPiece(pawn);
        board.getWhitePieces().add(pawn);
        pawn.setPlayingBoard(board);
    }

    @Test
    void move_OneTileUpwards_Success() {
        int x = pawn.getCurrentPosition().getX();
        int y = pawn.getCurrentPosition().getY() - 1;
        Tile expectedEndingPosition = board.getBoard()[y][x];
        int expectedMoveCount = 1;
        boolean expectedIsStartingPosition = false;
        int expectedLastRoundMoved = 0;

        pawn.move(expectedEndingPosition);

        assertEquals(expectedEndingPosition, pawn.getCurrentPosition());
        assertEquals(expectedMoveCount, pawn.getMoveCount());
        assertEquals(expectedIsStartingPosition, pawn.isStartingPosition());
        assertEquals(expectedLastRoundMoved, pawn.getLastRoundMoved());
    }

    @Test
    void testGetLegalMoves_startingPosition_oneTwoUpwards() {
        int x = pawn.getCurrentPosition().getX();
        int y = pawn.getCurrentPosition().getY();
        ArrayList<Tile> expected = new ArrayList<>();
        expected.add(board.getBoard()[y-1][x]);
        expected.add(board.getBoard()[y-2][x]);

        ArrayList<Tile> actual = pawn.getLegalMoves(pawn.getCurrentPosition(), board);

        assertEquals(expected, actual);
    }
}