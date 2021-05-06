package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.King;
import cz.cvut.fel.pjv.model.chestpieces.Rook;
import cz.cvut.fel.pjv.view.GameView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;

public class GameController {
    private final Game gameModel;
    private final GameView gameView;
    private final BoardController boardController;
    private final FileChooser fileChooser = new FileChooser();
    private Chesspiece selectedPiece;

    public GameController(Game gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;

        boardController = new BoardController(gameModel.getBoard(), gameView.getBoardView());
        boardController.setGameController(this);

        initController();
    }

    private void initController() {
        createEventListeners();

        fileChooser.setTitle("Save Game");
        fileChooser.setInitialFileName("Game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Game File", "*.ser"));
    }

    private void createEventListeners() {
        gameView.getSaveButton().setOnAction((event) -> {
            Window stage = gameView.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            saveGameToFile(file);
        });
    }

    private void saveGameToFile(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(gameModel);
            objectOutputStream.close();
        } catch (IOException ioException) {
            // TODO: Logging
            ioException.printStackTrace();
        }
    }

    public Game getGameModel() {
        return gameModel;
    }

    public GameView getGameView() {
        return gameView;
    }

    public void setSelectedPiece(Chesspiece currentChesspiece) {
        this.selectedPiece = currentChesspiece;
    }

    public Chesspiece getSelectedPiece() {
        return this.selectedPiece;
    }

    public void makeMove(Move move) {
        // Remove the current piece from the starting position
        Chesspiece startingPosition = move.getStartingPosition().getCurrentChessPiece();
        move.getStartingPosition().setCurrentChessPiece(null);

        // Check if the ending position is occupied
        Chesspiece endingPositionChesspiece = move.getEndingPosition().getCurrentChessPiece();

        if (endingPositionChesspiece != null) {
            // If it is occupied then remove the occupying piece
            gameModel.getBoard().removePiece(endingPositionChesspiece);
        }

        // Move the current piece to the ending position and rerender the board
        move.getEndingPosition().movePiece(move.getChesspiece());
        gameView.getBoardView().rerenderBoard();
    }

    public void makeRosadaMove(Move move) {

    }
}
