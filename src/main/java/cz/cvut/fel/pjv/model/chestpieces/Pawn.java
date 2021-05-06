package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.model.Board;

import java.util.ArrayList;

public class Pawn extends Chesspiece {
    private boolean startingPosition = true;

    public Pawn(Color color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public void move(Tile endingPosition) {
        super.move(endingPosition);
        startingPosition = false;
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Board board) {
        System.out.println(board.getGame().getCurrentPlayer());
        System.out.println(board.getGame().getTurnCount());

        ArrayList<Tile> moves = new ArrayList<Tile>();
        Color color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        pawnMoves(color, x, y, board.getBoard(), moves, startingPosition);
        return moves;
    }

    @Override
    public String toString() {
        return "\u265F";
    }
}
