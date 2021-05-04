package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.Board;
import cz.cvut.fel.pjv.model.chestpieces.Tile;
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

    public void rerenderBoard() {
        this.getChildren().clear();
        renderBoard();;
    }
}
