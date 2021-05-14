package cz.cvut.fel.pjv.model.player;

import cz.cvut.fel.pjv.controller.GameRules;
import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.ChessPieceFactory;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.model.chestpieces.Tile;
import cz.cvut.fel.pjv.view.TileView;

import java.util.ArrayList;
import java.util.Random;

public class Ai {
    private final Random random = new Random();
    private final GameRules gameRules;

    public Ai(GameRules gameRules) {
        this.gameRules = gameRules;
    }

    public Move chooseRandomMove(Color color, Board board) {
        ArrayList<Chesspiece> movedArray;

        if(color.equals(Color.WHITE)) {
            movedArray = board.getWhitePieces();
        } else {
            movedArray = board.getBlackPieces();
        }

        int i = index(movedArray);
        Chesspiece cp = movedArray.get(i);

        ArrayList<Tile> legalMoves = gameRules.getLegalNotCheckMoves(cp);

        while (legalMoves.size() == 0) {
            cp = movedArray.get(index(movedArray));
            legalMoves = gameRules.getLegalNotCheckMoves(cp);
        }

        i = index(legalMoves);
        Tile move = legalMoves.get(i);

        return new Move(cp.getCurrentPosition(), move);
   }

   private int index(ArrayList<?> arrayList) {
       int bot = 0;
       int top = arrayList.size();
       int i = random.nextInt(top-bot) + bot;
       return i;
   }

    public Chesspiece chooseRandomPiece(Tile forTile, Color color, ChessPieceFactory chessPieceFactory) {
        return switch (random.nextInt(4)) {
            case 0 -> chessPieceFactory.createKnight(color, forTile);
            case 1 -> chessPieceFactory.createBishop(color, forTile);
            case 2 -> chessPieceFactory.createRook(color, forTile);
            case 3 -> chessPieceFactory.createQueen(color, forTile);
            default -> null;
        };
    }
}
