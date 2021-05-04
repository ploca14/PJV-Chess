package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.chestpieces.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Board b = new Board();
        b.placeChessPieces();
        Tile[][] board = b.getBoard();
        Queen k = new Queen(Color.WHITE,board[7][3]);
        //k.move();
        board[7][3].setCurrentChessPiece(k);
        ArrayList<Tile> queenMovesList = k.getLegalMoves(k.getCurrentPosition(), board);
        System.out.println(queenMovesList);
    }
}
