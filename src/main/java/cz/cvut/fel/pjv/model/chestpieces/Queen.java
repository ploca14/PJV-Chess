package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Chesspiece {
    private String color;
    public Queen(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();
        String color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();

        // bishopmoves
        bishopMoves(color, x, y, board, moves);

        // rook moves
        rookMoves(color, x, y, board, moves);

        return moves;
    }
}
