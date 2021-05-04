package cz.cvut.fel.pjv.model.chestpieces;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class King extends Chesspiece {
    public King(Color color, Tile currentPosition) {
        super(color, currentPosition);
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board) {
        Color color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        ArrayList<Tile> moves = new ArrayList<>();

        int[] xs = {x , x+1, x+1, x+1, x, x-1, x-1, x-1};
        int[] ys = {y-1, y-1, y, y+1, y+1, y+1, y, y-1};

        for (int i = 0; i < 8; i++) {;
            ArrayList<Tile> knightMovesList = new ArrayList<>();
            ArrayList<Tile> bishopMovesList = new ArrayList<>();
            ArrayList<Tile> rookMovesList = new ArrayList<>();
            ArrayList<Tile> pawnMovesList = new ArrayList<>();
            boolean continueChecking = true;

            if(isOutOfRange(xs[i],ys[i])) {
                continueChecking = false;
            }

            if(continueChecking) {
                knightMoves(color, xs[i], ys[i], board, knightMovesList);
                for (Tile t : knightMovesList
                ) {
                    if (t.currentChessPiece instanceof Knight && !isTeammate(board, x, y, t.getCurrentChessPiece().getColor())) {
                        continueChecking = false;
                    }
                }
            }

            if(continueChecking) {
                bishopMoves(color, xs[i], ys[i], board, bishopMovesList);
                for (Tile t: bishopMovesList
                ) {
                    if ((t.currentChessPiece instanceof Bishop && !isTeammate(board, x, y, t.getCurrentChessPiece().getColor())) || (t.currentChessPiece instanceof Queen && !isTeammate(board, x, y, t.getCurrentChessPiece().getColor()))) {
                        continueChecking = false;
                    }
                }
            }

            if(continueChecking) {
                rookMoves(color, xs[i], ys[i], board, rookMovesList);
                for (Tile t: rookMovesList
                ) {
                    if ((t.currentChessPiece instanceof Rook && !isTeammate(board, x, y, t.getCurrentChessPiece().getColor())) || (t.currentChessPiece instanceof Queen && !isTeammate(board, x, y, t.getCurrentChessPiece().getColor()))) {
                        continueChecking = false;
                    }
                }
            }

            if(continueChecking) {
                pawnMoves(color, xs[i], ys[i], board, pawnMovesList, false);
                for (Tile t: pawnMovesList
                ) {
                    if ((t.currentChessPiece instanceof Pawn && !isTeammate(board, x, y, t.getCurrentChessPiece().getColor()))) {
                        continueChecking = false;
                    }
                }
            }


            if(continueChecking) {
                moves.add(board[ys[i]][xs[i]]);
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return "\u265A";
    }
}
