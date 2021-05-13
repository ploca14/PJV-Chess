package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.chestpieces.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class GameRules implements Serializable {

    private final Board board;

    public GameRules(Board board) {
        this.board = board;
    }

    /**
     *
     * @param color: color which is to move
     * @return: if player to move is in Check, returns true
     */
    public boolean isCheck(Color color, Board b) {
        Chesspiece king = null;
        Tile kingPosition = null;
        if(color.equals(Color.BLACK)) {
            // black is moving

            /**
             * king & kingPosition init
             */
            for (Chesspiece c : b.getBlackPieces()
            ) {
                if (c instanceof King) {
                    king = c;
                }
            }
            kingPosition = king.getCurrentPosition();

            /**
             * searching if any of enemy Chesspieces has King's position as it's legal move
             * if a match is found, fun returns true
             */
            for (Chesspiece c : b.getWhitePieces()
            ) {
                for (Tile t : c.getLegalMoves(c.getCurrentPosition(), b)
                ) {
                    if (t.getX() == kingPosition.getX() && t.getY() == kingPosition.getY()) {
                        return true;
                    }
                }
            }
        } else {
            // white is moving
            /**
             * king & kingPosition init
             */
            for (Chesspiece c: b.getWhitePieces()
            ) {
                if(c instanceof King) {
                    king = c;
                }
            }
            kingPosition = king.getCurrentPosition();

            /**
             * searching if any of enemy Chesspieces has King's position as it's legal move
             * if a match is found, fun returns true
             */
            for  (Chesspiece c: b.getBlackPieces()
            ) {
                for (Tile t: c.getLegalMoves(c.getCurrentPosition(), b)
                ) {
                    if (t.getX() == kingPosition.getX() && t.getY() == kingPosition.getY()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public ArrayList<Tile> getLegalNotCheckMoves(Chesspiece cp) {
        ArrayList<Tile> moves;
        ArrayList<Tile> movesFiltered = new ArrayList<>();
        Tile oldPosition = cp.getCurrentPosition();
        moves = cp.getLegalMoves(cp.getCurrentPosition(), board);
        Chesspiece oldCp;


        for (Tile t: moves
        ) {
            if(t.getCurrentChessPiece() != null) {
                oldCp = t.currentChessPiece;
            } else {
                oldCp = null;
            }
            board.getBoard()[t.getY()][t.getX()].setCurrentChessPiece(cp);
            board.getBoard()[oldPosition.getY()][oldPosition.getX()].setCurrentChessPiece(null);
            cp.setCurrentPosition(board.getBoard()[t.getY()][t.getX()]);
            board.getBoard()[t.getY()][t.getX()].currentChessPiece = cp;
            if(!isCheck(cp.getColor(), board)) {
                movesFiltered.add(t);
            }
            board.getBoard()[t.getY()][t.getX()].currentChessPiece = oldCp;
        }

        cp.setCurrentPosition(oldPosition);
        board.getBoard()[oldPosition.getY()][oldPosition.getX()].setCurrentChessPiece(cp);
        return movesFiltered;
    }

    public boolean ísCheckMate(Color color, Board b) {
        ArrayList<Chesspiece> chesspieces;
        if(color.equals(Color.BLACK)) {
            chesspieces = b.getBlackPieces();

        } else {
            chesspieces = b.getBlackPieces();
        }

        for (Chesspiece c: chesspieces
        ) {
            {
                ArrayList<Tile> moves = c.getLegalMoves(c.getCurrentPosition(), b);
                if(moves.size() == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
