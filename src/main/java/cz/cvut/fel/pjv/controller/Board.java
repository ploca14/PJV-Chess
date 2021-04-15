package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.chestpieces.*;

import java.util.ArrayList;

public class Board {

    private Tile[][] board;

    public Board() {
        // board representation, white pieces representation, black pieces representation
        board = new Tile[8][8];

        // coloring tiles
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int xMod = x % 2;
                int yMod = y % 2;

                if ((xMod == 0 && yMod == 0) || (xMod == 1 && yMod == 1)) {
                    board[x][y] = new Tile( 1, y, x);
                } else {
                    board[x][y] = new Tile( 0, y, x);
                }
            }
        }

    }

    public void placeChessPieces() {
        // pawn placements, first blacks, then whites
        for (int i = 0; i < 8; i++) {
            board[6][i].setCurrentChessPiece(new Pawn("Black",board[6][i]));
            board[1][i].setCurrentChessPiece(new Pawn("White",board[1][i]));
        }

        // rook placements, first blacks, then whites
        board[0][0].setCurrentChessPiece(new Rook("Black",board[0][0]));
        board[0][7].setCurrentChessPiece(new Rook("Black",board[0][7]));

        board[7][0].setCurrentChessPiece(new Rook("White",board[7][0]));
        board[7][7].setCurrentChessPiece(new Rook("White",board[7][7]));

        // knight placements, first blacks, then whites
        board[0][1].setCurrentChessPiece(new Knight("Black",board[0][1]));
        board[0][6].setCurrentChessPiece(new Knight("Black",board[0][6]));

        board[7][1].setCurrentChessPiece(new Knight("White",board[7][1]));
        board[7][6].setCurrentChessPiece(new Knight("White",board[7][6]));

        // bishop placements, first blacks, then whites
        board[0][2].setCurrentChessPiece(new Bishop("Black",board[0][2]));;
        board[0][5].setCurrentChessPiece(new Bishop("Black",board[0][5]));;

        board[7][2].setCurrentChessPiece(new Bishop("White",board[7][2]));;
        board[7][5].setCurrentChessPiece(new Bishop("White",board[7][5]));;

        // queen placements, first black, then white
        board[0][3].setCurrentChessPiece(new Queen("Black",board[0][3]));

        board[7][3].setCurrentChessPiece(new Queen("White",board[7][3]));

        // king placements, first black, then white
        board[0][4].setCurrentChessPiece(new King("Black",board[0][4]));

        board[7][4].setCurrentChessPiece(new King("White",board[7][4]));
    }
}
