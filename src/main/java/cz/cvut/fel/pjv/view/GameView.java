package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameView {
    private Game game;
    private final BoardView boardView;
    private Scene scene;
    private TimerView whiteTimerView;
    private TimerView blackTimerView;
    private final Button saveButton = new Button("Save game");
    private final Button mainMenuButton = new Button("Main menu");
    private final Button startGame = new Button("Start game");
    private final Button chooseSide = new Button("Switch colors");
    private final Text currentlyEditing = new Text("Editing white pieces, white will start");
    private final Text whiteName = new Text();
    private final Text blackName = new Text();
    private VBox root;

    public GameView(Game game) {
        this.game = game;
        boardView = new BoardView(game.getBoard());

        if (!game.isVersusAi()) {
            whiteTimerView = new TimerView(game.getWhiteTimer());
            blackTimerView = new TimerView((game.getBlackTimer()));
        }
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
        root.getChildren().add(createBar(blackName, blackTimerView));
        root.getChildren().add(boardView);
        root.getChildren().add(createBar(whiteName, whiteTimerView));

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

    private Region createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private HBox createBar(Text name, Text timer) {
        HBox bar = new HBox(name);
        bar.setPadding(new Insets(0, 10 , 0 ,10));
        name.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        if (!game.isVersusAi()) {
            bar.getChildren().addAll(createSpacer(), timer);
        }

        return bar;
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

    public void setGame(Game game) {
        this.game = game;
    }

    public void setWhiteName(String name) {
        whiteName.setText(name);
    }

    public void setBlackName(String name) {
        blackName.setText(name);
    }
}
