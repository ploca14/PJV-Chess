package cz.cvut.fel.pjv.model.player;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.model.chestpieces.Tile;

import java.util.ArrayList;
import java.util.Random;

public class Ai extends Board {

    public Ai(Game game) {
        super(game);
    }

    public Tile chooseRandomMove(Color color, Board board) {
        ArrayList<Chesspiece> movedArray;

        if(color.equals(Color.WHITE)) {
            movedArray = getWhitePieces();
        } else {
            movedArray = getBlackPieces();
        }

        int i = index(movedArray);
        Chesspiece cp = movedArray.get(i);

        ArrayList<Tile> legalMoves = cp.getLegalMoves(cp.getCurrentPosition(), board);
        i = index(legalMoves);
        Tile move = legalMoves.get(i);

        return move;
   }

   public int index(ArrayList<?> arrayList) {
       Random r = new Random();
       int bot = 0;
       int top = arrayList.size();
       int i = r.nextInt(top-bot) + bot;
       return i;
   }
}
