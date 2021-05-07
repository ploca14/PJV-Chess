package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.King;
import cz.cvut.fel.pjv.model.chestpieces.Tile;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

public class GameRules implements Serializable {

    private Board b;

    public GameRules(Board board) {
        b = board;
    }

    /**
     *
     * @param color: color which is to move
     * @return: if player to move is in Check, returns true
     */
    public boolean isCheck(Color color) {
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

    public ArrayList<Chesspiece> getMoveableChesspieces(Color color) {
        ArrayList<Chesspiece> result = new ArrayList<>();
        ArrayList<Chesspiece> checkedArrayList;
        ArrayList<Tile> allLegalMoves;

        if(color.equals(Color.WHITE)) {
            if(isCheck(color)) {
                checkedArrayList = b.whitePieces;
                for (Chesspiece cp: checkedArrayList
                     ) {
                    allLegalMoves = cp.getLegalMoves(cp.getCurrentPosition(), b);
                    for (Tile t: allLegalMoves
                         ) {
                        Tile oldPosition = cp.getCurrentPosition();
                        cp.setCurrentPosition(t);
                        if(!isCheck(Color.WHITE)) {
                            result.add(cp);
                        }
                        cp.setCurrentPosition(oldPosition);
                    }
                }
            }
        } else {
            if(isCheck(color)) {
                checkedArrayList = b.blackPieces;
                for (Chesspiece cp: checkedArrayList
                ) {
                    allLegalMoves = cp.getLegalMoves(cp.getCurrentPosition(), b);
                    for (Tile t: allLegalMoves
                    ) {
                        Tile oldPosition = cp.getCurrentPosition();
                        cp.setCurrentPosition(t);
                        if(!isCheck(Color.BLACK)) {
                            result.add(cp);
                        }
                        cp.setCurrentPosition(oldPosition);
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<Tile> getLegalMovesForBlockingCheck(Chesspiece cp) {
        ArrayList<Tile> moves;
        ArrayList<Tile> movesFiltered = new ArrayList<>();

        moves = cp.getLegalMoves(cp.getCurrentPosition(), b);

        for (Tile t: moves
             ) {
            Tile oldPosition = t;
            cp.setCurrentPosition(t);
            if(!isCheck(cp.getColor().getPaintColor())) {
                movesFiltered.add(t);
            }
            cp.setCurrentPosition(oldPosition);
        }

        return movesFiltered;
    }
}
