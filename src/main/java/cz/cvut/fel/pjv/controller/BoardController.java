package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Board;
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
                    System.out.println("Y: " + tileView.getTileModel().getY() + " X: " + tileView.getTileModel().getX());
                    Chesspiece currentChesspiece = tileView.getTileModel().getCurrentChessPiece();
                    if (currentChesspiece != null) {
                        Tile tileModel = tileView.getTileModel();
                        Tile[][] tiles = boardModel.getBoard();
                        showLegalMoves(currentChesspiece.getLegalMoves(tileView.getTileModel(), boardModel.getBoard()));
                    }
                }
            };

            node.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        }
    }

    private void showLegalMoves(ArrayList<Tile> legalMoves) {
        for (Node node : boardView.getChildren()) {
            TileView tileView = (TileView) node;

            tileView.showLegalMode(legalMoves.contains(tileView.getTileModel()));
        }
    }
}
