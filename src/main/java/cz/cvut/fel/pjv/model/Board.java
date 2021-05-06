package cz.cvut.fel.pjv.model;

import cz.cvut.fel.pjv.model.chestpieces.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {

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

        // Creating board
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board[y][x] = new Tile(x, y);
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
            board[1][i].setCurrentChessPiece(new Pawn(Color.BLACK,board[1][i]));
            blackPieces.add(board[1][i].getCurrentChessPiece());
            board[6][i].setCurrentChessPiece(new Pawn(Color.WHITE,board[6][i]));
            whitePieces.add(board[6][i].getCurrentChessPiece());
        }

        /**
         * placement of Rook Chesspieces
         */
        board[0][0].setCurrentChessPiece(new Rook(Color.BLACK,board[0][0]));
        blackPieces.add(board[0][0].getCurrentChessPiece());
        board[0][7].setCurrentChessPiece(new Rook(Color.BLACK,board[0][7]));
        blackPieces.add(board[0][7].getCurrentChessPiece());

        board[7][0].setCurrentChessPiece(new Rook(Color.WHITE,board[7][0]));
        whitePieces.add(board[7][0].getCurrentChessPiece());
        board[7][7].setCurrentChessPiece(new Rook(Color.WHITE,board[7][7]));
        whitePieces.add(board[7][7].getCurrentChessPiece());

        /**
         * placement of Knight Chesspieces
         */
        board[0][1].setCurrentChessPiece(new Knight(Color.BLACK,board[0][1]));
        blackPieces.add(board[0][1].getCurrentChessPiece());
        board[0][6].setCurrentChessPiece(new Knight(Color.BLACK,board[0][6]));
        blackPieces.add(board[0][6].getCurrentChessPiece());

        board[7][1].setCurrentChessPiece(new Knight(Color.WHITE,board[7][1]));
        whitePieces.add(board[7][1].getCurrentChessPiece());
        board[7][6].setCurrentChessPiece(new Knight(Color.WHITE,board[7][6]));
        whitePieces.add(board[7][6].getCurrentChessPiece());

        /**
         * placement of Bishop Chesspieces
         */
        board[0][2].setCurrentChessPiece(new Bishop(Color.BLACK,board[0][2]));;
        blackPieces.add(board[0][2].getCurrentChessPiece());
        board[0][5].setCurrentChessPiece(new Bishop(Color.BLACK,board[0][5]));;
        blackPieces.add(board[0][5].getCurrentChessPiece());

        board[7][2].setCurrentChessPiece(new Bishop(Color.WHITE,board[7][2]));;
        whitePieces.add(board[7][2].getCurrentChessPiece());
        board[7][5].setCurrentChessPiece(new Bishop(Color.WHITE,board[7][5]));;
        whitePieces.add(board[7][5].getCurrentChessPiece());

        /**
         * placement of Queen Chesspieces
         */
        board[0][3].setCurrentChessPiece(new Queen(Color.BLACK,board[0][3]));
        blackPieces.add(board[0][3].getCurrentChessPiece());

        board[7][3].setCurrentChessPiece(new Queen(Color.WHITE,board[7][3]));
        whitePieces.add(board[7][3].getCurrentChessPiece());

        /**
         * placement of King Chesspieces
         */
        board[0][4].setCurrentChessPiece(new King(Color.BLACK,board[0][4]));
        blackPieces.add(board[0][4].getCurrentChessPiece());

        board[7][4].setCurrentChessPiece(new King(Color.WHITE,board[7][4]));
        whitePieces.add(board[7][4].getCurrentChessPiece());
    }

    public void removePiece(Chesspiece currentChessPiece) {
        if (currentChessPiece.getColor().equals(Color.WHITE)) {
            whitePieces.remove(currentChessPiece);
        } else {
            blackPieces.remove(currentChessPiece);
        }
    }
}
