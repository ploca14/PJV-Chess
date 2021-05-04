package cz.cvut.fel.pjv.model.chestpieces;

public class Tile {
    private final String color;
    private final int x;
    private final int y;
    public Chesspiece currentChessPiece;

    /**
     * ctor of Tile, set color and coordinates
     * @param color color of Tile
     * @param x x coordinate of Tile
     * @param y y coordinate of Tile
     */
    public Tile(int color, int x, int y) {
        if(color == 1) {
            this.color = "Black";
        } else { this.color = "White"; }

        this.x = x;
        this.y = y;
    }

    public String getColor() {
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
