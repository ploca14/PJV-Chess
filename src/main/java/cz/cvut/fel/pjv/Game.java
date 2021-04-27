package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.controller.Board;
import cz.cvut.fel.pjv.model.chestpieces.*;

import java.util.ArrayList;

public class Game {
    public static void main(String[] args) {
        Board b = new Board();
        b.placeChessPieces();
        Tile[][] board = b.getBoard();
        King k = new King("Black",board[4][2]);
        k.move();
        board[4][2].setCurrentChessPiece(k);
        ArrayList<Tile> rookList = k.getLegalMoves(k.getCurrentPosition(), board);
        System.out.println(rookList);
    }
}
