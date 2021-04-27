package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Bishop extends Chesspiece{
    private String color;

    public Bishop(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();
        String color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        bishopMoves(color, x, y, board, moves);
        return moves;
    }
}
