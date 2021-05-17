package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.controller.AbstractGameController;
import cz.cvut.fel.pjv.controller.TimerController;
import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.view.GameView;
import cz.cvut.fel.pjv.view.TileView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;
import java.util.logging.Logger;

public class ClientGameController extends AbstractGameController {
    private final static Logger logger = Logger.getLogger(LobbyController.class.getName());
    private final FileChooser fileChooser = new FileChooser();
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ClientBoardController boardController;
    private Color playingAs;

    /**
     * @param gameModel The game model this controller will be updating
     * @param gameView The game view this controller will be rerendering
     * @param playingAs The color that the client player is playing as
     */
    public ClientGameController(Game gameModel, GameView gameView, Color playingAs) {
        super(gameModel, gameView);
        this.playingAs = playingAs;

        // We create a new client board controller fot this board
        boardController = new ClientBoardController(gameModel.getBoard(), gameView.getBoardView());
        boardController.setGameController(this);

        // We create new timer controllers
        setWhiteTimerController(new TimerController(getGameModel().getWhiteTimer(), gameView.getWhiteTimerView(), this));
        setBlackTimerController(new TimerController(getGameModel().getBlackTimer(), gameView.getBlackTimerView(), this));

        // Then we start the timer controllers on a new thread
        Thread whiteTimerThread = new Thread(getWhiteTimerController());
        whiteTimerThread.start();
        Thread blackTimerThread = new Thread(getBlackTimerController());
        blackTimerThread.start();

        // Then we initialize the controller
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

        waitForOpponent();
    }

    /**
     * This method is used to wait for an opponent without blocking the UI thread
     */
    private void waitForOpponent() {
        // We need to wait for the opponent on a new thread so that we don't block the UI thread
        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // We wait for the opponents name
                    String opponentName = (String) objectInputStream.readObject();

                    // Once we get the opponents name we set it to the corresponding text
                    if (playingAs.equals(Color.WHITE)) {
                        getGameView().setBlackName(opponentName);
                    } else {
                        getGameView().setWhiteName(opponentName);
                    }

                    // Then we start the game
                    getGameModel().setStarted(true);

                    // And if it's not the client players turn we wait for the opponents move first
                    if (!playingAs.equals(getGameModel().getCurrentPlayer())) {
                        waitForOpponentsMove();
                    }
                } catch (IOException | ClassNotFoundException exception) {
                    logger.severe("Unable to get opponents name");
                }
            }
        });
        taskThread.start();
    }

    /**
     * This method is used to create all the event listeners
     */
    private void createEventListeners() {
        // When the user clicks on the save button
        getGameView().getSaveButton().setOnAction((event) -> {
            // We open the fileChooser save dialog and let the user choose where to save the current game
            Window stage = getGameView().getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            // Then we save the game to the chosen file
            if (file != null) {
                saveGameToFile(file);
            }
        });
    }

    /**
     * This method is used to send the move to the server and perform the move on the board and rerender the board
     * @param move The move to be performed
     */
    @Override
    public boolean makeMove(Move move) {
        try {
            // First we send the move to the server
            objectOutputStream.writeObject(move);
            // The server then validates it and returns true if the move is valid
            if (objectInputStream.readBoolean()) {
                // If the move is valid we perform the move and rerender the board
                performMoveAndRerender(move);

                // Then we return that the move has been performed successfully
                return true;
            }
        } catch (EOFException exception) {
            logger.severe("The server connection has been closed");
        } catch (IOException exception) {
            logger.severe("Unable to send the move to the server");
        }

        // If The move isn't valid or there has been an error we return that the move has not been performed successfully
        return false;
    }

    /**
     * This method is used to switch the current player, check the game state, start the opponents clock and then wait for the opponents move
     */
    @Override
    public void takeTurn() {
        // First we switch the players
        getGameModel().takeTurn();

        // Then we check the game state
        checkGameState();

        // If the game is finished we do nothing and return
        if (getGameModel().getFinished()) return;

        // Then we start the opponents clock
        startClock();

        // And if the current player isn't the client player
        if (!getGameModel().getCurrentPlayer().equals(playingAs))
        {
            // We wait for the opponents move
            waitForOpponentsMove();
        }
    }

    /**
     * This method is used to wait for the opponents move on a new thread and then updating the local game state
     */
    private void waitForOpponentsMove() {
        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // First we wait for the opponents move
                    // We use the parseMove method to translate the move to the local board (because the serialized move has different pointers)
                    Move move = NetworkUtilities.parseMove(objectInputStream.readObject(), getGameModel().getBoard());

                    // Then we perform the translated move and rerender the board
                    performMoveAndRerender(move);

                    // If the move is special we perform additional steps
                    if (move.isSpecial()) {
                        // Is the move is pawn promoting
                        if (move.isPawnPromoting()) {
                            // We wait for the selected piece
                            Chesspiece chesspiece = (Chesspiece) objectInputStream.readObject();

                            TileView tileView = getGameView().getBoardView().getNodeByRowColumnIndex(move.getEndingPosition().getY(), move.getEndingPosition().getX());

                            // Then we set the selected piece to the ending position tile of the move
                            boardController.setChosenChesspieceAndRerender(tileView, chesspiece);
                        } else {
                            boardController.checkSpecialMoves(move);
                        }
                    }

                    // Lastly we take turns
                    takeTurn();
                } catch (IOException | ClassNotFoundException exception) {
                    // If the communication fails, we end the game
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            new Alert(Alert.AlertType.INFORMATION, "Game ended").showAndWait();
                            getGameModel().setFinished(true);
                            getGameView().getSaveButton().setDisable(true);
                            getWhiteTimerController().stopThread();
                            getBlackTimerController().stopThread();
                        }
                    });
                }
            }
        });
        taskThread.start();
    }

    /**
     * @param chosenPiece The chosen piece we want to send to the server
     * @return if the promotion was successful
     */
    public boolean sendChosenChessPiece(Chesspiece chosenPiece) {
        try {
            // First we send the chosen piece to the server
            objectOutputStream.writeObject(chosenPiece);
            objectOutputStream.flush();
            // The server then returns whether the promotion was successful
            return objectInputStream.readBoolean();
        } catch (EOFException exception) {
            logger.severe("The server connection has been closed");
        } catch (IOException exception) {
            logger.severe("Unable to send the move to the server");
        }
        // If there happens to be an error we return that the promotion was unsuccessful
        return false;
    }

    public void setInputStream(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    public void setOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    public Color getPlayingAs() {
        return playingAs;
    }
}
