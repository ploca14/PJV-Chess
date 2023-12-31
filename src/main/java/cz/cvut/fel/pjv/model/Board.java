package cz.cvut.fel.pjv.model;

import cz.cvut.fel.pjv.model.chestpieces.*;
import cz.cvut.fel.pjv.model.chestpieces.Color;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {

    /**
     * representation of board and black/white chesspieces
     */
    private Tile[][] board;
    private boolean isEditable = false;
    public ArrayList<Chesspiece> whitePieces;
    public ArrayList<Chesspiece> blackPieces;
    private final Game game;

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public ArrayList<Chesspiece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<Chesspiece> getBlackPieces() {
        return blackPieces;
    }

    public void setWhitePieces(ArrayList<Chesspiece> whitePieces) {
        this.whitePieces = whitePieces;
    }

    public void setBlackPieces(ArrayList<Chesspiece> blackPieces) {
        this.blackPieces = blackPieces;
    }

    public Game getGame() {
        return game;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    /**
     * board ctor
     */
    public Board(Game game) {
        this.game = game;
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
            Pawn newBlackPawn = new Pawn(Color.BLACK,board[1][i]);
            newBlackPawn.setPlayingBoard(this);
            board[1][i].setCurrentChessPiece(newBlackPawn);
            blackPieces.add(board[1][i].getCurrentChessPiece());

            Pawn newWhitePawn = new Pawn(Color.WHITE,board[6][i]);
            newWhitePawn.setPlayingBoard(this);
            board[6][i].setCurrentChessPiece(newWhitePawn);
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

    /**
     * This method is used to remove a chesspiece from the board
     * @param chesspiece The chesspiece to remove from the board
     */
    public void removePiece(Chesspiece chesspiece) {
        if ( chesspiece.getColor().equals(Color.WHITE)) {
            whitePieces.remove(chesspiece);
        } else {
            blackPieces.remove(chesspiece);
        }
    }

    /**
     * This method is used to add a new chesspiece to the board
     * @param chesspiece The chess piece to add to the board
     */
    public void addPiece(Chesspiece chesspiece) {
        if (chesspiece.getColor().equals(Color.WHITE)) {
            whitePieces.add(chesspiece);
        } else {
            blackPieces.add(chesspiece);
        }
    }

    public int getNumberOfKings() {
        ArrayList<Chesspiece> chesspieces = new ArrayList<>();
        chesspieces.addAll(whitePieces);
        chesspieces.addAll(blackPieces);

        int frequency = 0;
        for (Chesspiece chesspiece:
                chesspieces) {
            if (chesspiece.getClass().equals(King.class)) {
                frequency++;
            }
        }
        return frequency;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Tile[] tileRow: board) {
            for (Tile tile: tileRow) {
                if (tile.currentChessPiece != null) {
                    if (tile.currentChessPiece.getColor().equals(Color.BLACK)) {
                        stringBuilder.append("\u001B[31m");
                    } else {
                        stringBuilder.append("\u001B[0m");
                    }
                    stringBuilder.append(tile.currentChessPiece.toString());
                }
                else {
                    stringBuilder.append(" ");
                }
                stringBuilder.append("\t");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
