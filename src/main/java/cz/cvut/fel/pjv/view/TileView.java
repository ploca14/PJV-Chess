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
    private boolean isLegalMove;

    public TileView(Tile tileModel) {
        super();
        this.tileModel = tileModel;

        renderTile();
    }

    private void renderTile() {
        if (tileModel.getCurrentChessPiece() != null) {
            chessPieceText.setText(tileModel.getCurrentChessPiece().toString());
            chessPieceText.setFill(tileModel.getCurrentChessPiece().getColor().getPaintColor());
        }
        chessPieceText.setFont(Font.font(50));

        DropShadow dropShadow = new DropShadow(2.5, Color.rgb(32,32,32));
        chessPieceText.setEffect(dropShadow);

        rectangle.setFill(tileModel.getColor().getPaintColor());
        rectangle.setHeight(75); // Should not be a magic number
        rectangle.setWidth(75); // Should not be a magic number

        circle.setRadius(15);
        circle.setFill(Color.GREEN);

        this.getChildren().addAll(rectangle, chessPieceText);
    }

    public Tile getTileModel() {
        return tileModel;
    }

    public boolean isLegalMove() {
        return isLegalMove;
    }

    public void setLegalMove(boolean isLegalMove) {
        this.isLegalMove = isLegalMove;

        showLegalMove();
    }

    public void showLegalMove() {
        if (isLegalMove) {
            if (this.getChildren().contains(circle)) return;
            this.getChildren().add(circle);
        } else {
            this.getChildren().remove(circle);
        }
    }

    public void rerender() {
        if (tileModel.getCurrentChessPiece() == null) {
            chessPieceText.setText(null);
        } else {
            chessPieceText.setText(tileModel.getCurrentChessPiece().toString());
            chessPieceText.setFill(tileModel.getCurrentChessPiece().getColor().getPaintColor());
        }
    }
}