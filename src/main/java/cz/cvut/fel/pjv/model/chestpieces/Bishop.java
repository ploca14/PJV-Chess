package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Bishop extends Chesspiece{

    public Bishop(Color color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();
        Color color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        bishopMoves(color, x, y, board, moves);
        return moves;
    }

    @Override
    public String toString() {
        return "\u265D";
    }
}
