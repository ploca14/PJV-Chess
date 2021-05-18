package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChesspieceTest {
    private Chesspiece chesspiece;
    private Board board;

    @BeforeEach
    void setUp() {
        Game game = new Game();
        board = new Board(game);

        chesspiece = new King(Color.WHITE,board.getBoard()[6][0]);
        board.getBoard()[6][0].setCurrentChessPiece(chesspiece);
        board.getWhitePieces().add(chesspiece);
    }

    @Test
    void move_OneTileUpwards_Success() {
        int x = chesspiece.getCurrentPosition().getX();
        int y = chesspiece.getCurrentPosition().getY() - 1;
        Tile expectedEndingPosition = board.getBoard()[y][x];

        chesspiece.move(expectedEndingPosition);

        assertEquals(chesspiece.getCurrentPosition(), expectedEndingPosition);
    }

    @Test
    void isOccupied_CurrentPosition_True() {
        Tile[][] boardTiles = board.getBoard();
        int x = chesspiece.getCurrentPosition().getX();
        int y = chesspiece.getCurrentPosition().getY();
        boolean expected = true;

        boolean actual = chesspiece.isOccupied(boardTiles, x, y);

        assertEquals(expected, actual);
    }

    @Test
    void isOccupied_OneTileUpwards_False() {
        Tile[][] boardTiles = board.getBoard();
        int x = chesspiece.getCurrentPosition().getX();
        int y = chesspiece.getCurrentPosition().getY() - 1;
        boolean expected = false;

        boolean actual = chesspiece.isOccupied(boardTiles, x, y);

        assertEquals(expected, actual);
    }

    @Test
    void isOutOfRange_ExceedsBottom_True() {
        int x = 0;
        int y = 8;
        boolean expected = true;

        boolean actual = chesspiece.isOutOfRange(x, y);

        assertEquals(expected, actual);
    }

    @Test
    void isOutOfRange_ExceedsTop_True() {
        int x = 0;
        int y = -1;
        boolean expected = true;

        boolean actual = chesspiece.isOutOfRange(x, y);

        assertEquals(expected, actual);
    }

    @Test
    void isOutOfRange_ExceedsLeft_True() {
        int x = -1;
        int y = 0;
        boolean expected = true;

        boolean actual = chesspiece.isOutOfRange(x, y);

        assertEquals(expected, actual);
    }

    @Test
    void isOutOfRange_ExceedsRight_True() {
        int x = 8;
        int y = 0;
        boolean expected = true;

        boolean actual = chesspiece.isOutOfRange(x, y);

        assertEquals(expected, actual);
    }

    @Test
    void isOutOfRange_CornerTopLeft_False() {
        int x = 0;
        int y = 0;
        boolean expected = false;

        boolean actual = chesspiece.isOutOfRange(x, y);

        assertEquals(expected, actual);
    }

    @Test
    void isOutOfRange_CornerTopRight_False() {
        int x = 7;
        int y = 0;
        boolean expected = false;

        boolean actual = chesspiece.isOutOfRange(x, y);

        assertEquals(expected, actual);
    }

    @Test
    void isOutOfRange_CornerBottomLeft_False() {
        int x = 0;
        int y = 7;
        boolean expected = false;

        boolean actual = chesspiece.isOutOfRange(x, y);

        assertEquals(expected, actual);
    }

    @Test
    void isOutOfRange_CornerBottomRight_False() {
        int x = 7;
        int y = 7;
        boolean expected = false;

        boolean actual = chesspiece.isOutOfRange(x, y);

        assertEquals(expected, actual);
    }


    @Test
    void isTeammate_teammateTile_True() {
        Tile[][] boardTiles = board.getBoard();
        int x = chesspiece.getCurrentPosition().getX() + 1;
        int y = chesspiece.getCurrentPosition().getY();
        Chesspiece teammatechesspiece = new Pawn(Color.WHITE,board.getBoard()[y][x]);
        board.getBoard()[y][x].setCurrentChessPiece(teammatechesspiece);
        board.getWhitePieces().add(teammatechesspiece);
        boolean expected = true;

        boolean actual = chesspiece.isTeammate(boardTiles, x, y, chesspiece.getColor());

        assertEquals(expected, actual);
    }

    @Test
    void isTeammate_emptyTile_False() {
        Tile[][] boardTiles = board.getBoard();
        int x = chesspiece.getCurrentPosition().getX();
        int y = chesspiece.getCurrentPosition().getY() - 1;
        boolean expected = false;

        boolean actual = chesspiece.isTeammate(boardTiles, x, y, chesspiece.getColor());

        assertEquals(expected, actual);
    }

    @Test
    void isTeammate_enemyTile_False() {
        Tile[][] boardTiles = board.getBoard();
        int x = chesspiece.getCurrentPosition().getX();
        int y = chesspiece.getCurrentPosition().getY() - 5;
        boolean expected = false;

        boolean actual = chesspiece.isTeammate(boardTiles, x, y, chesspiece.getColor());

        assertEquals(expected, actual);
    }
}