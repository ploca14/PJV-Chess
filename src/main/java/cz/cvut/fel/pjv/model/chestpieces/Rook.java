package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.model.Board;

import java.util.ArrayList;

public class Rook extends Chesspiece{

    public Rook(Color color, Tile currentPosition) {
        super(color, currentPosition);
    }

    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Board board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();
        Color color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        rookMoves(color, x, y, board.getBoard(), moves);
        return moves;
    }

    @Override
    public String toString() {
        return "\u265C";
    }
}
