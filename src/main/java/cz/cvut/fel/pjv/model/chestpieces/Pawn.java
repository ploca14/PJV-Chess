package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Game;

import java.util.ArrayList;

public class Pawn extends Chesspiece {
    private boolean startingPosition = true;
    private int moveCount = 0;
    private Tile lastPosition;
    private int lastRoundMoved;
    private Board playingBoard;

    public int getMoveCount() {
        return moveCount;
    }

    public Tile getLastPosition() {
        return lastPosition;
    }

    public Integer getLastRoundMoved() {
        return lastRoundMoved;
    }


    public Pawn(Color color, Tile currentPosition) {
        super(color, currentPosition);
        lastPosition = currentPosition;
    }

    @Override
    public void move(Tile endingPosition) {
        super.move(endingPosition);
        startingPosition = false;
        moveCount++;
        lastRoundMoved = playingBoard.getGame().getTurnCount();
    }


    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Board board) {
        playingBoard = board;
        lastPosition = currentPosition;

        ArrayList<Tile> moves = new ArrayList<Tile>();
        Color color = getCurrentPosition().getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        pawnMoves(color, x, y, board.getBoard(), moves, startingPosition);

        // en passant
        if(color.equals(Color.BLACK)) {
            if(!isOutOfRange(x+1, y) && isOccupied(board.getBoard(), x+1, y) && !isTeammate(board.getBoard(), x+1, y, color)) {
                if((board.getBoard()[y][x+1].getCurrentChessPiece() instanceof Pawn
                        && board.getBoard()[y][x+1].currentChessPiece.getMoveCount() == 1
                        && board.getBoard()[y][x+1].getY() == 4
                        && (board.getGame().getTurnCount()) - (board.getBoard()[y][x+1].currentChessPiece.getLastRoundMoved()) == 1)) {
                    moves.add(board.getBoard()[y+1][x+1]);
                }
            }

            if(!isOutOfRange(x-1, y) &&  isOccupied(board.getBoard(), x-1, y) && !isTeammate(board.getBoard(), x-1, y, color)) {
                if((board.getBoard()[y][x-1].getCurrentChessPiece() instanceof Pawn
                        && board.getBoard()[y][x-1].currentChessPiece.getMoveCount() == 1
                        && board.getBoard()[y][x-1].getY() == 4
                        && (board.getGame().getTurnCount()) - (board.getBoard()[y][x-1].currentChessPiece.getLastRoundMoved()) == 1)) {
                    moves.add(board.getBoard()[y+1][x-1]);
                }
            }

        }
        else {
            if(!isOutOfRange(x+1, y) && isOccupied(board.getBoard(), x+1, y) && !isTeammate(board.getBoard(), x+1, y, color)) {
                if((board.getBoard()[y][x+1].getCurrentChessPiece() instanceof Pawn
                        && board.getBoard()[y][x+1].currentChessPiece.getMoveCount() == 1
                        && board.getBoard()[y][x+1].getY() == 3
                        && (board.getGame().getTurnCount()) - (board.getBoard()[y][x+1].currentChessPiece.getLastRoundMoved()) == 1)) {
                    moves.add(board.getBoard()[y-1][x+1]);
                }
            }

            if(!isOutOfRange(x-1, y) &&  isOccupied(board.getBoard(), x-1, y) && !isTeammate(board.getBoard(), x-1, y, color)) {
                if((board.getBoard()[y][x-1].getCurrentChessPiece() instanceof Pawn
                        && board.getBoard()[y][x-1].currentChessPiece.getMoveCount() == 1
                        && board.getBoard()[y][x-1].getY() == 3
                        && (board.getGame().getTurnCount()) - (board.getBoard()[y][x-1].currentChessPiece.getLastRoundMoved()) == 1)) {
                    moves.add(board.getBoard()[y-1][x-1]);
                }
            }
        }
    return moves;
    }

    @Override
    public String toString() {
        return "\u265F";
    }
}
