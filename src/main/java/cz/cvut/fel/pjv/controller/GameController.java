package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.ChessPieceFactory;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.model.player.Ai;
import cz.cvut.fel.pjv.view.GameView;
import cz.cvut.fel.pjv.view.TileView;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;

public class GameController extends AbstractGameController {
    private final FileChooser fileChooser = new FileChooser();
    private BoardController boardController;
    private Ai ai;

    /**
     * @param gameModel The game model this controller will be updating
     * @param gameView The game view this controller will be rerendering
     */
    public GameController(Game gameModel, GameView gameView) {
        super(gameModel, gameView);

        // We create a new client board controller fot this board
        boardController = new BoardController(gameModel.getBoard(), gameView.getBoardView());
        boardController.setGameController(this);

        if (getGameModel().isVersusAi()) {
            ai = new Ai(getGameModel().getRules());
        } else {
            // We create new timer controllers
            setWhiteTimerController(new TimerController(getGameModel().getWhiteTimer(), gameView.getWhiteTimerView(), this));
            setBlackTimerController(new TimerController(getGameModel().getBlackTimer(), gameView.getBlackTimerView(), this));

            // Then we start the timer controllers on a new thread
            Thread whiteTimerThread = new Thread(getWhiteTimerController());
            whiteTimerThread.start();
            Thread blackTimerThread = new Thread(getBlackTimerController());
            blackTimerThread.start();
        }

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

        // Start the clock
        if (!(getGameModel().getBoard().isEditable() || getGameModel().isVersusAi())) {
            startClock();
        }
    }

    /**
     * This method is used to create all the event listeners
     */
    private void createEventListeners() {
        // When the user clicks on the save button
        getGameView().getSaveButton().setOnAction((event) -> {
            // Stop the timers
            if (!getGameModel().isVersusAi()) {
                getWhiteTimerController().getTimer().setRunning(false);
                getBlackTimerController().getTimer().setRunning(false);
            }

            // We open the fileChooser save dialog and let the user choose where to save the current game
            Window stage = getGameView().getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            // Then we save the game to the chosen file
            if (file != null) {
                saveGameToFile(file);
            }
        });

        //When the user clicks the switch sides button
        getGameView().getChooseSide().setOnAction((actionEvent -> {
            // We change the button text
            if (getGameModel().getCurrentPlayer().equals(Color.WHITE)) {
                getGameView().getCurrentlyEditing().setText("Editing black pieces, black will start");
            } else {
                getGameView().getCurrentlyEditing().setText("Editing white pieces, white will start");
            }

            // And then we switch the current player
            getGameModel().switchPlayer();
        }));

        // When the user click the start game button
        getGameView().getStartGame().setOnAction((event) -> {
            // First we check if there are two kings on the board, if not we show an error and return
            if (getGameModel().getBoard().getNumberOfKings() < 2) {
                new Alert(Alert.AlertType.ERROR, "Game cannot be started, there must be two kings on the board").show();
                return;
            }

            // Then we check if the game isn!t already over
            if (getGameModel().getRules().isEndgame(getGameModel().getCurrentPlayer(), getGameModel().getBoard())) {
                new Alert(Alert.AlertType.ERROR, "Game cannot be started, it's already over").show();
                return;
            }

            // We set the board to not be editable
            getGameModel().getBoard().setEditable(false);
            // Then we recreate the scene and switch the current window to the recreated scene
            ((Stage) getGameView().getScene().getWindow()).setScene(getGameView().createScene());
            // Then we start the clock
            startClock();
        });
    }

    /**
     * This method is used to perform the move on the board and rerender the board
     * @param move The move to be performed
     */
    public boolean makeMove(Move move) {
        performMoveAndRerender(move);

        return true;
    }

    /**
     * This method is used to switch the current player, check the game state, start the opponents clock or perform an ai move
     */
    @Override
    public void takeTurn() {
        // First we switch the players
        getGameModel().takeTurn();

        // Then we check the game state
        checkGameState();

        // If the game is finished we do nothing and return
        if (getGameModel().getFinished()) return;

        // If the game is not finished we start the clock for the next player or make a random ai move if the game is against the ai
        if (getGameModel().isVersusAi()) {
            Move move = ai.chooseRandomMove(getGameModel().getCurrentPlayer(), getGameModel().getBoard());
            boardController.makeAiMove(move);
            getGameModel().takeTurn();
            checkGameState();
        } else {
            startClock();
        }
    }

    /**
     * @param forTileView the tile for which we want to select a random piece
     * @param color the color of the randomly selected piece
     * @param chessPieceFactory a chesspiece factory, so that we can't choose too many pieces
     * @return the randomly chosen piece
     */
    public Chesspiece getRandomPiece(TileView forTileView, Color color, ChessPieceFactory chessPieceFactory) {
        return ai.chooseRandomPiece(forTileView.getTileModel(), color, chessPieceFactory);
    }

    public BoardController getBoardController() {
        return boardController;
    }
}
