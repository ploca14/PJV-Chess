package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.controller.AbstractBoardController;
import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.view.BoardView;
import cz.cvut.fel.pjv.view.TileView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class ClientBoardController extends AbstractBoardController {
    /**
     * @param boardModel the board model that this controller will be updating
     * @param boardView the board view that this controller will be rerendering
     */
    public ClientBoardController(Board boardModel, BoardView boardView) {
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
            EventHandler<MouseEvent> eventHandler = new EventHandler<>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    handleTurn();
                }

                private void handleTurn() {
                    // If the game has already finished, do nothing and return
                    if (getGameController().getGameModel().getFinished()) return;
                    // If the game hasn't started yet, do nothing and return
                    if (!getGameController().getGameModel().getStarted()) return;

                    // check if this tile is a legal move of the currenttly selected piece
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

    /**
     * This method is used to select a chess piece from a specific tile, if there is one
     * @param tile The tile from which we want to select a piece
     */
    @Override
    public void selectPiece(TileView tile) {
        // First we get the current chess piece from the tile
        Chesspiece currentChesspiece = tile.getTileModel().getCurrentChessPiece();
        // If there isn't one the do nothing and return
        if (currentChesspiece == null) return;

        // If there is a chess piece we check whether the chess piece is the same color as the current player
        if (currentChesspiece.getColor().equals(getGameController().getGameModel().getCurrentPlayer())) {
            // And if the chess piece is the same color as the playing as color
            if (currentChesspiece.getColor().equals(getClientGameController().getPlayingAs())) {
                showLegalMoves(getGameController().getGameModel().getRules().getLegalNotCheckMoves(currentChesspiece));
                setSelectedPiece(currentChesspiece);
            }
        }
    }

    private ClientGameController getClientGameController() {
        return (ClientGameController) getGameController();
    }

    /**
     * This method is used to send the chosen chesspiece to the server and then set it to the tile and rerender it
     * @param forTileView The tile view for which we want to set a chesspiece and rerender it
     * @param chosenPiece The chess piece we want to set to the tile
     */
    @Override
    public void setChosenChesspieceAndRerender(TileView forTileView, Chesspiece chosenPiece) {
        // First we check if the local player is playing
        if (getGameController().getGameModel().getCurrentPlayer().equals(getClientGameController().getPlayingAs())) {
            // If yes, we send the chosen piece to the server, and we check if the server responds with true
            if (getClientGameController().sendChosenChessPiece(chosenPiece)) {
                // Then we ser the chosen piece to the tile and rerender
                performSetChosenPieceAndRerender(forTileView, chosenPiece);
            }
        } else {
            // Then we set the chosen piece to the tile and rerender
            performSetChosenPieceAndRerender(forTileView, chosenPiece);
        }
    }

    /**
     * This method is used to set a chesspiece to a tile and rerender it
     * @param forTileView The tile view for which we want to set a chesspiece and rerender it
     * @param chosenPiece The chess piece we want to set to the tile
     */
    private void performSetChosenPieceAndRerender(TileView forTileView, Chesspiece chosenPiece) {
        // Then we try to remove the chess piece on the chosen tile, if there is one
        removePiece(forTileView);

        // Then we update the tile model
        setChosenChesspiece(forTileView.getTileModel(), chosenPiece);

        // And lastly we rerender the tile
        forTileView.rerender();
    }
}
