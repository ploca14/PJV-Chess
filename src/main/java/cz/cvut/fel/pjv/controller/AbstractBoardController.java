package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.ChessPieceFactory;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.model.chestpieces.Tile;
import cz.cvut.fel.pjv.view.BoardView;
import cz.cvut.fel.pjv.view.PiecePickerView;
import cz.cvut.fel.pjv.view.TileView;
import javafx.scene.Node;
import javafx.scene.control.Alert;

import java.util.ArrayList;

public abstract class AbstractBoardController {
    private final Board boardModel;
    private final BoardView boardView;
    private final ChessPieceFactory chessPieceFactory;
    private Chesspiece selectedPiece;
    private AbstractGameController gameController;

    /**
     * @param boardModel the board model that this controller will be updating
     * @param boardView the board view that this controller will be rerendering
     */
    public AbstractBoardController(Board boardModel, BoardView boardView) {
        this.boardModel = boardModel;
        this.boardView = boardView;
        this.chessPieceFactory = new ChessPieceFactory(boardModel);
    }

    /**
     * This method is used to remove a chess piece from a specific tile and then rerender the tile
     * @param tileView The tile where we want to remove a chesspiece
     */
    public void removePiece(TileView tileView) {
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
    public void choosePieceForTile(TileView tileView, boolean showAll) {
        // Create a modal window with the piece picker
        PiecePickerView piecePickerView = new PiecePickerView();
        piecePickerView.initOwner(getBoardView().getScene().getWindow());
        piecePickerView.setChessPieceFactory(getChessPieceFactory());

        // Get the current player
        Color currentPlayer = getGameController().getGameModel().getCurrentPlayer();
        // Show the dialog for this tile and current player
        Chesspiece chosenPiece = piecePickerView.showPickDialog(tileView, currentPlayer, showAll);

        // If the chosen piece isn't null, we set the tile's current chess piece to the choden piece
        if (chosenPiece != null) {
            setChosenChesspieceAndRerender(tileView, chosenPiece);
        } else {
            new Alert(Alert.AlertType.ERROR, "You can't add any more of these pieces").show();
        }
    }

    /**
     * This method is used to add the chess piece to the board model and set it to the selected tile
     * @param forTile the tile that the chesspiece should be set to
     * @param chosenPiece the chesspiece to set
     */
    public void setChosenChesspiece(Tile forTile, Chesspiece chosenPiece) {
        boardModel.addPiece(chosenPiece);

        // Set the picked piece position to this tile
        forTile.setCurrentChessPiece(chosenPiece);
    }

    public void setChosenChesspieceAndRerender(TileView forTileView, Chesspiece chosenPiece) {
        // First we try to remove the chess piece on the chosen tile, if there is one
        removePiece(forTileView);

        setChosenChesspiece(forTileView.getTileModel(), chosenPiece);

        forTileView.rerender();
    }

    /**
     * This method is used to perform a move on the board
     * @param tile The ending tile for the move
     */
    public void makeMove(TileView tile) {
        // First we instantiate a new Move with the current and ending position
        Move move = new Move(selectedPiece.getCurrentPosition(), tile.getTileModel());
        // Then we tell the gameController to perform the move
        if (gameController.makeMove(move)) {
            // Then we hide the legal moves of the moving piece
            clearLegalMoves();

            checkSpecialMoves(move);

            // The we reset the selected piece and switch players
            selectedPiece = null;
            gameController.takeTurn();
        }
    }

    public void checkSpecialMoves(Move move) {
        // Then we check if the move is a pawn promoting move
        if (move.isPawnPromoting()) {
            // If it is a pawn promoting move we delete the pawn
            boardModel.removePiece(selectedPiece);
            // And then we choose a new piece
            TileView tileToChoose = boardView.getNodeByRowColumnIndex(move.getEndingPosition().getY(), move.getEndingPosition().getX());
            choosePieceForTile(tileToChoose, false);
        } else if(move.isShortRosada()) {
            Tile startingTile = boardModel.getBoard()[move.getEndingPosition().getY()][move.getEndingPosition().getX()+1];
            Tile endingTile = boardModel.getBoard()[move.getEndingPosition().getY()][move.getEndingPosition().getX()-1];
            Move shortRosadaMove = new Move(startingTile, endingTile);
            gameController.performMoveAndRerender(shortRosadaMove);
        } else if(move.isLongRosada()) {
            Tile startingTile = boardModel.getBoard()[move.getEndingPosition().getY()][move.getEndingPosition().getX()-2];
            Tile endingTile = boardModel.getBoard()[move.getEndingPosition().getY()][move.getEndingPosition().getX()+1];
            Move longRosadaMove = new Move(startingTile, endingTile);
            gameController.performMoveAndRerender(longRosadaMove);
        } else if(move.isEnPassant()) {
            TileView tileToRemove = boardView.getNodeByRowColumnIndex(move.getStartingPosition().getY(), move.getEndingPosition().getX());
            removePiece(tileToRemove);
        }
    }

    /**
     * This method is used to select a chess piece from a specific tile, if there is one
     * @param tile The tile from which we want to select a piece
     */
    public void selectPiece(TileView tile) {
        // First we get the current chess piece from the tile
        Chesspiece currentChesspiece = tile.getTileModel().getCurrentChessPiece();
        // If there isn't one the do nothing and return
        if (currentChesspiece == null) return;

        // If there is a chess piece we check whether the chess is the same color as the current player
        if (currentChesspiece.getColor().equals(getGameController().getGameModel().getCurrentPlayer())) {
            showLegalMoves(getGameController().getGameModel().getRules().getLegalNotCheckMoves(currentChesspiece));
            setSelectedPiece(currentChesspiece);
        }
    }

    /**
     * This method is used to show al the provided legal moves on the board
     * @param legalMoves The legal moves to show
     */
    public void showLegalMoves(ArrayList<Tile> legalMoves) {
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
    public void clearLegalMoves() {
        // We go throud all the tiles in the board
        for (Node node : boardView.getChildren()) {
            TileView tileView = (TileView) node;

            // And we set the tile to not be a legal move
            tileView.setLegalMove(false);
        }
    }

    public ChessPieceFactory getChessPieceFactory() {
        return chessPieceFactory;
    }

    public Board getBoardModel() {
        return boardModel;
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public Chesspiece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Chesspiece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public void setGameController(AbstractGameController gameController) {
        this.gameController = gameController;
    }

    public AbstractGameController getGameController() {
        return gameController;
    }
}
