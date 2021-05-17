package cz.cvut.fel.pjv.model;

import cz.cvut.fel.pjv.controller.GameRules;
import cz.cvut.fel.pjv.model.chestpieces.Color;

import java.io.Serializable;

public class Game implements Serializable {
    private final Board board = new Board(this);
    private final GameRules rules = new GameRules(board);
    private Color currentPlayer = Color.WHITE;
    private Integer turnCount = 0;
    private final Timer whiteTimer = new Timer();
    private final Timer blackTimer = new Timer();
    private boolean finished = false;
    private boolean versusAi = false;
    private boolean started = false;

    public Board getBoard() {
        return board;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * This method is used to switch the current player
     */
    public void switchPlayer() {
        currentPlayer = currentPlayer.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    /**
     * This method switches the current player and increases the turn count
     */
    public void takeTurn() {
        switchPlayer();
        turnCount++;
    }

    public GameRules getRules() {
        return rules;
    }

    public Integer getTurnCount() {
        return turnCount;
    }

    public Timer getWhiteTimer() {
        return whiteTimer;
    }

    public Timer getBlackTimer() {
        return blackTimer;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean getFinished() {
        return finished;
    }

    public boolean isVersusAi() {
        return versusAi;
    }

    public void setVersusAi(boolean versusAi) {
        this.versusAi = versusAi;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean getStarted() {
        return started;
    }
}
