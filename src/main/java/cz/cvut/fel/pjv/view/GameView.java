package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameView {
    private final Game game;
    private final BoardView boardView;
    private Scene scene;
    private final TimerView timerView = new TimerView();
    private final Button saveButton = new Button("Save game");
    private final Button startGame = new Button("Start game");
    private final Button chooseSide = new Button("Switch colors");
    private final Text currentlyEditing = new Text("Editing white pieces, white will start");
    private VBox root;

    public GameView(Game game) {
        this.game = game;
        boardView = new BoardView(game.getBoard());
    }

    /**
     * This method is used to create the game view scene
     * @return The game view scene with a board and all the UI controls
     */
    public Scene createScene() {
        root = new VBox();
        scene = new Scene(root);

        root.setSpacing(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(boardView);

        if (game.getBoard().isEditable()) {
            HBox hBox = new HBox(startGame, chooseSide);
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER);
            root.getChildren().addAll(hBox, currentlyEditing);
        } else {
            root.getChildren().add(saveButton);
        }

        return scene;
    }

    /**
     * @return The board view
     */
    public BoardView getBoardView() {
        return boardView;
    }


    /**
     * @return The save button
     */
    public Button getSaveButton() {
        return saveButton;
    }

    /**
     * @return The start game button for custom games
     */
    public Button getStartGame() {
        return startGame;
    }

    /**
     * @return The choose side button for custom game creation
     */
    public Button getChooseSide() {
        return chooseSide;
    }

    /**
     * @return The game view scene
     */
    public Scene getScene() {
        return scene;
    }

    public Text getCurrentlyEditing() {
        return currentlyEditing;
    }
}
