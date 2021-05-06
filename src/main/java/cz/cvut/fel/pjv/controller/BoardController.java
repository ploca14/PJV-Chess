package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Tile;
import cz.cvut.fel.pjv.view.BoardView;
import cz.cvut.fel.pjv.view.TileView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
                    if(boardModel.isEditable()) {

                        Label testLabel = new Label("MODAL");
                        HBox hBox = new HBox();
                        hBox.getChildren().add(testLabel);
                        Scene scene = new Scene(hBox);
                        Stage newWindow = new Stage();
                        newWindow.setTitle("pick chesspiece");
                        newWindow.setScene(scene);
                        newWindow.initModality(Modality.WINDOW_MODAL);
                        newWindow.initOwner(boardView.getScene().getWindow());
                        newWindow.initStyle(StageStyle.UNDECORATED);
                        newWindow.show();
                    }
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
