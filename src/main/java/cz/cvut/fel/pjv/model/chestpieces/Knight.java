package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Knight extends Chesspiece {
    public Knight(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();
        String color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        knightMoves(color, x, y, board, moves);
        return moves;
    }
}
