package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.Game;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameView {
    private final Game game;
    private final BoardView boardView;
    private Scene scene;
    private final TimerView timerView = new TimerView();
    private final Button saveButton = new Button("Save game");
    private final Button startGame = new Button("Start game");
    private final Button chooseSide = new Button("Add black pieces");
    private VBox root;

    public GameView(Game game) {
        this.game = game;
        boardView = new BoardView(game.getBoard());
    }

    public Scene createScene() {
        root = new VBox();
        scene = new Scene(root);

        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(boardView);

        if (game.getBoard().isEditable()) {
            HBox hBox = new HBox(startGame, chooseSide);
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER);
            root.getChildren().add(hBox);
        } else {
            root.getChildren().add(saveButton);
        }

        return scene;
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getStartGame() {
        return startGame;
    }

    public Button getChooseSide() {
        return chooseSide;
    }

    public Scene getScene() {
        return scene;
    }
}
