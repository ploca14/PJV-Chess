package cz.cvut.fel.pjv.model;

import cz.cvut.fel.pjv.controller.GameRules;
import cz.cvut.fel.pjv.model.chestpieces.Color;

import java.io.Serializable;

public class Game implements Serializable {
    private final Board board = new Board(this);
    private final GameRules rules = new GameRules(board);
    private Color currentPlayer = Color.WHITE;
    private Integer turnCount = 0;

    public Board getBoard() {
        return board;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
        turnCount++;
    }

    public GameRules getRules() {
        return rules;
    }

    public Integer getTurnCount() {
        return turnCount;
    }
}
