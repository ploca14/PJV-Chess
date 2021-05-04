package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.chestpieces.*;

import java.util.ArrayList;

public class Board {

    /**
     * representation of board and black/white chesspieces
     */
    private Tile[][] board;
    public ArrayList<Chesspiece> whitePieces;
    public ArrayList<Chesspiece> blackPieces;

    public ArrayList<Chesspiece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<Chesspiece> getBlackPieces() {
        return blackPieces;
    }

    public Tile[][] getBoard() {
        return board;
    }

    /**
     * board ctor
     */
    public Board() {
        board = new Tile[8][8];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();

        /**
         * coloring tiles
         */
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

    /**
     * placing Chesspieces
     */
    public void placeChessPieces() {
        /**
         * placement of Pawn Chesspieces
         */
        for (int i = 0; i < 8; i++) {
            board[1][i].setCurrentChessPiece(new Pawn("Black",board[1][i]));
            blackPieces.add(board[1][i].getCurrentChessPiece());
            board[6][i].setCurrentChessPiece(new Pawn("White",board[6][i]));
            whitePieces.add(board[6][i].getCurrentChessPiece());
        }

        /**
         * placement of Rook Chesspieces
         */
        board[0][0].setCurrentChessPiece(new Rook("Black",board[0][0]));
        blackPieces.add(board[0][0].getCurrentChessPiece());
        board[0][7].setCurrentChessPiece(new Rook("Black",board[0][7]));
        blackPieces.add(board[0][7].getCurrentChessPiece());

        board[7][0].setCurrentChessPiece(new Rook("White",board[7][0]));
        whitePieces.add(board[7][0].getCurrentChessPiece());
        board[7][7].setCurrentChessPiece(new Rook("White",board[7][7]));
        whitePieces.add(board[7][7].getCurrentChessPiece());

        /**
         * placement of Knight Chesspieces
         */
        board[0][1].setCurrentChessPiece(new Knight("Black",board[0][1]));
        blackPieces.add(board[0][1].getCurrentChessPiece());
        board[0][6].setCurrentChessPiece(new Knight("Black",board[0][6]));
        blackPieces.add(board[0][6].getCurrentChessPiece());

        board[7][1].setCurrentChessPiece(new Knight("White",board[7][1]));
        whitePieces.add(board[7][1].getCurrentChessPiece());
        board[7][6].setCurrentChessPiece(new Knight("White",board[7][6]));
        whitePieces.add(board[7][6].getCurrentChessPiece());

        /**
         * placement of Bishop Chesspieces
         */
        board[0][2].setCurrentChessPiece(new Bishop("Black",board[0][2]));;
        blackPieces.add(board[0][2].getCurrentChessPiece());
        board[0][5].setCurrentChessPiece(new Bishop("Black",board[0][5]));;
        blackPieces.add(board[0][5].getCurrentChessPiece());

        board[7][2].setCurrentChessPiece(new Bishop("White",board[7][2]));;
        whitePieces.add(board[7][2].getCurrentChessPiece());
        board[7][5].setCurrentChessPiece(new Bishop("White",board[7][5]));;
        whitePieces.add(board[7][5].getCurrentChessPiece());

        /**
         * placement of Queen Chesspieces
         */
        board[0][3].setCurrentChessPiece(new Queen("Black",board[0][3]));
        blackPieces.add(board[0][3].getCurrentChessPiece());

        board[7][3].setCurrentChessPiece(new Queen("White",board[7][3]));
        whitePieces.add(board[7][3].getCurrentChessPiece());

        /**
         * placement of King Chesspieces
         */
        board[0][4].setCurrentChessPiece(new King("Black",board[0][4]));
        blackPieces.add(board[0][4].getCurrentChessPiece());

        board[7][4].setCurrentChessPiece(new King("White",board[7][4]));
        whitePieces.add(board[7][4].getCurrentChessPiece());
    }
}
