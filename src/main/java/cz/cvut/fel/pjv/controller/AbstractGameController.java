package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.ChessPieceFactory;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.view.GameView;
import cz.cvut.fel.pjv.view.TileView;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;

public abstract class AbstractGameController {
    private final Game gameModel;
    private final GameView gameView;
    private TimerController whiteTimerController;
    private TimerController blackTimerController;

    /**
     * @param gameModel The game model this controller will be updating
     * @param gameView The game view this controller will be rerendering
     */
    public AbstractGameController(Game gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    /**
     * This method is used to save the game to a chosen file
     * @param file The file to which the game should be saved
     */
    public void saveGameToFile(File file) {
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

    public void performMoveAndRerender(Move move) {
        // Remove the current piece from the starting position
        move.getStartingPosition().setCurrentChessPiece(null);

        // Check if the ending position is occupied
        Chesspiece endingPositionChesspiece = move.getEndingPosition().getCurrentChessPiece();

        if (endingPositionChesspiece != null) {
            // If it is occupied then remove the occupying piece
            getGameModel().getBoard().removePiece(endingPositionChesspiece);
        }

        // Move the current piece to the ending position and rerender the board
        move.getEndingPosition().movePiece(move.getChesspiece());
        getGameView().getBoardView().rerenderBoard();
    }

    /**
     * This method is used to perform the move on the board and rerender the board
     * @param move The move to be performed
     */
    public abstract boolean makeMove(Move move);

    /**
     * This method is used to switch the current player, check the game state and then start the opponents clock
     */
    public abstract void takeTurn();

    /**
     * This methods is used to check whether the game is finished
     */
    public void checkGameState() {
        // First we check if the current player doesn't have any more legal moves to play
        if(gameModel.getRules().isEndgame(gameModel.getCurrentPlayer(), gameModel.getBoard())) {
            // If the doesn't have any more legal moves to play we check if he is in check
            if(gameModel.getRules().isCheck(gameModel.getCurrentPlayer(), gameModel.getBoard())) {
                // If he is in check we announce that the opponent won
                playerWon();
            } else {
                // If he is not in check then we announce that its a draw
                playerDraw();
            }
        }
    }

    /**
     * This method is used to start the current players timer and pause the opponents clock
     */
    public void startClock() {
        if (gameModel.getCurrentPlayer().equals(Color.WHITE)) {
            whiteTimerController.start();
            blackTimerController.pause();
        } else {
            whiteTimerController.pause();
            blackTimerController.start();
        }
    }


    /**
     * If the timer reaches 0 we end the game and announce that the opponent player won
     */
    public void playerRunOutOfTime() {
        gameModel.setFinished(true);

        if (gameModel.getCurrentPlayer().equals(Color.WHITE)) {
            announceWinner("Black wins! White has run out of time!", Color.BLACK);
        } else {
            announceWinner("White wins! Black has run out of time!", Color.WHITE);
        }
    }

    /**
     * This method is used to announce the winner
     * @param message The game result message
     * @param color The color of the winning player
     */
    public void announceWinner(String message, Color color) {
        // Show alert with winner
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
                alert.show();
            }
        });

        gameView.getSaveButton().setDisable(true);
    }

    /**
     * This method is used to end the game and announce the winner
     */
    public void playerWon() {
        gameModel.setFinished(true);
        if (gameModel.getCurrentPlayer().equals(Color.WHITE)) {
            announceWinner("Black wins!", Color.BLACK);
        } else {
            announceWinner("White wins!", Color.WHITE);
        }
    }

    /**
     * This method is used to end the game and announce that its a draw
     */
    public void playerDraw() {
        gameModel.setFinished(true);
        announceWinner("It's a draw!", null);
    }

    public TimerController getWhiteTimerController() {
        return whiteTimerController;
    }

    public TimerController getBlackTimerController() {
        return blackTimerController;
    }

    /**
     * This method is used to validate a received move
     * @param move The move to validate
     * @return Whether the move is valid
     */
    public boolean validateMove(Move move) {
        // If the move moves a chess piece of the current player
        if (move.getChesspiece().getColor().equals(gameModel.getCurrentPlayer())) {
            // Then we check if the move is a legal move of that chesspiece
            return gameModel.getRules().getLegalNotCheckMoves(move.getChesspiece()).contains(move.getEndingPosition());
        } else {
            // Otherwise we return false
            return false;
        }
    }

    public void setWhiteTimerController(TimerController whiteTimerController) {
        this.whiteTimerController = whiteTimerController;
    }

    public void setBlackTimerController(TimerController blackTimerController) {
        this.blackTimerController = blackTimerController;
    }
}
