package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.Game;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class GameView {
    private final Game game;
    private final BoardView boardView;
    private final TimerView timerView = new TimerView();
    private VBox root = new VBox();

    public GameView(Game game) {
        this.game = game;
        boardView = new BoardView(game.getBoard());
    }

    public Scene getScene() {
        Scene scene = new Scene(root);

        root.setSpacing(10);

        root.getChildren().add(boardView);

        return scene;
    }

    public BoardView getBoardView() {
        return boardView;
    }
}
