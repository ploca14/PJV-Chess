package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Chesspiece {
    public Queen(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();
        String color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();

        /**
         * fill list of legal moves with moves for types Bishop and Rook
         */
        bishopMoves(color, x, y, board, moves);
        rookMoves(color, x, y, board, moves);

        return moves;
    }
}
