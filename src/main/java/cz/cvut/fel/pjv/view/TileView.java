package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.chestpieces.Tile;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TileView extends StackPane {
    private final Tile tileModel;
    private final Text chessPieceText = new Text();
    private final Rectangle rectangle = new Rectangle();
    private final Circle circle = new Circle();

    public TileView(Tile tileModel) {
        super();
        this.tileModel = tileModel;

        createTile();
    }

    private void createTile() {
        //chessPieceText.setText(tileModel.getY() + ", " + tileModel.getX());
        if (tileModel.getCurrentChessPiece() != null) {
            chessPieceText.setText(tileModel.getCurrentChessPiece().toString());
            chessPieceText.setFill(tileModel.getCurrentChessPiece().getColor());
        }
        chessPieceText.setFont(Font.font(50));

        DropShadow dropShadow = new DropShadow(2.5, Color.rgb(32,32,32));
        chessPieceText.setEffect(dropShadow);

        rectangle.setFill(tileModel.getColor());
        rectangle.setHeight(75); // Should not be a magic number
        rectangle.setWidth(75); // Should not be a magic number

        circle.setRadius(15);
        circle.setFill(Color.GREEN);

        this.getChildren().addAll(rectangle, chessPieceText);
    }

    public Tile getTileModel() {
        return tileModel;
    }

    public void showLegalMode(boolean shouldLegalMoveBeRendered) {
        if (shouldLegalMoveBeRendered) {
            if (this.getChildren().contains(circle)) return;
            this.getChildren().add(circle);
        } else {
            this.getChildren().remove(circle);
        }
    }
}