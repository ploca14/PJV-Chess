package cz.cvut.fel.pjv.model.chestpieces;

import cz.cvut.fel.pjv.model.Board;

import java.util.ArrayList;

public class ChessPieceFactory {
    private Board board;

    public ChessPieceFactory(Board board) {
        this.board = board;
    }

    private int getFrequency(Color color, Class<? extends Chesspiece> pieceClass) {
        ArrayList<Chesspiece> chesspieces;
        if (color.equals(Color.WHITE)) {
            chesspieces = board.getWhitePieces();
        } else {
            chesspieces = board.getBlackPieces();
        }

        int frequency = 0;
        for (Chesspiece chesspiece:
                chesspieces) {
            if (chesspiece.getClass().equals(pieceClass)) {
                frequency++;
            }
        }
        return frequency;
    }

    public Pawn createPawn(Color color, Tile currentPosition) {
        int pawnCount = getFrequency(color, Pawn.class);
        if (pawnCount >= 8) return null;

        return new Pawn(color, currentPosition);
    }

    public Knight createKnight(Color color, Tile currentPosition) {
        int knightCount = getFrequency(color, Knight.class);
        if (knightCount >= 10) return null;

        return new Knight(color, currentPosition);
    }

    public Bishop createBishop(Color color, Tile currentPosition) {
        int bishopCount = getFrequency(color, Bishop.class);
        if (bishopCount >= 10) return null;

        return new Bishop(color, currentPosition);
    }

    public Rook createRook(Color color, Tile currentPosition) {
        int rookCount = getFrequency(color, Rook.class);
        if (rookCount >= 10) return null;

        return new Rook(color, currentPosition);
    }

    public Queen createQueen(Color color, Tile currentPosition) {
        int queenCount = getFrequency(color, Queen.class);
        if (queenCount >= 9) return null;

        return new Queen(color, currentPosition);
    }

    public King createKing(Color color, Tile currentPosition) {
        int kingCount = getFrequency(color, King.class);
        if (kingCount >= 1) return null;

        return new King(color, currentPosition);
    }
}
