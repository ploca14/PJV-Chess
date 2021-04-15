package cz.cvut.fel.pjv.model.chestpieces;

import java.util.ArrayList;

public class Rook extends Chesspiece{
    private String color;

    public Rook(String color, Tile currentPosition) {
        super(color, currentPosition);
        color = getColor() ;
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
            } else if (isOccupied(board, x+i, y) && isTeammate(board, x+i,y,currentPosition.getCurrentChessPiece().getColor())) {
                break;
            } else if(!isOccupied(board, x+i, y)) {
                moves.add(board[y][x+i]);
            } else if (isOccupied(board, x+i, y) && !isTeammate(board, x+i, y, currentPosition.getCurrentChessPiece().getColor())) {
                moves.add(board[y][x+i]);
                break;
            }
        }

        // horizontal left
        for (int i = 1; i < 8; i++) {
            if(isOutOfRange(x-i,y)) {
                break;
            } else if (isOccupied(board, x-i, y) && isTeammate(board, x-i,y,currentPosition.getCurrentChessPiece().getColor())) {
                break;
            } else if(!isOccupied(board, x-i, y)) {
                moves.add(board[y][x-i]);
            } else if (isOccupied(board, x-i, y) && !isTeammate(board, x-i, y, currentPosition.getCurrentChessPiece().getColor())) {
                moves.add(board[y][x-i]);
                break;
            }
        }
        // vertical up
        for (int i = 0; i < 8; i++) {
            if(isOutOfRange(x,y-i)) {
                break;
            } else if (isOccupied(board, x, y-i) && isTeammate(board, x,y-i,currentPosition.getCurrentChessPiece().getColor())) {
                break;
            } else if(!isOccupied(board, x-i, y)) {
                moves.add(board[y-i][x]);
            } else if (isOccupied(board, x,y-i) && !isTeammate(board, x,y-i, currentPosition.getCurrentChessPiece().getColor())) {
                moves.add(board[y-i][x]);
                break;
            }
        }

        // vertical down
        for (int i = 1; i < 8; i++) {
            if(isOutOfRange(x,y+i)) {
                break;
            } else if (isOccupied(board, x, y+i) && isTeammate(board, x,y+i, currentPosition.getCurrentChessPiece().getColor())) {
                break;
            } else if(!isOccupied(board, x, y+i)) {
                moves.add(board[y+i][x]);
            } else if (isOccupied(board, x,y+i) && !isTeammate(board, x,y+i, currentPosition.getCurrentChessPiece().getColor())) {
                moves.add(board[y+i][x]);
                break;
            }
        }
        return moves;
    }
}
