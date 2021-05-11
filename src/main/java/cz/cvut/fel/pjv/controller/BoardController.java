package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.*;
import cz.cvut.fel.pjv.view.BoardView;
import cz.cvut.fel.pjv.view.PiecePickerView;
import cz.cvut.fel.pjv.view.TileView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class BoardController {
    private final Board boardModel;
    private final BoardView boardView;
    private GameController gameController;

    public BoardController(Board boardModel, BoardView boardView) {
        this.boardModel = boardModel;
        this.boardView = boardView;

        initController();
    }

    private void initController() {
        createEventListeners();
    }

    private void createEventListeners() {
        // Create event listeners for all the tiles in the boardView
        for (Node node : boardView.getChildren()) {
            TileView tileView = (TileView) node;

            // New mouse click handler for this tile
            EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // Check if the board is editable
                    if(boardModel.isEditable()) {
                        handleEdit(mouseEvent);

                    } else {
                        handleTurn();
                    }
                }

                private void handleEdit(MouseEvent mouseEvent) {
                    // If the mouse event is a right click we remove the chesspiece
                    if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                        // Check if the tile is already occupied
                        removePiece(tileView);
                        // If the mouse event is a left click we choose a new piece
                    } else if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        // If it is choose a piece for this tile
                        choosePieceForTile(tileView, true);
                    }
                }

                private void handleTurn() {
                    // If its not editable check if this tile is a legal move of the currenttly selected piece
                    if (gameController.getSelectedPiece() != null && tileView.isLegalMove()) {
                        // If it is then make a move to this tile
                        makeMove(tileView);
                    } else {
                        // If its not, try to select a piece from this tile
                        selectPiece(tileView);
                    }
                }
            };

            // Add the handler to the tile
            node.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        }
    }

    /**
     * This method is used to remove a chess piece from a specific tile
     * @param tileView The tile where we want to remove a chesspiece
     */
    private void removePiece(TileView tileView) {
        // Check if the the tile is occupied
        if (tileView.getTileModel().getCurrentChessPiece() != null) {
            // If it is occupied then remove the occupying piece
            boardModel.removePiece(tileView.getTileModel().getCurrentChessPiece());
            tileView.getTileModel().setCurrentChessPiece(null);
        }

        // Update the tileView
        tileView.rerender();
    }

    /**
     * @param tileView The the for which we want to choose a new piece
     * @param showAll Whetheer we want to show all the chesspieces (true) or just the ones for pawn promotion (false)
     */
    private void choosePieceForTile(TileView tileView, boolean showAll) {
        // First we try to remove the chess piece on the chosen tile, if there is one
        removePiece(tileView);

        // Create a modal window with the piece picker
        PiecePickerView piecePickerView = new PiecePickerView();
        piecePickerView.initOwner(boardView.getScene().getWindow());

        // Get the current player
        Color currentPlayer = gameController.getGameModel().getCurrentPlayer();
        // Show the dialog for this tile and current player
        Chesspiece pickedPiece = piecePickerView.showPickDialog(tileView, currentPlayer, showAll);

        // Add the pickedPiece from the Picker modal to the board model
        boardModel.addPiece(pickedPiece);
        // Set the picked piece position to this tile
        tileView.getTileModel().setCurrentChessPiece(pickedPiece);
        tileView.rerender();
    }

    /**
     * This method is used to perform a move on the board
     * @param tile The ending tile for the move
     */
    private void makeMove(TileView tile) {
        // First we instantiate a new Move with the current and ending position
        Move move = new Move(gameController.getSelectedPiece().getCurrentPosition(), tile.getTileModel());
        // Then we tell the gameController to perform the move
        gameController.makeMove(move);
        // Then we hide the legal moves of the moving piece
        clearLegalMoves();

        // Then we check if the move is a pawn promoting move
        if (move.isPawnPromoting()) {
            // If it is a pawn promoting move we delete the pawn
            gameController.getGameModel().getBoard().removePiece(gameController.getSelectedPiece());
            // And then we choose a new piece
            choosePieceForTile(tile, false);
        }

        // The we reset the selected piece and switch players
        gameController.setSelectedPiece(null);
        gameController.getGameModel().takeTurn();
    }

    /**
     * This method is used to select a chess piece from a specific tile, if there is one
     * @param tile The tile from which we want to select a piece
     */
    private void selectPiece(TileView tile) {
        // First we get the current chess piece from the tile
        Chesspiece currentChesspiece = tile.getTileModel().getCurrentChessPiece();
        // If there isn't one the do nothing and return
        if (currentChesspiece == null) return;

        // If there is a chess piece we check whether the chess is the same color as the current player
        if (currentChesspiece.getColor().equals(gameController.getGameModel().getCurrentPlayer())) {
            // If it is we generate all the legal moves for that piece and render them
            showLegalMoves(currentChesspiece.getLegalMoves(tile.getTileModel(), boardModel));
            // We also set the chess piece as the selected chess piece
            gameController.setSelectedPiece(currentChesspiece);
        }
    }

    /**
     * This method is used to show al the provided legal moves on the board
     * @param legalMoves The legal moves to show
     */
    private void showLegalMoves(ArrayList<Tile> legalMoves) {
        // We go through all the tiles in the board
        for (Node node : boardView.getChildren()) {
            TileView tileView = (TileView) node;

            // And we set the tile to be a legal move if the tile is in the provided legal moves list
            tileView.setLegalMove(legalMoves.contains(tileView.getTileModel()));
        }
    }

    /**
     * This method is used to hide all the legal moves
     */
    private void clearLegalMoves() {
        // We go throud all the tiles in the board
        for (Node node : boardView.getChildren()) {
            TileView tileView = (TileView) node;

            // And we set the tile to not be a legal move
            tileView.setLegalMove(false);
        }
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
