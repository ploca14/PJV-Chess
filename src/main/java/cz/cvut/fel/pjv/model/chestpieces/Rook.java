package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Rook extends Chesspiece{
    private String color;
    public Rook(String color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public void move() {
        super.move();
    }

    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<Tile>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        // horizontal right
        for (int i = 1; i < 8; i++) {
            if(isOutOfRange(x+i,y)) {
                break;
            } else if (isOccupied(board, x+i, y) && isTeammate(board, x+i,y,color)) {
                break;
            } else if(!isOccupied(board, x+i, y)) {
                moves.add(board[x+i][y]);
            } else if (isOccupied(board, x+i, y) && !isTeammate(board, x+i, y, color)) {
                moves.add(board[x+i][y]);
                break;
            }
        }

        // horizontal left
        for (int i = 1; i < 8; i++) {
            if(isOutOfRange(x-i,y)) {
                break;
            } else if (isOccupied(board, x-i, y) && isTeammate(board, x-i,y,color)) {
                break;
            } else if(!isOccupied(board, x-i, y)) {
                moves.add(board[x-i][y]);
            } else if (isOccupied(board, x-i, y) && !isTeammate(board, x-i, y, color)) {
                moves.add(board[x-i][y]);
                break;
            }
        }
        // vertical up
        for (int i = 1; i < 8; i++) {
            if(isOutOfRange(x,y-i)) {
                break;
            } else if (isOccupied(board, x, y-i) && isTeammate(board, x,y-i,color)) {
                break;
            } else if(!isOccupied(board, x-i, y)) {
                moves.add(board[x][y-i]);
            } else if (isOccupied(board, x,y-i) && !isTeammate(board, x,y-i, color)) {
                moves.add(board[x][y-i]);
                break;
            }
        }

        // vertical down
        for (int i = 1; i < 8; i++) {
            if(isOutOfRange(x,y+i)) {
                break;
            } else if (isOccupied(board, x, y+i) && isTeammate(board, x,y+i,color)) {
                break;
            } else if(!isOccupied(board, x+i, y)) {
                moves.add(board[x][y+i]);
            } else if (isOccupied(board, x,y+i) && !isTeammate(board, x,y+i, color)) {
                moves.add(board[x][y+i]);
                break;
            }
        }
        return moves;
    }
}
