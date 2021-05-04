package cz.cvut.fel.pjv.model;

import cz.cvut.fel.pjv.controller.GameRules;
import javafx.scene.paint.Color;

public class Game {
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
}
