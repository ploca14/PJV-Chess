package cz.cvut.fel.pjv.model.chestpieces;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Queen extends Chesspiece {
    public Queen(Color color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();
        Color color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();

        /**
         * fill list of legal moves with moves for types Bishop and Rook
         */
        bishopMoves(color, x, y, board, moves);
        rookMoves(color, x, y, board, moves);

        return moves;
    }
    // :W

    @Override
    public String toString() {
        return "\u265B";
    }
}
