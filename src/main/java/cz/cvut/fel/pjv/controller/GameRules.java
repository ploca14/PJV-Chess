package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.King;
import cz.cvut.fel.pjv.model.chestpieces.Tile;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class GameRules implements Serializable {

    Board b;

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
            for (Chesspiece c: b.getBlackPieces()
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
}
