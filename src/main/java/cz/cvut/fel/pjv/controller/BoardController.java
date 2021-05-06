package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Tile;
import cz.cvut.fel.pjv.view.BoardView;
import cz.cvut.fel.pjv.view.TileView;
import javafx.event.EventHandler;
import javafx.scene.Node;
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
        for (Node node : boardView.getChildren()) {
            TileView tileView = (TileView) node;

            EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (tileView.isLegalMove()) {
                        makeMove(tileView);
                    } else {
                        selectPiece(tileView);
                    }
                }
            };

            node.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        }
    }

    private void makeMove(TileView tile) {
        Move move = new Move(gameController.getSelectedPiece().getCurrentPosition(), tile.getTileModel());
        gameController.makeMove(move);
        gameController.setSelectedPiece(null);
        clearLegalMoves();

        gameController.getGameModel().switchPlayer();
    }

    private void selectPiece(TileView tile) {
        Chesspiece currentChesspiece = tile.getTileModel().getCurrentChessPiece();
        if (currentChesspiece == null) return;

        if (currentChesspiece.getColor().equals(gameController.getGameModel().getCurrentPlayer())) {
            showLegalMoves(currentChesspiece.getLegalMoves(tile.getTileModel(), boardModel));
            gameController.setSelectedPiece(currentChesspiece);
        }
    }

    private void showLegalMoves(ArrayList<Tile> legalMoves) {
        for (Node node : boardView.getChildren()) {
            TileView tileView = (TileView) node;

            tileView.setLegalMove(legalMoves.contains(tileView.getTileModel()));
        }
    }

    private void clearLegalMoves() {
        for (Node node : boardView.getChildren()) {
            TileView tileView = (TileView) node;

            tileView.setLegalMove(false);
        }
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
