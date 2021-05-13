package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.chestpieces.Tile;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class BoardView extends GridPane {
    private final Board boardModel;

    public BoardView(Board boardModel) {
        this.boardModel = boardModel;

        renderBoard();
    }

    private void renderBoard() {
        Tile[][] tiles = boardModel.getBoard();

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                TileView tileView = new TileView(tiles[y][x]);
                this.add(tileView, x, y);
            }
        }
    }

    /**
     * This method is used to rerender all the tiles in the board
     */
    public void rerenderBoard() {
        for (Node node:
                this.getChildren()) {
            ((TileView) node).rerender();
        }
    }
    public TileView getNodeByRowColumnIndex (final int row, final int column) {
        Node result = null;
        ObservableList<Node> childrens = this.getChildren();

        for (Node node : childrens) {
            if(this.getRowIndex(node) == row && this.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return (TileView)result;
    }
}
