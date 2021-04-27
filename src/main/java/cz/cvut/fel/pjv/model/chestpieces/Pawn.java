package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Pawn extends Chesspiece{
    private boolean startingPosition = true;

    public Pawn(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public void move() {
        super.move();
        startingPosition = false;
    }
    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();
        String color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        pawnMoves(color, x, y, board, moves, startingPosition);
        return moves;
    }
}
