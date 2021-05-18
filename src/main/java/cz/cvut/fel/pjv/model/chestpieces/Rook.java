package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.model.Board;

import java.util.ArrayList;

public class Rook extends Chesspiece{
    private boolean startingPosition;

    /**
     * constructor of rook piece
     * @param color color of chesspiece
     * @param currentPosition current position on the board
     */
    public Rook(Color color, Tile currentPosition) {
        super(color, currentPosition);
        startingPosition = true;
    }

    /**
     * get all legal moves according to movement rules
     * @param currentPosition: currentPosition of Chesspiece
     * @param board: board of Tiles
     * @return
     */
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Board board) {
        // moves to return
        ArrayList<Tile> moves = new ArrayList<Tile>();
        Color color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        rookMoves(color, x, y, board.getBoard(), moves);
        return moves;
    }

    @Override
    protected Integer getLastRoundMoved() {
        return null;
    }

    @Override
    public String toString() {
        return "\u265C";
    }

    public boolean isStartingPosition() {
        return startingPosition;
    }
}
