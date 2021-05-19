package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.controller.network.LobbyController;
import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.chestpieces.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class GameRules implements Serializable {
    private final static Logger logger = Logger.getLogger(LobbyController.class.getName());
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

    /**
     * filtering method for all legal moves
     * @param cp chesspiece
     * @return list of legal moves not causing check / getting king out of check
     */
    public ArrayList<Tile> getLegalNotCheckMoves(Chesspiece cp) {
        logger.info("generating legal moves for "+cp.getCurrentPosition().getCurrentChessPiece().getColor().toString()+" "+cp+" at y: "+cp.getCurrentPosition().getY()+" ,x: "+cp.getCurrentPosition().getX());
        ArrayList<Tile> moves;
        ArrayList<Tile> movesFiltered = new ArrayList<>();
        Tile oldPosition = cp.getCurrentPosition();
        // get all moves for the chesspiece
        moves = cp.getLegalMoves(cp.getCurrentPosition(), board);
        Chesspiece oldCp;

        logger.info("number of all legal moves: "+moves.size());
        // for every move of the tile:
        // set chesspieces position to the tile
        // set old position of the chesspiece to null
        // check if the king is in check with those 2 changes
        // return board to original state
        // test again with another move option
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

        // return filtered move list
        logger.info("number of legal moves after filtering: "+movesFiltered.size());
        return movesFiltered;
    }

    /**
     * check if the game is over
     * @param color of the moving player
     * @param b board
     * @return true if there are not any legal moves for the player -> game is over
     */
    public boolean isEndgame(Color color, Board b) {
        ArrayList<Chesspiece> chesspieces;

        if(color.equals(Color.BLACK)) {
            chesspieces = b.getBlackPieces();
        } else {
            chesspieces = b.getWhitePieces();
        }

        int sum = 0;
        for (Chesspiece c: chesspieces
        ) {
            {
                ArrayList<Tile> moves = getLegalNotCheckMoves(c);
                sum += moves.size();
            }
        }
        return sum == 0;
    }
}
