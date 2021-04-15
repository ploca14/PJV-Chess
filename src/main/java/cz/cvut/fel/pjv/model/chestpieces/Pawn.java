package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Pawn extends Chesspiece{
    private boolean startingPosition = true;
    private String color;

    public Pawn(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public void move() {
        super.move();
        startingPosition = false;
    }
    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        // startingPostition moves are different then notStarting
        // if checks starting position
        // moves when not taking enemy figure
        if (startingPosition) {
             if(board[x][y-1].currentChessPiece == null) { moves.add(board[x][y-1]); }
             if(board[x][y-2].currentChessPiece == null) { moves.add(board[x][y-2]); }
        } else {
            // first condition checks if Tile is not occupied, second checks if its on board
            if(!isOccupied(board, x, y) && !isOutOfRange(x,y)) {
                moves.add(board[x][y-1]);
            }
        }

        // taking enemy figure {x+1, y-1}, {x-1, y-1}
        if(isOccupied(board,x+1,y-1) && !isOutOfRange(x+1,y-1) && !isTeammate(board,x,y,color)) {
            moves.add(board[x+1][y+1]);
        }
        if(isOccupied(board,x-1,y-1) && !isOutOfRange(x-1,y-1) && !isTeammate(board,x,y,color)) {
            moves.add(board[x+1][y-1]);
        }
        return moves;
    }
}
