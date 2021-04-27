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

    public abstract ArrayList<Tile> getLegalMoves(Tile currentPosition, Tile[][] board);


    public Tile getCurrentPosition() {
        return currentPosition;
    }

    public String getColor() {
        return color;
    }

    public boolean isOccupied(Tile[][]board, int x, int y) {
        return board[y][x].currentChessPiece != null;
    }

    public boolean isOutOfRange(int x, int y) {
        return (x > 7 || x < 0 || y > 7 || y < 0);
    }

    public boolean isTeammate(Tile[][] b, int x, int y, String color) {
        return (b[y][x].currentChessPiece.getColor()).equals(color);
    }

    public void knightMoves(String color, int x , int y, Tile[][] board, ArrayList<Tile> moves) {


        // all combinations:
        // x+1 y-2, x+2 y-1...
        int[] xs = {x+1, x+2, x+2, x+1, x-1, x-2, x-2, x-1};
        int[] ys = {y-2, y-1, y+1, y+2, y+2, y+1, y-1, y-2};

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
    public void bishopMoves(String color, int x , int y, Tile[][] board, ArrayList<Tile> moves) {
        // top right direction
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

        // top left direction
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

        // bot left direction
        for (int i = 1; i < 10; i++) {
            if(isOutOfRange(x-i,y+i)) { break; }
            if(!isOccupied(board, x-i, y+i)) { moves.add(board[y+i][x-i]); }
            if(isOccupied(board,x-i,y+i)) {
                if (isTeammate(board,x-i,y+i, Chesspiece.this.color)) { break; }
                else { moves.add(board[y+i][x-i]); break; }
            }
        }

        // bot right direction
        for (int i = 1; i < 10; i++) {
            if(isOutOfRange(x+i,y+i)) { break; }
            if(!isOccupied(board, x+i, y+i)) { moves.add(board[y+i][x+i]); }
            if(isOccupied(board,x+i,y+i)) {
                if (isTeammate(board,x+i,y+i, Chesspiece.this.color)) { break; }
                else { moves.add(board[y+i][x+i]); break; }
            }
        }

    }
    public void rookMoves(String color, int x , int y, Tile[][] board, ArrayList<Tile> moves) {

        // horizontal right
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

        // horizontal left
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
        // vertical up
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

        // vertical down
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
    public void pawnMoves(String color, int x , int y, Tile[][] board, ArrayList<Tile> moves, boolean startingPosition) {
        if(color.equals("White")) {
            // startingPostition moves are different then notStarting
            // if checks starting position
            // moves when not taking enemy figure
            if (startingPosition) {
                if(board[y-1][x].currentChessPiece == null) { moves.add(board[y-1][x]); }
                if(board[y-2][x].currentChessPiece == null) { moves.add(board[y-2][x]); }
            } else {
                // first condition checks if Tile is not occupied, second checks if its on board
                if(!isOutOfRange(x, y-1) && !isOccupied(board, x, y-1)) {
                    moves.add(board[y-1][x]);
                }
            }

            // taking enemy figure {x+1, y-1}, {x-1, y-1}
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
            // startingPostition moves are different then notStarting
            // if checks starting position
            // moves when not taking enemy figure
            if (startingPosition) {
                if (board[y+1][x].currentChessPiece == null) {
                    moves.add(board[y+1][x]);
                }
                if (board[y - 2][x].currentChessPiece == null) {
                    moves.add(board[y+2][x]);
                }
            } else {
                // first condition checks if Tile is not occupied, second checks if its on board
                if (!isOutOfRange(x, y+1) && !isOccupied(board, x, y+1)) {
                    moves.add(board[y+1][x]);
                }
            }

            // taking enemy figures
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
