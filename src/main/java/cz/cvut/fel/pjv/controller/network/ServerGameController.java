package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.controller.AbstractGameController;
import cz.cvut.fel.pjv.controller.TimerController;
import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.model.network.Lobby;
import cz.cvut.fel.pjv.view.GameView;

import java.util.logging.Logger;

public class ServerGameController extends AbstractGameController {
    private final static Logger logger = Logger.getLogger(ServerGameController.class.getName());
    private ServerBoardController boardController;
    private final Lobby lobby;

    /**
     * @param gameModel The game model this controller will be updating
     * @param lobby The lobby in which this game controller is
     */
    public ServerGameController(Game gameModel, Lobby lobby) {
        super(gameModel, new GameView(gameModel));
        this.lobby = lobby;

        // We create a new client board controller fot this board
        boardController = new ServerBoardController(gameModel.getBoard(), getGameView().getBoardView());
        boardController.setGameController(this);

        // We create new timer controllers
        setWhiteTimerController(new TimerController(getGameModel().getWhiteTimer(), getGameView().getWhiteTimerView(), this));
        setBlackTimerController(new TimerController(getGameModel().getBlackTimer(), getGameView().getBlackTimerView(), this));

        // Then we start the timer controllers on a new thread
        Thread whiteTimerThread = new Thread(getWhiteTimerController());
        whiteTimerThread.start();
        Thread blackTimerThread = new Thread(getBlackTimerController());
        blackTimerThread.start();
    }

    /**
     * This method is used to perform the move on the board
     * @param move The move to be performed
     */
    @Override
    public boolean makeMove(Move move) {
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

        return true;
    }

    /**
     * This method is used to switch the current player, check the game state, and then start the opponents clock
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
    }

    public ServerBoardController getBoardController() {
        return boardController;
    }

    /**
     * This method is used to save the result of the game for the statistics
     * @param message The game result message
     * @param color The winning players color
     */
    @Override
    public void announceWinner(String message, Color color) {
        // We check who won
        int winnerTime;
        String winnerName;
        String loserName;
        if (color.equals(Color.WHITE)) {
             winnerTime = getGameModel().getWhiteTimer().getSecondsPassed();
             winnerName = lobby.getFirstPlayer().getPlayerName();
             loserName = lobby.getSecondPlayer().getPlayerName();
        } else {
            winnerTime = getGameModel().getBlackTimer().getSecondsPassed();
            winnerName = lobby.getSecondPlayer().getPlayerName();
            loserName = lobby.getFirstPlayer().getPlayerName();
        }
        System.out.printf("%d, %s, %s%n", winnerTime, winnerName, loserName);
        logger.info(lobby.getName() + ": Game ended, it took " + winnerTime + "s for " + winnerName + " to win , " + loserName + " lost");
    }
}
