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
    private final TimerView whiteTimerView;
    private final TimerView blackTimerView;
    private final Button saveButton = new Button("Save game");
    private final Button mainMenuButton = new Button("Main menu");
    private final Button startGame = new Button("Start game");
    private final Button chooseSide = new Button("Switch colors");
    private final Text currentlyEditing = new Text("Editing white pieces, white will start");
    private VBox root;

    public GameView(Game game) {
        this.game = game;
        boardView = new BoardView(game.getBoard());
        whiteTimerView = new TimerView(game.getWhiteTimer());
        blackTimerView = new TimerView((game.getBlackTimer()));
    }

    /**
     * This method is used to create the game view scene
     * @return The game view scene with a board and all the UI controls
     */
    public Scene createScene() {
        root = new VBox();
        scene = new Scene(root);

        root.setSpacing(10);
        root.setPadding(new Insets(10, 0, 10, 0));
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(blackTimerView);
        root.getChildren().add(boardView);
        root.getChildren().add(whiteTimerView);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        if (game.getBoard().isEditable()) {
            hBox.getChildren().addAll(currentlyEditing, startGame, chooseSide);
        } else {
            hBox.getChildren().addAll(saveButton, mainMenuButton);
        }
        root.getChildren().add(hBox);

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
     * @return The main menu button
     */
    public Button getMainMenuButton() {
        return mainMenuButton;
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

    public TimerView getWhiteTimerView() {
        return whiteTimerView;
    }

    public TimerView getBlackTimerView() {
        return blackTimerView;
    }
}
