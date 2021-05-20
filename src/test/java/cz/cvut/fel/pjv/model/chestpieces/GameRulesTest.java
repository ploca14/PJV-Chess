package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.controller.GameRules;
import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class GameRulesTest {

    @Test
    public void whiteKingIsInCheckFromEnemyPawn() {
        Game game = Mockito.mock(Game.class);
        Board board = new Board(game);

        Tile[][] playingBoard = board.getBoard();
        board.placeChessPieces();
        Tile firstExpectedTile = playingBoard[7][4];
        Tile secondExpectedTile = playingBoard[7][2];
        ArrayList<Tile> expectedLegalMoves = new ArrayList<>();
        expectedLegalMoves.add(firstExpectedTile);
        expectedLegalMoves.add(secondExpectedTile);

        Chesspiece testCp = Mockito.mock(Pawn.class);
        when(testCp.getCurrentPosition()).thenReturn(playingBoard[6][3]);
        when(testCp.getColor()).thenReturn(Color.BLACK);
        when(testCp.getLastPosition()).thenReturn(playingBoard[6][3]);
        when(testCp.getLastRoundMoved()).thenReturn(0);
        when(testCp.getMoveCount()).thenReturn(0);
        when(testCp.getLegalMoves(testCp.getCurrentPosition(), board)).thenReturn(expectedLegalMoves);
        board.getBlackPieces().add(testCp);

        boolean expectedBool = true;

        GameRules gameRules = new GameRules(board);
        boolean resultBool = gameRules.isCheck(Color.WHITE, board);

        Assertions.assertEquals(expectedBool, resultBool);
    }

    @Test
    public void checkedWhiteKingExpectedToHaveNoLegalmoves() {
        Game game = Mockito.mock(Game.class);
        Board board = new Board(game);

        Tile[][] playingBoard = board.getBoard();
        board.placeChessPieces();

        Chesspiece testCp = Mockito.mock(Queen.class);
        board.blackPieces.add(testCp);
        when(testCp.getCurrentPosition()).thenReturn(playingBoard[4][7]);
        when(testCp.getColor()).thenReturn(Color.BLACK);
        board.getBlackPieces().add(testCp);

        playingBoard[6][6].setCurrentChessPiece(null);
        playingBoard[6][5].setCurrentChessPiece(null);
        playingBoard[4][7].setCurrentChessPiece(testCp);

        Chesspiece whiteKing = null;
        for (Chesspiece cp: board.getWhitePieces()
             ) {
            if(cp instanceof King) {
                whiteKing = cp;
            }
        }

        Assertions.assertEquals( 0, whiteKing.getLegalMoves(whiteKing.getCurrentPosition(), board).size());
    }
    @Test
    public void blackKingIsInCheckFromEnemyPawn() {
        Game game = Mockito.mock(Game.class);
        Board board = new Board(game);

        Tile[][] playingBoard = board.getBoard();
        board.placeChessPieces();
        Tile firstExpectedTile = playingBoard[0][4];
        Tile secondExpectedTile = playingBoard[0][2];
        ArrayList<Tile> expectedLegalMoves = new ArrayList<>();
        expectedLegalMoves.add(firstExpectedTile);
        expectedLegalMoves.add(secondExpectedTile);

        Chesspiece testCp = Mockito.mock(Pawn.class);
        when(testCp.getCurrentPosition()).thenReturn(playingBoard[1][3]);
        when(testCp.getColor()).thenReturn(Color.WHITE);
        when(testCp.getLastPosition()).thenReturn(playingBoard[1][3]);
        when(testCp.getLastRoundMoved()).thenReturn(0);
        when(testCp.getMoveCount()).thenReturn(0);
        when(testCp.getLegalMoves(testCp.getCurrentPosition(), board)).thenReturn(expectedLegalMoves);
        board.getWhitePieces().add(testCp);

        boolean expectedBool = true;

        GameRules gameRules = new GameRules(board);
        boolean resultBool = gameRules.isCheck(Color.BLACK, board);

        Assertions.assertEquals(expectedBool, resultBool);
    }

    @Test
    public void checkedBlackKingExpectedToHaveNoLegalmovesS() {
        Game game = Mockito.mock(Game.class);
        Board board = new Board(game);

        Tile[][] playingBoard = board.getBoard();
        board.placeChessPieces();

        Chesspiece testCp = Mockito.mock(Queen.class);
        board.whitePieces.add(testCp);
        when(testCp.getCurrentPosition()).thenReturn(playingBoard[3][7]);
        when(testCp.getColor()).thenReturn(Color.WHITE);
        board.getWhitePieces().add(testCp);

        playingBoard[1][6].setCurrentChessPiece(null);
        playingBoard[1][5].setCurrentChessPiece(null);
        playingBoard[3][7].setCurrentChessPiece(testCp);

        Chesspiece blackKing = null;
        for (Chesspiece cp: board.getWhitePieces()
        ) {
            if(cp instanceof King) {
                blackKing = cp;
            }
        }

        Assertions.assertEquals( 0, blackKing.getLegalMoves(blackKing.getCurrentPosition(), board).size());
    }
}
