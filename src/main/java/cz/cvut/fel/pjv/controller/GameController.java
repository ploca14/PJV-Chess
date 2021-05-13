package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.view.GameView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;

public class GameController {
    private final Game gameModel;
    private final GameView gameView;
    private final BoardController boardController;
    private final TimerController whiteTimerController;
    private final TimerController blackTimerController;
    private final FileChooser fileChooser = new FileChooser();
    private Chesspiece selectedPiece;

    public GameController(Game gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;

        boardController = new BoardController(gameModel.getBoard(), gameView.getBoardView());
        boardController.setGameController(this);

        whiteTimerController = new TimerController(gameModel.getWhiteTimer(), gameView.getWhiteTimerView(), this);
        blackTimerController = new TimerController(gameModel.getBlackTimer(), gameView.getBlackTimerView(), this);

        Thread whiteTimerThread = new Thread(whiteTimerController);
        whiteTimerThread.start();
        Thread blackTimerThread = new Thread(blackTimerController);
        blackTimerThread.start();

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

        // Start the clock
        startClock();
    }

    /**
     * This method is used to create all the event listeners
     */
    private void createEventListeners() {
        // When the user clicks on the save button
        gameView.getSaveButton().setOnAction((event) -> {
            // Stop the timers
            whiteTimerController.getTimer().setRunning(false);
            blackTimerController.getTimer().setRunning(false);

            // We open the fileChooser save dialog and let the user choose where to save the current game
            Window stage = gameView.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            // Then we save the game to the chosen file
            if (file != null) {
                saveGameToFile(file);
            }
        });

        //When the user clicks the switch sides button
        gameView.getChooseSide().setOnAction((actionEvent -> {
            // We change the button text
            if (gameModel.getCurrentPlayer().equals(Color.WHITE)) {
                gameView.getCurrentlyEditing().setText("Editing black pieces, black will start");
            } else {
                gameView.getCurrentlyEditing().setText("Editing white pieces, white will start");
            }

            // And then we switch the current player
            gameModel.switchPlayer();
        }));

        // When the user click the start game button
        gameView.getStartGame().setOnAction((event) -> {
            // First we check if there are two kings on the board, if not we show an error and return
            if (gameModel.getBoard().getNumberOfKings() < 2) {
                new Alert(Alert.AlertType.ERROR, "Game cannot be started, there must be two kings on the board").show();
                return;
            }

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

    public void takeTurn() {
        gameModel.takeTurn();
        startClock();
    }

    private void startClock() {
        if (gameModel.getCurrentPlayer().equals(Color.WHITE)) {
            whiteTimerController.start();
            blackTimerController.pause();
        } else {
            whiteTimerController.pause();
            blackTimerController.start();
        }
    }

    public void playerRunOutOfTime() {
        gameModel.setFinished(true);

        if (gameModel.getCurrentPlayer().equals(Color.WHITE)) {
            announceWinner("Black wins! White has run out of time!");
        } else {
            announceWinner("White wins! Black has run out of time!");
        }
    }

    private void announceWinner(String message) {
        Platform.runLater(() -> {
            new Alert(Alert.AlertType.INFORMATION, message).show();
        });
    }

    public void playerWon() {
        gameModel.setFinished(true);
        if (gameModel.getCurrentPlayer().equals(Color.WHITE)) {
            announceWinner("Black wins!");
        } else {
            announceWinner("White wins!");
        }
    }

    public void playerDraw() {
        announceWinner("draw");
    }
}
