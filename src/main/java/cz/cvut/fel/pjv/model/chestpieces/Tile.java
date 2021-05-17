package cz.cvut.fel.pjv.model.chestpieces;

import java.io.Serializable;
import java.util.Objects;

public class Tile implements Serializable {
    private final Color color;
    private final int x;
    private final int y;
    public Chesspiece currentChessPiece;

    /**
     * ctor of Tile, set color and coordinates
     * @param x x coordinate of Tile
     * @param y y coordinate of Tile
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        color = ((x + y) % 2) == 0 ? Color.LIGHT : Color.DARK;
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
        if (currentChessPiece != null) {
            currentChessPiece.setCurrentPosition(this);
        }
    }

    public void movePiece(Chesspiece chesspiece) {
        // If the current tile is occupied then clear the current pieces position
        if (this.currentChessPiece != null) {
            this.currentChessPiece.setCurrentPosition(null);
        }

        // Overwrite the current piece with the moving piece
        this.currentChessPiece = chesspiece;
        // Set the position of the moving piece to this tile
        currentChessPiece.move(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return x == tile.x && y == tile.y && color == tile.color && Objects.equals(currentChessPiece, tile.currentChessPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, x, y, currentChessPiece);
    }
}
