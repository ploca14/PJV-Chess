package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Knight extends Chesspiece {
    private String color;
    public Knight(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        // all combinations:
        // x+1 y-2, x+2 y-1...
        int[] xs = {x+1, x+2, x+2, x+1, x-1, x-2, x-2, x-1};
        int[] ys = {y-2, y-1, y+1, y+2, y+2, y+1, y-1, y-2};

        for (int i = 0; i < 8; i++) {
            if(isOutOfRange(ys[i], xs[i])) { continue; }

            if(!isOccupied(board, xs[i], ys[i])) {
                moves.add(board[ys[i]][xs[i]]);
            } else if (isOccupied(board,  xs[i],ys[i])) {
                if (!isTeammate(board, xs[i],ys[i], currentPosition.getCurrentChessPiece().getColor())) {
                    moves.add(board[ys[i]][xs[i]]);
                }
            }
        }
        return moves;
    }
}
