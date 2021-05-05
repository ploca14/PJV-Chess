package cz.cvut.fel.pjv.model;

import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Tile;

public class Move {
    private final Tile startingPosition;
    private final Tile endingPosition;
    private final Chesspiece chesspiece;

    public Move(Tile startingPosition, Tile endingPosition) {
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
        this.chesspiece = startingPosition.currentChessPiece;
    }

    public Tile getStartingPosition() {
        return startingPosition;
    }

    public Tile getEndingPosition() {
        return endingPosition;
    }

    public Chesspiece getChesspiece() {
        return chesspiece;
    }
}
