package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.controller.Board;
import cz.cvut.fel.pjv.model.chestpieces.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    public static void main(String[] args) {
        Board b = new Board();
        b.placeChessPieces();
        Tile[][] board = b.getBoard();
        Knight k = new Knight("Black",board[0][1]);
        ArrayList<Tile> rookList = k.getLegalMoves(k.getCurrentPosition(), board);
        System.out.println(rookList);
    }
}
