package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.model.Board;

import java.util.ArrayList;

public class King extends Chesspiece {
    private boolean startingPosition;
    /**
     * constructor of king piece
     * @param color color of chesspiece
     * @param currentPosition current position on the board
     */
    public King(Color color, Tile currentPosition) {
        super(color, currentPosition);
        startingPosition = true;
    }

    /**
     * move of the king, after first move change starting position to false
     * @param endingPosition new position for the king
     */
    @Override
    public void move(Tile endingPosition) {
        super.move(endingPosition);
        startingPosition = false;
    }
    /**
     * get all legal moves according to movement rules
     * @param currentPosition: currentPosition of Chesspiece
     * @param board: board of Tiles
     * @return
     */
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
        for (int i = 0; i < 8; i++) {
            // creating moves for every chesspiece movement from the currentposition
            ArrayList<Tile> knightMovesList = new ArrayList<>();
            ArrayList<Tile> bishopMovesList = new ArrayList<>();
            ArrayList<Tile> rookMovesList = new ArrayList<>();
            ArrayList<Tile> pawnMovesList = new ArrayList<>();
            boolean continueChecking = true;

            // if the tested tile is out of range, stop testing this tile
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

            /**
             * check the new tile if enemy king is around it
             */
            if(continueChecking && !isOutOfRange(xs[i], ys[i])) {
                if(isKingAround(board.getBoard()[ys[i]][xs[i]], board, color)) {
                    continueChecking = false;
                }
            }

            /**
             * if the tile is occupied by teammate, dont add as legal move
             * if the tile is occupied by enemy or is empty, add as legal move
             */
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

        // check for short rosada moves
        canDoShortRosada(currentPosition, board.getBoard(), moves);

        // check for long rosada moves
        canDoLongRosada(currentPosition, board.getBoard(), moves);

        return moves;
    }

    /**
     * check if enemy king chesspiece can reach current position
     * @param currentPosition tested position of the enemy king presence
     * @param board playing board
     * @param color color of king which wanted to move on the chesspiece
     * @return true if enemy king is nearby the current position
     */
    private boolean isKingAround(Tile currentPosition, Board board, Color color) {
        int x = currentPosition.getX();
        int y = currentPosition.getY();

        // every x and y coordinates, clockwise
        int[] xs = {x , x+1, x+1, x+1, x, x-1, x-1, x-1};
        int[] ys = {y-1, y-1, y, y+1, y+1, y+1, y, y-1};

        for (int i = 0; i < 8; i++) {
            if(!isOutOfRange(xs[i], ys[i])) {
                Tile position = board.getBoard()[ys[i]][xs[i]];
                // if tested position.currentChesspiece is different color king, return true
                if(position.getCurrentChessPiece() instanceof King && !position.getCurrentChessPiece().getColor().equals(color)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected Integer getLastRoundMoved() {
        return null;
    }

    @Override
    public String toString() {
        return "\u265A";
    }

    /**
     * check if it is possible to make short rosada move
     * @param currentPosition current position of the king
     * @param board playing board
     * @param moves all legal moves of the king
     */
    private void canDoShortRosada(Tile currentPosition, Tile[][] board, ArrayList<Tile> moves) {
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        if(startingPosition) {
            if(!isOutOfRange(x+3,y)) {
                // check if (x+3;y) is Rook and if the Rook is at its starts position
                // also check if tiles between rook and king are empty
                if(board[y][x+3].getCurrentChessPiece() instanceof Rook
                        && ((Rook) board[y][x+3].getCurrentChessPiece()).isStartingPosition()
                        && !isOccupied(board, x+1, y)
                        && !isOccupied(board, x+2, y)) {
                    moves.add(board[y][x+2]);
                }
            }
        }
    }
    /**
     * check if it is possible to make long rosada move
     * @param currentPosition current position of the king
     * @param board playing board
     * @param moves all legal moves of the king
     */
    private void canDoLongRosada(Tile currentPosition, Tile[][] board, ArrayList<Tile> moves) {
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        if(startingPosition) {
            // check if (x-4;y) is Rook and if the Rook is at its starts position
            // also check if tiles between rook and king are empty
            if(!isOutOfRange(x-3,y) && !isOutOfRange(x-4, y)) {
                if(board[y][x-4].getCurrentChessPiece() instanceof Rook
                        && ((Rook) board[y][x-4].getCurrentChessPiece()).isStartingPosition()
                        && !isOccupied(board, x-1, y)
                        && !isOccupied(board, x-2, y)
                        && !isOccupied(board, x-3, y))  {
                    moves.add(board[y][x-2]);
                }
            }
        }
    }
}
