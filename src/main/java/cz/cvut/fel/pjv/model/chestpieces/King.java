package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.model.Board;

import java.util.ArrayList;

public class King extends Chesspiece {
    private boolean startingPosition;

    public King(Color color, Tile currentPosition) {
        super(color, currentPosition);
        startingPosition = true;
    }

    @Override
    public ArrayList<Tile> getLegalMoves(Tile currentPosition, Board board) {
        Color color = currentPosition.getCurrentChessPiece().getColor();
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        ArrayList<Tile> moves = new ArrayList<>();

        /**
         * xs: array of x coordinates clockwise
         * ys: array of y coordinates clockwise
         */
        int[] xs = {x , x+1, x+1, x+1, x, x-1, x-1, x-1};
        int[] ys = {y-1, y-1, y, y+1, y+1, y+1, y, y-1};

        /**
         * checking rules for every (x;y)
         * adding legal moves to passed array
         */
        for (int i = 0; i < 8; i++) {;
            ArrayList<Tile> knightMovesList = new ArrayList<>();
            ArrayList<Tile> bishopMovesList = new ArrayList<>();
            ArrayList<Tile> rookMovesList = new ArrayList<>();
            ArrayList<Tile> pawnMovesList = new ArrayList<>();
            boolean continueChecking = true;

            if(isOutOfRange(xs[i],ys[i])) {
                continueChecking = false;
            }

            /**
             * filling array with legal moves for Chesspiece of type Knight for coordinates (y[i];x[i])
             * for every move from array of legal moves check if there's enemy Chesspiece of type Knight
             * if there is enemy Chesspiece of type Knight, move is not legal anymore
             * stop further testing: bool "continueChecking": true->false
             */
            if(continueChecking) {
                knightMoves(color, xs[i], ys[i], board.getBoard(), knightMovesList);
                for (Tile t : knightMovesList
                ) {
                    if (t.currentChessPiece instanceof Knight
                            && !isTeammate(board.getBoard(), x, y, t.getCurrentChessPiece().getColor())) {
                        continueChecking = false;
                    }
                }
            }

            /**
             * filling array with legal moves for Chesspiece of type Bishop for coordinates (y[i];x[i])
             * for every move from array of legal moves check if there's enemy Chesspiece of type Bishop/Queen
             * if there is enemy Chesspiece of type Bishop/Queen, move is not legal anymore
             * stop further testing: bool "continueChecking": true->false
             */
            if(continueChecking) {
                bishopMoves(color, xs[i], ys[i], board.getBoard(), bishopMovesList);
                for (Tile t: bishopMovesList
                ) {
                    if ((t.currentChessPiece instanceof Bishop
                            && !isTeammate(board.getBoard(), x, y, t.getCurrentChessPiece().getColor())) || (t.currentChessPiece instanceof Queen
                            && !isTeammate(board.getBoard(), x, y, t.getCurrentChessPiece().getColor()))) {
                        continueChecking = false;
                    }
                }
            }

            /**
             * filling array with legal moves for Chesspiece of type Rook for coordinates (y[i];x[i])
             * for every move from array of legal moves check if there's enemy Chesspiece of type Rook/Queen
             * if there is enemy Chesspiece of type Rook/Queen, move is not legal anymore
             * stop further testing: bool "continueChecking": true->false
             */
            if(continueChecking) {
                rookMoves(color, xs[i], ys[i], board.getBoard(), rookMovesList);
                for (Tile t: rookMovesList
                ) {
                    if ((t.currentChessPiece instanceof Rook
                            && !isTeammate(board.getBoard(), x, y, t.getCurrentChessPiece().getColor())) || (t.currentChessPiece instanceof Queen
                            && !isTeammate(board.getBoard(), x, y, t.getCurrentChessPiece().getColor()))) {
                        continueChecking = false;
                    }
                }
            }

            /**
             * filling array with legal moves for Chesspiece of type Pawn for coordinates (y[i];x[i])
             * for every move from array of legal moves check if there's enemy Chesspiece of type Pawn
             * if there is enemy Chesspiece of type Pawn, move is not legal anymore
             * stop further testing: bool "continueChecking": true->false
             */
            if(continueChecking && !isOutOfRange(xs[i], ys[i])) {
                pawnMoves(color, xs[i], ys[i], board.getBoard(), pawnMovesList, false);
                for (Tile t: pawnMovesList
                ) {
                    if ((t.currentChessPiece instanceof Pawn && !isTeammate(board.getBoard(), x, y, t.getCurrentChessPiece().getColor()))) {
                        continueChecking = false;
                    }
                }
            }

            if(continueChecking) {
                if(isOccupied(board.getBoard(), xs[i], ys[i])) {
                    if(isTeammate(board.getBoard(), xs[i], ys[i], currentPosition.getCurrentChessPiece().getColor())) {
                        continueChecking = false;
                    }
                    if(!isTeammate(board.getBoard(), xs[i], ys[i], currentPosition.getCurrentChessPiece().getColor())) {
                        moves.add(board.getBoard()[ys[i]][xs[i]]);
                    }
                } else if (!isOccupied(board.getBoard(), xs[i], ys[i])) {
                    moves.add(board.getBoard()[ys[i]][xs[i]]);
                }
            }
        }

        canDoShortRosada(currentPosition, board.getBoard(), moves);

        canDoLongRosada(currentPosition, board.getBoard(), moves);

        return moves;
    }

    @Override
    protected Integer getLastRoundMoved() {
        return null;
    }

    @Override
    public String toString() {
        return "\u265A";
    }

    private void canDoShortRosada(Tile currentPosition, Tile[][] board, ArrayList<Tile> moves) {
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        if(startingPosition) {
            if(!isOutOfRange(x+3,y)) {
                if(board[y][x+3].getCurrentChessPiece() instanceof Rook
                        && ((Rook) board[y][x+3].getCurrentChessPiece()).isStartingPosition()
                        && !isOccupied(board, x+1, y)
                        && !isOccupied(board, x+2, y)) {
                    moves.add(board[y][x+3]);
                }
            }
        }
    }

    private void canDoLongRosada(Tile currentPosition, Tile[][] board, ArrayList<Tile> moves) {
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        if(startingPosition) {
            if(!isOutOfRange(x-4,y)) {
                if(board[y][x-4].getCurrentChessPiece() instanceof Rook
                        && ((Rook) board[y][x-4].getCurrentChessPiece()).isStartingPosition()
                        && !isOccupied(board, x-1, y)
                        && !isOccupied(board, x-2, y)
                        && !isOccupied(board, x-3, y))  {
                    moves.add(board[y][x-4]);
                }
            }
        }
    }
}
