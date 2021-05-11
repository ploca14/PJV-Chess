package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.view.GameView;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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

    /**
     * This method is used to initialize the controller
     */
    private void initController() {
        // First we create all the event listeners
        createEventListeners();

        // Then we initialize the fileChooser component for game saving
        fileChooser.setTitle("Save Game");
        fileChooser.setInitialFileName("Game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Game File", "*.ser"));
    }

    /**
     * This method is used to create all the event listeners
     */
    private void createEventListeners() {
        // When the user clicks on the save button
        gameView.getSaveButton().setOnAction((event) -> {
            // We open the fileChooser save dialog and let the user choose where to save the current game
            Window stage = gameView.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            // Then we save the game to the chosen file
            saveGameToFile(file);
        });

        //When the user clicks the switch sides button
        gameView.getChooseSide().setOnAction((actionEvent -> {
            // We change the button text
            if (gameModel.getCurrentPlayer().equals(Color.WHITE)) {
                ((Button) actionEvent.getSource()).setText("Add white pieces");
            } else {
                ((Button) actionEvent.getSource()).setText("Add black pieces");
            }

            // And then we switch the current player
            gameModel.switchPlayer();
        }));

        // When the user click the start game button
        gameView.getStartGame().setOnAction((event) -> {
            // We set the board to not be editable
            gameModel.getBoard().setEditable(false);
            // And then we recreate the scene and switch the current window to the recreated scene
            ((Stage) gameView.getScene().getWindow()).setScene(gameView.createScene());
        });
    }

    /**
     * This method is used to save the game to a chosen file
     * @param file The file to which the game should be saved
     */
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

    /**
     * This method is used to set the selected chess piece
     * @param currentChesspiece The selected chess piece
     */
    public void setSelectedPiece(Chesspiece currentChesspiece) {
        this.selectedPiece = currentChesspiece;
    }

    public Chesspiece getSelectedPiece() {
        return this.selectedPiece;
    }

    /**
     * This method is used to perform the move on the board and rerender the board
     * @param move The move to be performed
     */
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
}
