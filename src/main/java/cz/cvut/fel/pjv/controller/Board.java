package cz.cvut.fel.pjv.controller;

import java.awt.desktop.AboutEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Board {
    // empty Board Tile: 0

    // white Pawn: 1
    // white Knight: 2
    // white Bishop: 3
    // white Rook: 4
    // white Queen: 5
    // white King: 6

    // black Pawn: -1
    // black Knight: -2
    // black Bishop: -3
    // black Rook: -4
    // black Queen: -5
    // black King: -6

    // default board: white figures down, black figures up
    int[][] board = {
            {-4, -2, -3, -5, -6, -3, -2, -4},
            {-1, -1, -1, -1, -1, -1, -1, -1},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {4, 2, 3, 5, 6, 3, 2, 4}
    };

    public Board() {

    }

    // preset board from PGN code
    public void presetBoard(String board) {
        // PGN => board
    }

    // turn board if player selects black figures (vs computer)
    public void turnBoard() {
        Collections.reverse(Arrays.asList(board));

        int queen = board[0][3];
        int king = board[0][4];
        board[0][4] = queen;
        board[0][3] = king;

        queen = board[7][3];
        king = board[7][4];
        board[7][3] = king;
        board[7][4] = queen;
    }
}
