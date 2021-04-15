package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Queen extends Chesspiece {
    public Queen(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {

        return null;
    }
}
