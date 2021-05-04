package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Rook extends Chesspiece{

    public Rook(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public void move() {
        super.move();
    }

    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();
        String color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        rookMoves(color, x, y, board, moves);
        return moves;
    }
}
