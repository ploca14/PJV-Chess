package cz.cvut.fel.pjv.model;

import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.King;
import cz.cvut.fel.pjv.model.chestpieces.Pawn;
import cz.cvut.fel.pjv.model.chestpieces.Tile;

import java.io.Serializable;

public class Move implements Serializable {
    private final Tile startingPosition;
    private final Tile endingPosition;
    private final Chesspiece chesspiece;
    private boolean isPawnPromoting = false;
    private boolean isShortRosada = false;
    private boolean isLongRosada = false;
    private boolean isEnPassant = false;

    public Move(Tile startingPosition, Tile endingPosition) {
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
        this.chesspiece = startingPosition.currentChessPiece;

        // Check if the move is a Pawn promoting move
        if (chesspiece.getClass().equals(Pawn.class) && (endingPosition.getY() == 0 || endingPosition.getY() == 7)) {
            isPawnPromoting = true;
        } else if(chesspiece.getClass().equals(King.class) && endingPosition.getY() == startingPosition.getY() && endingPosition.getX() - 2 == startingPosition.getX()) {
            isShortRosada = true;
        } else if(chesspiece.getClass().equals(King.class) && endingPosition.getY() == startingPosition.getY() && endingPosition.getX() + 2 == startingPosition.getX()) {
            isLongRosada = true;
        } else if(chesspiece.getClass().equals(Pawn.class) && endingPosition.getX() != startingPosition.getX() && endingPosition.getCurrentChessPiece() == null) {
            isEnPassant = true;
        }
    }

    public boolean isEnPassant() { return isEnPassant; }
    public boolean isShortRosada() { return isShortRosada; }
    public boolean isLongRosada() { return isLongRosada; }

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

    public boolean isSpecial() {
        return isEnPassant || isPawnPromoting || isLongRosada || isShortRosada;
    }

    @Override
    public String toString() {
        return  "from " + startingPosition.getX() + ", " + startingPosition.getY() +
                " to " + endingPosition.getX() + ", " + endingPosition.getY() +
                " with  " + chesspiece +
                " (isPawnPromoting=" + isPawnPromoting +
                ", isShortRosada=" + isShortRosada +
                ", isLongRosada=" + isLongRosada +
                ", isEnPassant=" + isEnPassant +
                ')';
    }
}
