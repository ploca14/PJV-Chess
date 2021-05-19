package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.model.Board;

import java.util.ArrayList;

public class Knight extends Chesspiece {
    /**
     * constructor of knight piece
     * @param color color of chesspiece
     * @param currentPosition current position on the board
     */
    public Knight(Color color, Tile currentPosition) {
        super(color, currentPosition);
    }
    /**
     * get all legal moves according to movement rules
     * @param currentPosition: currentPosition of Chesspiece
     * @param board: board of Tiles
     * @return
     */
    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Board board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();
        Color color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        knightMoves(color, x, y, board.getBoard(), moves);
        return moves;
    }

    @Override
    protected Integer getLastRoundMoved() {
        return null;
    }

    @Override
    public String toString() {
        return "\u265E";
    }
}
