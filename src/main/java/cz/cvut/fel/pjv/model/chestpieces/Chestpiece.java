package cz.cvut.fel.pjv.model.chestpieces;

public interface Chestpiece {
    Integer[][] possibleMoves();
    Integer[] move(Integer[] possibleMove);
}
