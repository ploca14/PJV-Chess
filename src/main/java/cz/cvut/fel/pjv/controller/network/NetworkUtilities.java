package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.Tile;

public class NetworkUtilities {

    /**
     * This method is used to translate a move received from the network to the local board
     * @param moveObject The original move object
     * @param board The local board to which we want to translate the move
     * @return The translated move
     */
    public static Move parseMove(Object moveObject, Board board) {
        Move receivedMove = (Move) moveObject;
        Tile receivedStartingPosition = receivedMove.getStartingPosition();
        Tile receivedEndingPosition = receivedMove.getEndingPosition();

        Tile startingPosition = board.getBoard()[receivedStartingPosition.getY()][receivedStartingPosition.getX()];
        Tile endingPosition = board.getBoard()[receivedEndingPosition.getY()][receivedEndingPosition.getX()];
        return new Move(startingPosition, endingPosition);
    }
}
