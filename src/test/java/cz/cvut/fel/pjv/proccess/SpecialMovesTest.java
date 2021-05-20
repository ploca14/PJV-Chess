package cz.cvut.fel.pjv.proccess;

import cz.cvut.fel.pjv.controller.BoardController;
import cz.cvut.fel.pjv.controller.GameController;
import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.chestpieces.*;
import cz.cvut.fel.pjv.view.GameView;
import cz.cvut.fel.pjv.view.TileView;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpecialMovesTest {
    private Game game;
    private GameView gameView;
    private GameController gameController;
    private BoardController boardController;
    private JFXPanel panel = new JFXPanel();

    @BeforeEach
    void setUp() {
        game = new Game();
        gameView = new GameView(game);
        gameController = spy(new GameController(game, gameView));
        boardController = spy(gameController.getBoardController());
        gameController.setBoardController(boardController);

        //LogManager.getLogManager().reset();
    }

    @Test
    void processSpecialMoves0() {
        /* ====== Board setup ====== */
        Board board = game.getBoard();
        Tile[][] boardTiles = game.getBoard().getBoard();

        King whiteKing = new King(Color.WHITE, boardTiles[7][4]);
        assertSame(boardTiles[7][4], whiteKing.getCurrentPosition());
        boardTiles[7][4].setCurrentChessPiece(whiteKing);
        assertSame(whiteKing, boardTiles[7][4].getCurrentChessPiece());
        board.addPiece(whiteKing);
        assertTrue(board.getWhitePieces().contains(whiteKing));

        King blackKing = new King(Color.BLACK, boardTiles[0][4]);
        assertSame(boardTiles[0][4], blackKing.getCurrentPosition());
        boardTiles[0][4].setCurrentChessPiece(blackKing);
        assertSame(blackKing, boardTiles[0][4].getCurrentChessPiece());
        board.addPiece(blackKing);
        assertTrue(board.getBlackPieces().contains(blackKing));

        Rook whiteRook = new Rook(Color.WHITE, boardTiles[7][7]);
        assertSame(boardTiles[7][7], whiteRook.getCurrentPosition());
        boardTiles[7][7].setCurrentChessPiece(whiteRook);
        assertSame(whiteRook, boardTiles[7][7].getCurrentChessPiece());
        board.addPiece(whiteRook);
        assertTrue(board.getWhitePieces().contains(whiteRook));

        Pawn whitePawn = new Pawn(Color.WHITE, boardTiles[6][5]);
        assertSame(boardTiles[6][5], whitePawn.getCurrentPosition());
        boardTiles[6][5].setCurrentChessPiece(whitePawn);
        assertSame(whitePawn, boardTiles[6][5].getCurrentChessPiece());
        board.addPiece(whitePawn);
        assertTrue(board.getWhitePieces().contains(whitePawn));

        Pawn promotingBlackPawn = new Pawn(Color.BLACK, boardTiles[6][1]);
        assertSame(boardTiles[6][1], promotingBlackPawn.getCurrentPosition());
        boardTiles[6][1].setCurrentChessPiece(promotingBlackPawn);
        assertSame(promotingBlackPawn, boardTiles[6][1].getCurrentChessPiece());
        board.addPiece(promotingBlackPawn);
        assertTrue(board.getBlackPieces().contains(promotingBlackPawn));

        Pawn enPassantBlackPawn = new Pawn(Color.BLACK, boardTiles[4][6]);
        enPassantBlackPawn.setStartingPosition(false);
        assertSame(boardTiles[4][6], enPassantBlackPawn.getCurrentPosition());
        boardTiles[4][6].setCurrentChessPiece(enPassantBlackPawn);
        assertSame(enPassantBlackPawn, boardTiles[4][6].getCurrentChessPiece());
        board.addPiece(enPassantBlackPawn);
        assertTrue(board.getBlackPieces().contains(enPassantBlackPawn));

        ArrayList<Tile> currentLegalMoves;

        /* ====== White pawn move ====== */
        boardController.setSelectedPiece(whitePawn);
        // Check selected piece
        assertSame(whitePawn, boardController.getSelectedPiece());
        currentLegalMoves = game.getRules().getLegalNotCheckMoves(whitePawn);
        // Check legal moves
        assertEquals(2, currentLegalMoves.size());
        assertTrue(currentLegalMoves.contains(boardTiles[5][5]));
        assertTrue(currentLegalMoves.contains(boardTiles[4][5]));
        boardController.makeMove(gameView.getBoardView().getNodeByRowColumnIndex(4, 5));
        // Check that the chess piece has been moved from this tile
        assertNull(boardTiles[6][5].getCurrentChessPiece());
        // Check that the chess piece has been moved to this tile
        assertSame(whitePawn, boardTiles[4][5].getCurrentChessPiece());

        /* ====== En passant move ====== */
        boardController.setSelectedPiece(enPassantBlackPawn);
        // Check selected piece
        assertSame(enPassantBlackPawn, boardController.getSelectedPiece());
        currentLegalMoves = game.getRules().getLegalNotCheckMoves(enPassantBlackPawn);
        // Check legal moves
        assertEquals(2, currentLegalMoves.size());
        assertTrue(currentLegalMoves.contains(boardTiles[5][5]));
        assertTrue(currentLegalMoves.contains(boardTiles[5][6]));
        boardController.makeMove(gameView.getBoardView().getNodeByRowColumnIndex(5, 5));
        // Check that the chess piece has been moved from this tile
        assertNull(boardTiles[4][6].getCurrentChessPiece());
        // Check that the chess piece has been moved to this tile
        assertSame(enPassantBlackPawn, boardTiles[5][5].getCurrentChessPiece());
        // Check that the white pawn has been taken
        assertNull(boardTiles[4][5].getCurrentChessPiece());
        assertFalse(board.getWhitePieces().contains(whitePawn));
        assertNull(whitePawn.getCurrentPosition());

        /* ====== Castling move ====== */
        boardController.setSelectedPiece(whiteKing);
        // Check selected piece
        assertSame(whiteKing, boardController.getSelectedPiece());
        currentLegalMoves = game.getRules().getLegalNotCheckMoves(whiteKing);
        // Check legal moves
        assertEquals(5, currentLegalMoves.size());
        assertTrue(currentLegalMoves.contains(boardTiles[6][3]));
        assertTrue(currentLegalMoves.contains(boardTiles[6][5]));
        assertTrue(currentLegalMoves.contains(boardTiles[7][3]));
        assertTrue(currentLegalMoves.contains(boardTiles[7][5]));
        assertTrue(currentLegalMoves.contains(boardTiles[7][6]));
        boardController.makeMove(gameView.getBoardView().getNodeByRowColumnIndex(7, 6));
        // Check that the chess piece has been moved from this tile
        assertNull(boardTiles[7][4].getCurrentChessPiece());
        // Check that the chess piece has been moved to this tile
        assertSame(whiteKing, boardTiles[7][6].getCurrentChessPiece());
        // Check that the rook has been moved
        assertNull(boardTiles[7][7].getCurrentChessPiece());
        assertSame(whiteRook, boardTiles[7][5].getCurrentChessPiece());

        /* ====== Pawn promoting move ====== */
        boardController.setSelectedPiece(promotingBlackPawn);
        // Check selected piece
        assertSame(promotingBlackPawn, boardController.getSelectedPiece());
        currentLegalMoves = game.getRules().getLegalNotCheckMoves(promotingBlackPawn);
        // Check legal moves
        assertEquals(1, currentLegalMoves.size());
        assertTrue(currentLegalMoves.contains(boardTiles[7][1]));
        doNothing().when(boardController).choosePieceForTile(any(TileView.class), anyBoolean());
        boardController.makeMove(gameView.getBoardView().getNodeByRowColumnIndex(7, 1));
        // Check that the choosePieceForTile method has been called
        verify(boardController, times(1)).choosePieceForTile(any(TileView.class), anyBoolean());
        Chesspiece promotedPiece = new Queen(Color.BLACK, boardTiles[7][1]);
        boardController.setChosenChesspieceAndRerender(gameView.getBoardView().getNodeByRowColumnIndex(7, 1), promotedPiece);
        // Check that the chess piece has been moved from this tile
        assertNull(boardTiles[6][1].getCurrentChessPiece());
        // Check that the chess piece has been moved to this tile
        assertSame(promotedPiece, boardTiles[7][1].getCurrentChessPiece());
    }

    @Test
    void processSpecialMoves1() {
        /* ====== Board setup ====== */
        Board board = game.getBoard();
        Tile[][] boardTiles = game.getBoard().getBoard();

        King whiteKing = new King(Color.WHITE, boardTiles[7][4]);
        assertSame(boardTiles[7][4], whiteKing.getCurrentPosition());
        boardTiles[7][4].setCurrentChessPiece(whiteKing);
        assertSame(whiteKing, boardTiles[7][4].getCurrentChessPiece());
        board.addPiece(whiteKing);
        assertTrue(board.getWhitePieces().contains(whiteKing));

        King blackKing = new King(Color.BLACK, boardTiles[0][4]);
        assertSame(boardTiles[0][4], blackKing.getCurrentPosition());
        boardTiles[0][4].setCurrentChessPiece(blackKing);
        assertSame(blackKing, boardTiles[0][4].getCurrentChessPiece());
        board.addPiece(blackKing);
        assertTrue(board.getBlackPieces().contains(blackKing));

        Rook leftWhiteRook = new Rook(Color.WHITE, boardTiles[7][3]);
        assertSame(boardTiles[7][3], leftWhiteRook.getCurrentPosition());
        boardTiles[7][3].setCurrentChessPiece(leftWhiteRook);
        assertSame(leftWhiteRook, boardTiles[7][3].getCurrentChessPiece());
        board.addPiece(leftWhiteRook);
        assertTrue(board.getWhitePieces().contains(leftWhiteRook));

        Rook rightWhiteRook = new Rook(Color.WHITE, boardTiles[7][5]);
        assertSame(boardTiles[7][5], rightWhiteRook.getCurrentPosition());
        boardTiles[7][5].setCurrentChessPiece(rightWhiteRook);
        assertSame(rightWhiteRook, boardTiles[7][5].getCurrentChessPiece());
        board.addPiece(rightWhiteRook);
        assertTrue(board.getWhitePieces().contains(rightWhiteRook));

        Rook blackRook = new Rook(Color.BLACK, boardTiles[1][4]);
        assertSame(boardTiles[1][4], blackRook.getCurrentPosition());
        boardTiles[1][4].setCurrentChessPiece(blackRook);
        assertSame(blackRook, boardTiles[1][4].getCurrentChessPiece());
        board.addPiece(blackRook);
        assertTrue(board.getBlackPieces().contains(blackRook));

        Pawn whitePawn = new Pawn(Color.WHITE, boardTiles[6][3]);
        assertSame(boardTiles[6][3], whitePawn.getCurrentPosition());
        boardTiles[6][3].setCurrentChessPiece(whitePawn);
        assertSame(whitePawn, boardTiles[6][3].getCurrentChessPiece());
        board.addPiece(whitePawn);
        assertTrue(board.getWhitePieces().contains(whitePawn));

        Pawn whitePawnToTake = new Pawn(Color.WHITE, boardTiles[4][5]);
        whitePawnToTake.setMoveCount(1);
        assertSame(boardTiles[4][5], whitePawnToTake.getCurrentPosition());
        boardTiles[4][5].setCurrentChessPiece(whitePawnToTake);
        assertSame(whitePawnToTake, boardTiles[4][5].getCurrentChessPiece());
        board.addPiece(whitePawnToTake);
        assertTrue(board.getWhitePieces().contains(whitePawnToTake));

        Pawn blackPawn = new Pawn(Color.BLACK, boardTiles[5][6]);
        blackPawn.setStartingPosition(false);
        assertSame(boardTiles[5][6], blackPawn.getCurrentPosition());
        boardTiles[5][6].setCurrentChessPiece(blackPawn);
        assertSame(blackPawn, boardTiles[5][6].getCurrentChessPiece());
        board.addPiece(blackPawn);
        assertTrue(board.getBlackPieces().contains(blackPawn));

        Pawn enPassantBlackPawn = new Pawn(Color.BLACK, boardTiles[4][4]);
        enPassantBlackPawn.setStartingPosition(false);
        assertSame(boardTiles[4][4], enPassantBlackPawn.getCurrentPosition());
        boardTiles[4][4].setCurrentChessPiece(enPassantBlackPawn);
        assertSame(enPassantBlackPawn, boardTiles[4][4].getCurrentChessPiece());
        board.addPiece(enPassantBlackPawn);
        assertTrue(board.getBlackPieces().contains(enPassantBlackPawn));

        ArrayList<Tile> currentLegalMoves;

        /* ====== En passant move ====== */
        game.takeTurn();
        boardController.setSelectedPiece(enPassantBlackPawn);
        // Check selected piece
        assertSame(enPassantBlackPawn, boardController.getSelectedPiece());
        currentLegalMoves = game.getRules().getLegalNotCheckMoves(enPassantBlackPawn);
        // Check legal moves
        assertEquals(2, currentLegalMoves.size());
        assertTrue(currentLegalMoves.contains(boardTiles[5][4]));
        assertTrue(currentLegalMoves.contains(boardTiles[5][5]));
        boardController.makeMove(gameView.getBoardView().getNodeByRowColumnIndex(5, 5));
        // Check that the chess piece has been moved from this tile
        assertNull(boardTiles[4][4].getCurrentChessPiece());
        // Check that the chess piece has been moved to this tile
        assertSame(enPassantBlackPawn, boardTiles[5][5].getCurrentChessPiece());
        // Check that the white pawn has been taken
        assertNull(boardTiles[4][5].getCurrentChessPiece());
        assertFalse(board.getWhitePieces().contains(whitePawnToTake));
        assertNull(whitePawnToTake.getCurrentPosition());

        gameController.checkGameState();
        // Check that the correct winner has been announced
        verify(gameController, times(1)).announceWinner(anyString(), eq(Color.BLACK));
    }

    @Test
    void processSpecialMoves2() {
        /* ====== Board setup ====== */
        Board board = game.getBoard();
        Tile[][] boardTiles = game.getBoard().getBoard();

        King whiteKing = new King(Color.WHITE, boardTiles[7][4]);
        assertSame(boardTiles[7][4], whiteKing.getCurrentPosition());
        boardTiles[7][4].setCurrentChessPiece(whiteKing);
        assertSame(whiteKing, boardTiles[7][4].getCurrentChessPiece());
        board.addPiece(whiteKing);
        assertTrue(board.getWhitePieces().contains(whiteKing));

        King blackKing = new King(Color.BLACK, boardTiles[0][4]);
        assertSame(boardTiles[0][4], blackKing.getCurrentPosition());
        boardTiles[0][4].setCurrentChessPiece(blackKing);
        assertSame(blackKing, boardTiles[0][4].getCurrentChessPiece());
        board.addPiece(blackKing);
        assertTrue(board.getBlackPieces().contains(blackKing));

        Rook whiteRook = new Rook(Color.WHITE, boardTiles[1][7]);
        assertSame(boardTiles[1][7], whiteRook.getCurrentPosition());
        boardTiles[1][7].setCurrentChessPiece(whiteRook);
        assertSame(whiteRook, boardTiles[1][7].getCurrentChessPiece());
        board.addPiece(whiteRook);
        assertTrue(board.getWhitePieces().contains(whiteRook));

        Rook blackRook = new Rook(Color.BLACK, boardTiles[0][5]);
        assertSame(boardTiles[0][5], blackRook.getCurrentPosition());
        boardTiles[0][5].setCurrentChessPiece(blackRook);
        assertSame(blackRook, boardTiles[0][5].getCurrentChessPiece());
        board.addPiece(blackRook);
        assertTrue(board.getBlackPieces().contains(blackRook));

        Pawn whitePawn = new Pawn(Color.WHITE, boardTiles[1][2]);
        assertSame(boardTiles[1][2], whitePawn.getCurrentPosition());
        boardTiles[1][2].setCurrentChessPiece(whitePawn);
        assertSame(whitePawn, boardTiles[1][2].getCurrentChessPiece());
        board.addPiece(whitePawn);
        assertTrue(board.getWhitePieces().contains(whitePawn));

        ArrayList<Tile> currentLegalMoves;

        /* ====== Pawn promoting move ====== */
        boardController.setSelectedPiece(whitePawn);
        // Check selected piece
        assertSame(whitePawn, boardController.getSelectedPiece());
        currentLegalMoves = game.getRules().getLegalNotCheckMoves(whitePawn);
        // Check legal moves
        assertEquals(1, currentLegalMoves.size());
        assertTrue(currentLegalMoves.contains(boardTiles[0][2]));
        doNothing().when(boardController).choosePieceForTile(any(TileView.class), anyBoolean());
        boardController.makeMove(gameView.getBoardView().getNodeByRowColumnIndex(0, 2));
        // Check that the choosePieceForTile method has been called
        verify(boardController, times(1)).choosePieceForTile(any(TileView.class), anyBoolean());
        Chesspiece promotedPiece = new Queen(Color.WHITE, boardTiles[0][2]);
        boardController.setChosenChesspieceAndRerender(gameView.getBoardView().getNodeByRowColumnIndex(0, 2), promotedPiece);
        // Check that the chess piece has been moved from this tile
        assertNull(boardTiles[1][2].getCurrentChessPiece());
        // Check that the chess piece has been moved to this tile
        assertSame(promotedPiece, boardTiles[0][2].getCurrentChessPiece());

        gameController.checkGameState();
        // Check that the correct winner has been announced
        verify(gameController, times(1)).announceWinner(anyString(), eq(Color.WHITE));
    }

    @Test
    void processSpecialMoves4() {
        /* ====== Board setup ====== */
        Board board = game.getBoard();
        Tile[][] boardTiles = game.getBoard().getBoard();

        King whiteKing = new King(Color.WHITE, boardTiles[2][4]);
        assertSame(boardTiles[2][4], whiteKing.getCurrentPosition());
        boardTiles[2][4].setCurrentChessPiece(whiteKing);
        assertSame(whiteKing, boardTiles[2][4].getCurrentChessPiece());
        board.addPiece(whiteKing);
        assertTrue(board.getWhitePieces().contains(whiteKing));

        King blackKing = new King(Color.BLACK, boardTiles[0][4]);
        assertSame(boardTiles[0][4], blackKing.getCurrentPosition());
        boardTiles[0][4].setCurrentChessPiece(blackKing);
        assertSame(blackKing, boardTiles[0][4].getCurrentChessPiece());
        board.addPiece(blackKing);
        assertTrue(board.getBlackPieces().contains(blackKing));

        Queen whiteQueen = new Queen(Color.WHITE, boardTiles[2][3]);
        assertSame(boardTiles[2][3], whiteQueen.getCurrentPosition());
        boardTiles[2][3].setCurrentChessPiece(whiteQueen);
        assertSame(whiteQueen, boardTiles[2][3].getCurrentChessPiece());
        board.addPiece(whiteQueen);
        assertTrue(board.getWhitePieces().contains(whiteQueen));

        ArrayList<Tile> currentLegalMoves;

        /* ====== Check mate move ====== */
        boardController.setSelectedPiece(whiteQueen);
        // Check selected piece
        assertSame(whiteQueen, boardController.getSelectedPiece());
        currentLegalMoves = game.getRules().getLegalNotCheckMoves(whiteQueen);
        // Check legal moves
        assertEquals(21, currentLegalMoves.size());
        boardController.makeMove(gameView.getBoardView().getNodeByRowColumnIndex(1, 4));
        // Check that the chess piece has been moved from this tile
        assertNull(boardTiles[2][3].getCurrentChessPiece());
        // Check that the chess piece has been moved to this tile
        assertSame(whiteQueen, boardTiles[1][4].getCurrentChessPiece());

        gameController.checkGameState();
        // Check that the correct winner has been announced
        verify(gameController, times(1)).announceWinner(anyString(), eq(Color.WHITE));
    }
}
