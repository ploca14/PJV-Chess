package cz.cvut.fel.pjv.model;

import cz.cvut.fel.pjv.controller.GameRules;
import cz.cvut.fel.pjv.model.chestpieces.Color;

import java.io.Serializable;

public class Game implements Serializable {
    private final Board board = new Board();
    private final GameRules rules = new GameRules(board);
    private Color currentPlayer = Color.WHITE;

    public Board getBoard() {
        return board;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public GameRules getRules() {
        return rules;
    }
}
