package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.controller.AbstractBoardController;
import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Tile;
import cz.cvut.fel.pjv.view.BoardView;

public class ServerBoardController extends AbstractBoardController {

    /**
     * @param boardModel The board model this controller will be updating
     * @param boardView The board view this controller will be rerendering
     */
    public ServerBoardController(Board boardModel, BoardView boardView) {
        super(boardModel, boardView);
    }

    /**
     * This method is used to clear the current chess piece if there is one and add the chess piece to the board model and set it to the selected tile
     * @param forTile the tile that the chesspiece should be set to
     * @param chosenPiece the chesspiece to set
     */
    @Override
    public void setChosenChesspiece(Tile forTile, Chesspiece chosenPiece) {
        if (forTile.getCurrentChessPiece() != null) {
            // If it is occupied then remove the occupying piece
            getBoardModel().removePiece(forTile.getCurrentChessPiece());
            forTile.setCurrentChessPiece(null);
        }

        getBoardModel().addPiece(chosenPiece);

        // Set the picked piece position to this tile
        forTile.setCurrentChessPiece(chosenPiece);
    }


}
