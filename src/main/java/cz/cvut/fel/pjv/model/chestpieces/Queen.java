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

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        // bishopmoves
        ArrayList<Tile> bishopMoves = new ArrayList<Tile>();
        Bishop b = new Bishop(color, currentPosition);
        bishopMoves = b.getLegalMoves(currentPosition, board);

        // rook moves
        ArrayList<Tile> rookMoves = new ArrayList<Tile>();
        Rook r = new Rook(color, currentPosition);
        rookMoves = r.getLegalMoves(currentPosition, board);

        // connect
        moves.addAll(bishopMoves);
        moves.addAll(rookMoves);

        return null;
    }
}
