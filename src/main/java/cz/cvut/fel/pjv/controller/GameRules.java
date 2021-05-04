package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.King;
import cz.cvut.fel.pjv.model.chestpieces.Tile;

public class GameRules {

    Board b;

    public GameRules() {
        Board b = new Board();
    }

    /**
     *
     * @param color: color which is to move
     * @return: if player to move is in Check, returns true
     */
    public boolean isCheck(String color) {
        Chesspiece king = null;
        Tile kingPosition = null;
        switch(color) {
            // black is moving
            case "Black":

                /**
                 * king & kingPosition init
                 */
                for (Chesspiece c: b.getBlackPieces()
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
                for (Chesspiece c: b.getWhitePieces()
                     ) {
                    for (Tile t: c.getLegalMoves(c.getCurrentPosition(), b.getBoard())
                         ) {
                        if (t.getX() == kingPosition.getX() && t.getY() == kingPosition.getY()) {
                            return true;
                        }
                    }
                }
                break;

            case "White":

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
                    for (Tile t: c.getLegalMoves(c.getCurrentPosition(), b.getBoard())
                    ) {
                        if (t.getX() == kingPosition.getX() && t.getY() == kingPosition.getY()) {
                            return true;
                        }
                    }
                }
                break;
        }

        return false;
    }
}
