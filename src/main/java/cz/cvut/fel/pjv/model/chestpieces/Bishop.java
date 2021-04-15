package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Bishop extends Chesspiece{

    public Bishop(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {

        return null;
    }
}
