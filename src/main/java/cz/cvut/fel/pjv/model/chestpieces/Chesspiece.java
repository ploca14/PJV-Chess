package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.controller.Board;

import java.util.ArrayList;

public abstract class Chesspiece {
    private Tile currentPosition;
    private String color;

    public Chesspiece(String color, Tile currentPosition) {
        this.color = color;
    }

    public void move() {
    }

    public abstract ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board);

    public Tile getCurrentPosition() {
        return currentPosition;
    }

    public String getColor() {
        return color;
    }

    public boolean isOccupied(Tile[][]board, int x, int y) {
        if (board[x][y].getCurrentChessPiece() != null) {
            return true;
        }
        return false;
    }

    public boolean isOutOfRange(int x, int y) {
        return (x > 7 || x < 0 || y > 7 || y < 0);
    }

    public boolean isTeammate(Tile[][] b, int x, int y, String color) {
        return b[x][y].getColor().equals(color);
    }
}
