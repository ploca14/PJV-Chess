package cz.cvut.fel.pjv.model.chestpieces;

import javafx.scene.paint.Color;

public class Tile {
    private final Color color;
    private final int x;
    private final int y;
    public Chesspiece currentChessPiece;

    /**
     * ctor of Tile, set color and coordinates
     * @param color color of Tile
     * @param x x coordinate of Tile
     * @param y y coordinate of Tile
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        color = ((x + y) % 2) == 0 ? Color.BEIGE : Color.TAN;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Chesspiece getCurrentChessPiece() {
        return currentChessPiece;
    }

    public void setCurrentChessPiece(Chesspiece currentChessPiece) {
        this.currentChessPiece = currentChessPiece;
    }

}
