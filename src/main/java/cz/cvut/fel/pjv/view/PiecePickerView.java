package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.chestpieces.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class PiecePickerView extends Stage {
    Button addPawn = new Button("\u265F");
    Button addKnight = new Button("\u265E");
    Button addBishop = new Button("\u265D");
    Button addRook = new Button("\u265C");
    Button addQueen = new Button("\u265B");
    Button addKing = new Button("\u265A");
    Chesspiece chosenPiece;
    private TileView tile;
    private Color pieceColor;
    private ChessPieceFactory chessPieceFactory;

    public PiecePickerView() {
        super(StageStyle.UNDECORATED);

        this.setTitle("Pick a chesspiece");
        this.initModality(Modality.WINDOW_MODAL);
    }

    private Scene createScene(boolean showAll) {
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(10));

        addPawn.setOnAction((event) -> {
            chosenPiece = chessPieceFactory.createPawn(pieceColor, tile.getTileModel());
            this.close();
        });
        addKnight.setOnAction((event) -> {
            chosenPiece = chessPieceFactory.createKnight(pieceColor, tile.getTileModel());
            this.close();
        });
        addBishop.setOnAction((event) -> {
            chosenPiece = chessPieceFactory.createBishop(pieceColor, tile.getTileModel());
            this.close();
        });
        addRook.setOnAction((event) -> {
            chosenPiece = chessPieceFactory.createRook(pieceColor, tile.getTileModel());
            this.close();
        });
        addQueen.setOnAction((event) -> {
            chosenPiece = chessPieceFactory.createQueen(pieceColor, tile.getTileModel());
            this.close();
        });
        addKing.setOnAction((event) -> {
            chosenPiece = chessPieceFactory.createKing(pieceColor, tile.getTileModel());
            this.close();
        });

        if (showAll) { hBox.getChildren().add(addPawn); }
        hBox.getChildren().addAll(addKnight, addBishop, addRook, addQueen);
        if (showAll) { hBox.getChildren().add(addKing); }
        for (Node child:
             hBox.getChildren()) {
            Button button = (Button) child;
            button.setFont(new Font(50));
        }

        Scene scene = new Scene(hBox);
        return scene;
    }

    /**
     * @param forTileView The tile for which you want to choose a new piece
     * @param color The color of the new piece
     * @param showAll Whether to show all the pieces (true) or just the pieces for pawn promotion (false)
     * @return The chosen chess piece
     */
    public Chesspiece showPickDialog(TileView forTileView, Color color, boolean showAll) {
        tile = forTileView;
        this.pieceColor = color;
        this.setScene(createScene(showAll));
        this.showAndWait();
        return chosenPiece;
    }

    public void setChessPieceFactory(ChessPieceFactory chessPieceFactory) {
        this.chessPieceFactory = chessPieceFactory;
    }
}
