package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.controller.Board;

import java.util.ArrayList;

public abstract class Chesspiece {
    private Tile currentPosition;
    private String color;

    public Chesspiece(String color, Tile currentPosition) {
        this.color = color;
        this.currentPosition = currentPosition;
    }

    public void move() {
    }

    /**
     *
     * @param currentPosition: currentPosition of Chesspiece
     * @param board: board of Tiles
     * @return ArrayList of legal moves for Bishop
     */
    public abstract ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board);


    public Tile getCurrentPosition() {
        return currentPosition;
    }

    public String getColor() {
        return color;
    }

    /**
     * checks if Tile with coordinates (y;x) is occupied
     * @param board board of Tiles
     * @param x x coordinate
     * @param y y coordinate
     * @return true if Tile is occupied by some figure
     */
    public boolean isOccupied(Tile[][]board, int x, int y) {
        return board[y][x].currentChessPiece != null;
    }

    /**
     * checks if indexes are out of range
     * @param x x coordinate
     * @param y y coordinate
     * @return true if location with coordinates (y;x) are out of range
     */
    public boolean isOutOfRange(int x, int y) {
        return (x > 7 || x < 0 || y > 7 || y < 0);
    }

    /**
     * check if figure on Tile with coordiantes (y;x) is same Color
     * @param b board of tiles
     * @param x x coordinate of tested Tile
     * @param y y coordinate of tested Tile
     * @param color color of Chesspiece compared to Chesspiece on Tile with coordinates (y;x)
     * @return true if compared figures have same color. if the color is differen, returns false
     */
    public boolean isTeammate(Tile[][] b, int x, int y, String color) {
        return (b[y][x].currentChessPiece.getColor()).equals(color);
    }

    /**
     * fills inserted ArrayList<Tile> with legal moves for Chesspiece of type Knight
     * @param color color of Chesspiece
     * @param x x location of Chesspiece
     * @param y y location of Chesspiece
     * @param board board of Tiles
     * @param moves ArrayList<Tile> of moves for Chesspiece. Can be empty or contain some data
     */
    public void knightMoves(String color, int x , int y, Tile[][] board, ArrayList<Tile> moves) {

        /**
         * xs: array of x coordinates clockwise
         * ys: array of y coordinates clockwise
         */
        int[] xs = {x+1, x+2, x+2, x+1, x-1, x-2, x-2, x-1};
        int[] ys = {y-2, y-1, y+1, y+2, y+2, y+1, y-1, y-2};

        /**
         * checking rules for every (x;y)
         * adding legal moves to passed array
         */
        for (int i = 0; i < 8; i++) {
            if(isOutOfRange(ys[i], xs[i])) { continue; }

            if(!isOccupied(board, xs[i], ys[i])) {
                moves.add(board[ys[i]][xs[i]]);
            } else if (isOccupied(board,  xs[i],ys[i])) {
                if (!isTeammate(board, xs[i],ys[i], color)) {
                    moves.add(board[ys[i]][xs[i]]);
                }
            }
        }
    }

    /**
     * fills inserted ArrayList<Tile> with legal moves for Chesspiece of type Bishop
     * @param color color of Chesspiece
     * @param x x location of Chesspiece
     * @param y y location of Chesspiece
     * @param board board of Tiles
     * @param moves ArrayList<Tile> of moves for Chesspiece. Can be empty or contain some data
     */
    public void bishopMoves(String color, int x , int y, Tile[][] board, ArrayList<Tile> moves) {
        /**
         * moves with direction: top right
         */
        for (int i = 1; i < 10; i++) {
            if(isOutOfRange(x+i,y-i)) { break; }
            if(!isOccupied(board, x+i, y-i)) {
                moves.add(board[y-i][x+i]);
            }
            if(isOccupied(board,x+i,y-i)) {
                if (isTeammate(board,x+i,y-i, Chesspiece.this.color)) { break; }
                else {
                    moves.add(board[y-i][x+i]);
                    break;
                }
            }
        }

        /**
         * moves with direction: top left
         */
        for (int i = 1; i < 10; i++) {
            if(isOutOfRange(x-i,y-i)) { break; }
            if(!isOccupied(board, x-i, y-i)) {
                moves.add(board[y-i][x-i]);
            }
            if(isOccupied(board,x-i,y-i)) {
                if (isTeammate(board,x-i,y-i, Chesspiece.this.color)) { break; }
                else {
                    moves.add(board[y-i][x-i]);
                    break;
                }
            }
        }

        /**
         * moves with direction: bot left
         */
        for (int i = 1; i < 10; i++) {
            if(isOutOfRange(x-i,y+i)) { break; }
            if(!isOccupied(board, x-i, y+i)) { moves.add(board[y+i][x-i]); }
            if(isOccupied(board,x-i,y+i)) {
                if (isTeammate(board,x-i,y+i, Chesspiece.this.color)) { break; }
                else { moves.add(board[y+i][x-i]); break; }
            }
        }

        /**
         * moves with direction: bot right
         */
        for (int i = 1; i < 10; i++) {
            if(isOutOfRange(x+i,y+i)) { break; }
            if(!isOccupied(board, x+i, y+i)) { moves.add(board[y+i][x+i]); }
            if(isOccupied(board,x+i,y+i)) {
                if (isTeammate(board,x+i,y+i, Chesspiece.this.color)) { break; }
                else { moves.add(board[y+i][x+i]); break; }
            }
        }

    }

    /**
     * fills inserted ArrayList<Tile> with legal moves for Chesspiece of type Rook
     * @param color color of Chesspiece
     * @param x x location of Chesspiece
     * @param y y location of Chesspiece
     * @param board board of Tiles
     * @param moves ArrayList<Tile> of moves for Chesspiece. Can be empty or contain some data
     */
    public void rookMoves(String color, int x , int y, Tile[][] board, ArrayList<Tile> moves) {
        /**
         * moves with direction: horizontal right
         */
        for (int i = 0; i < 8; i++) {
            if(i == 0) { continue; }
            if(isOutOfRange(x+i,y)) {
                break;
            } else if (isOccupied(board, x+i, y) && isTeammate(board, x+i,y,color)) {
                break;
            } else if(!isOccupied(board, x+i, y)) {
                moves.add(board[y][x+i]);
            } else if (isOccupied(board, x+i, y) && !isTeammate(board, x+i, y, color)) {
                moves.add(board[y][x+i]);
                break;
            }
        }

        /**
         * moves with direction: horizontal left
         */
        for (int i = 0; i < 8; i++) {
            if(i == 0) { continue; }
            if(isOutOfRange(x-i,y)) {
                break;
            } else if (isOccupied(board, x-i, y) && isTeammate(board, x-i,y, color)) {
                break;
            } else if(!isOccupied(board, x-i, y)) {
                moves.add(board[y][x-i]);
            } else if (isOccupied(board, x-i, y) && !isTeammate(board, x-i, y, color)) {
                moves.add(board[y][x-i]);
                break;
            }
        }

        /**
         * moves with direction: vertical up
         */
        for (int i = 0; i < 8; i++) {
            if(i == 0) { continue; }
            if(isOutOfRange(x,y-i)) {
                break;
            } else if (isOccupied(board, x, y-i) && isTeammate(board, x,y-i,color)) {
                break;
            } else if(!isOccupied(board, x, y-i)) {
                moves.add(board[y-i][x]);
            } else if (isOccupied(board, x,y-i) && !isTeammate(board, x,y-i, color)) {
                moves.add(board[y-i][x]);
                break;
            }
        }

        /**
         * moves with direction: vertical down
         */
        for (int i = 1; i < 8; i++) {
            if(isOutOfRange(x,y+i)) {
                break;
            } else if (isOccupied(board, x, y+i) && isTeammate(board, x,y+i, color)) {
                break;
            } else if(!isOccupied(board, x, y+i)) {
                moves.add(board[y+i][x]);
            } else if (isOccupied(board, x,y+i) && !isTeammate(board, x,y+i, color)) {
                moves.add(board[y+i][x]);
                break;
            }
        }
    }

    /**
     * fills inserted ArrayList<Tile> with legal moves for Chesspiece of type Bishop
     * @param color color of Chesspiece
     * @param x x location of Chesspiece
     * @param y y location of Chesspiece
     * @param board board of Tiles
     * @param moves ArrayList<Tile> of moves for Chesspiece. Can be empty or contain some data
     * @param startingPosition boolean checking if Pawn was moved
     */
    public void pawnMoves(String color, int x , int y, Tile[][] board, ArrayList<Tile> moves, boolean startingPosition) {
        /**
         * if statement chooses how the array of legal moves will be filled
         * black figures: y coordinate +
         * white figures: y coordinate -
         */
        if(color.equals("White")) {
            /**
             * if starting position is true, Pawn can be moved +1 or +2 in the straight direction
             * if starting position is false, Pawn can be moved +1
             */
            if (startingPosition) {
                if(board[y-1][x].currentChessPiece == null) {
                    moves.add(board[y-1][x]);
                }
                if(board[y-2][x].currentChessPiece == null) {
                    moves.add(board[y-2][x]);
                }
            } else {
                if(!isOutOfRange(x, y-1) && !isOccupied(board, x, y-1)) {
                    moves.add(board[y-1][x]);
                }
            }

            /**
             * moves for taking enemy figures
             */
            if(!isOutOfRange(x+1, y-1)) {
                if(isOccupied(board,x+1, y-1) && !isTeammate(board, x+1, y-1, currentPosition.getCurrentChessPiece().getColor())) {
                    moves.add(board[y-1][x+1]);
                }
            }

            if(!isOutOfRange(x-1, y-1)) {
                if(isOccupied(board,x-1, y-1) && !isTeammate(board, x-1, y-1, currentPosition.getCurrentChessPiece().getColor())) {
                    moves.add(board[y-1][x-1]);
                }
            }

        } else {
            /**
             * if starting position is true, Pawn can be moved +1 or +2 in the straight direction
             * if starting position is false, Pawn can be moved +1
             */
            if (startingPosition) {
                if (board[y+1][x].currentChessPiece == null) {
                    moves.add(board[y+1][x]);
                }
                if (board[y - 2][x].currentChessPiece == null) {
                    moves.add(board[y+2][x]);
                }
            } else {
                if (!isOutOfRange(x, y+1) && !isOccupied(board, x, y+1)) {
                    moves.add(board[y+1][x]);
                }
            }

            /**
             * moves for taking enemy figures
             */
            if (!isOutOfRange(x + 1, y+1)) {
                if (isOccupied(board, x + 1, y+1) && !isTeammate(board, x + 1, y+1, currentPosition.getCurrentChessPiece().getColor())) {
                    moves.add(board[y+1][x + 1]);
                }
            }

            if (!isOutOfRange(x - 1, y+1)) {
                if (isOccupied(board, x - 1, y+1) && !isTeammate(board, x - 1, y+1, currentPosition.getCurrentChessPiece().getColor())) {
                    moves.add(board[y+1][x - 1]);
                }
            }
        }
    }
}
