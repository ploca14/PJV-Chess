package cz.cvut.fel.pjv.model;

import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Pawn;
import cz.cvut.fel.pjv.model.chestpieces.Tile;

public class Move {
    private final Tile startingPosition;
    private final Tile endingPosition;
    private final Chesspiece chesspiece;
    private boolean isPawnPromoting = false;

    public Move(Tile startingPosition, Tile endingPosition) {
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
        this.chesspiece = startingPosition.currentChessPiece;

        // Check if the move is a Pawn promoting move
        if (chesspiece instanceof Pawn && endingPosition.getY() == 0 || endingPosition.getY() == 7) {
            isPawnPromoting = true;
        }
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

    public boolean isPawnPromoting() {
        return isPawnPromoting;
    }
}
