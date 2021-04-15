package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Bishop extends Chesspiece{
    private String color;

    public Bishop(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        // top right direction
        for (int i = 0; i < 10; i++) {
            if(isOutOfRange(x+i,y-i)) { break; }
            if(!isOccupied(board, x+i, y-i)) { moves.add(board[x+i][y-i]); }
            if(isOccupied(board,x+i,y-i)) {
                if (isTeammate(board,x+i,y-i,color)) { break; }
                else { moves.add(board[x+i][y-i]); break; }
            }
        }

        // top left direction
        for (int i = 0; i < 10; i++) {
            if(isOutOfRange(x-i,y-i)) { break; }
            if(!isOccupied(board, x-i, y-i)) { moves.add(board[x+i][y-i]); }
            if(isOccupied(board,x-i,y-i)) {
                if (isTeammate(board,x-i,y-i,color)) { break; }
                else { moves.add(board[x-i][y-i]); break; }
            }
        }

        // bot left direction
        for (int i = 0; i < 10; i++) {
            if(isOutOfRange(x-i,y+i)) { break; }
            if(!isOccupied(board, x-i, y+i)) { moves.add(board[x+i][y+i]); }
            if(isOccupied(board,x-i,y+i)) {
                if (isTeammate(board,x-i,y+i,color)) { break; }
                else { moves.add(board[x-i][y+i]); break; }
            }
        }

        // bot right direction
        for (int i = 0; i < 10; i++) {
            if(isOutOfRange(x+i,y+i)) { break; }
            if(!isOccupied(board, x+i, y+i)) { moves.add(board[x+i][y+i]); }
            if(isOccupied(board,x+i,y+i)) {
                if (isTeammate(board,x+i,y+i,color)) { break; }
                else { moves.add(board[x+i][y+i]); break; }
            }
        }


        return null;
    }
}
