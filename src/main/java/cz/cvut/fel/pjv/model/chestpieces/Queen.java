package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.model.Board;

import java.util.ArrayList;

public class Queen extends Chesspiece {
    public Queen(Color color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Board board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();
        Color color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();

        /**
         * fill list of legal moves with moves for types Bishop and Rook
         */
        bishopMoves(color, x, y, board.getBoard(), moves);
        rookMoves(color, x, y, board.getBoard(), moves);

        return moves;
    }
    // :W

    @Override
    public String toString() {
        return "\u265B";
    }
}
