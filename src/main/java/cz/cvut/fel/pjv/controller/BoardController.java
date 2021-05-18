package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.controller.network.ClientGameController;
import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.*;
import cz.cvut.fel.pjv.view.BoardView;
import cz.cvut.fel.pjv.view.PiecePickerView;
import cz.cvut.fel.pjv.view.TileView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class BoardController extends AbstractBoardController {
    /**
     @param boardModel the board model that this controller will be updating
      * @param boardView the board view that this controller will be rerendering
     */
    public BoardController(Board boardModel, BoardView boardView) {
        super(boardModel, boardView);

        initController();
    }

    private void initController() {
        createEventListeners();
    }

    private void createEventListeners() {
        // Create event listeners for all the tiles in the boardView
        for (Node node : getBoardView().getChildren()) {
            TileView tileView = (TileView) node;

            // New mouse click handler for this tile
            EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // Check if the board is editable
                    if(getBoardModel().isEditable()) {
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
                        // first we check if we didnt reach the maximum amount of chess pieces, if we did, we do nothing and return
                        Game gameModel = getGameController().getGameModel();
                        if (gameModel.getCurrentPlayer().equals(Color.WHITE) && gameModel.getBoard().getWhitePieces().size() >= 16) return;
                        if (gameModel.getCurrentPlayer().equals(Color.BLACK) && gameModel.getBoard().getBlackPieces().size() >= 16) return;
                        // if we didnt we choose a piece for this tile
                        choosePieceForTile(tileView, true);
                    }
                }

                private void handleTurn() {
                    // If the game has already finished, do nothing and return
                    if (getGameController().getGameModel().getFinished()) return;

                    // If its not editable check if this tile is a legal move of the currenttly selected piece
                    if (getSelectedPiece() != null && tileView.isLegalMove()) {
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

    private GameController getActualGameController() {
        return (GameController) getGameController();
    }

    /**
     * @param tileView The the for which we want to choose a new piece
     * @param showAll Whetheer we want to show all the chesspieces (true) or just the ones for pawn promotion (false)
     */
    @Override
    public void choosePieceForTile(TileView tileView, boolean showAll) {
        // Create a modal window with the piece picker
        PiecePickerView piecePickerView = new PiecePickerView();
        piecePickerView.initOwner(getBoardView().getScene().getWindow());
        piecePickerView.setChessPieceFactory(getChessPieceFactory());

        // Get the current player
        Color currentPlayer = getGameController().getGameModel().getCurrentPlayer();
        // Show the dialog for this tile and current player
        Chesspiece chosenPiece;
        if (getGameController().getGameModel().isVersusAi() && getGameController().getGameModel().getCurrentPlayer().equals(Color.BLACK)) {
            chosenPiece = getActualGameController().getRandomPiece(tileView, currentPlayer, getChessPieceFactory());
        } else {
            chosenPiece = piecePickerView.showPickDialog(tileView, currentPlayer, showAll);
        }

        // If the chosen piece isn't null, we set the tile's current chess piece to the choden piece
        if (chosenPiece != null) {
            setChosenChesspieceAndRerender(tileView, chosenPiece);
        } else {
            new Alert(Alert.AlertType.ERROR, "You can't add any more of these pieces").show();
        }
    }

    /**
     * This method is used to perform an AI move
     * @param move The move to make
     */
    public void makeAiMove(Move move) {
        // Set the random move chess piece as selected
        setSelectedPiece(move.getChesspiece());

        // We tell the gameController to perform the move
        getGameController().makeMove(move);

        checkSpecialMoves(move);

        // The we reset the selected piece
        setSelectedPiece(null);
    }
}
