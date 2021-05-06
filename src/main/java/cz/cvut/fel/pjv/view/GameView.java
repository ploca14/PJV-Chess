package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.Game;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class GameView {
    private final Game game;
    private final BoardView boardView;
    private Scene scene;
    private final TimerView timerView = new TimerView();
    private final Button saveButton = new Button("Save game");
    private VBox root = new VBox();

    public GameView(Game game) {
        this.game = game;
        boardView = new BoardView(game.getBoard());
    }

    public Scene createScene() {
        scene = new Scene(root);

        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(boardView, saveButton);

        return scene;
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Scene getScene() {
        return scene;
    }
}
