package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.chestpieces.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class ClientView {
    private final VBox root = new VBox();
    private final VBox menu = new VBox();
    private final Button startGame = new Button("Start local game");
    private final Button loadGame = new Button("Load local game");
    private final Button customGame = new Button("Start custom game");
    private final Label title = new Label("CHESS GAME");

    public Scene getScene() {
        Scene scene = new Scene(root);
        createMenuView();
        return scene;
    }

    private void createMenuView() {
        startGame.setStyle("-fx-background-color: #F5F5DC;");
        loadGame.setStyle("-fx-background-color: #F5F5DC;");
        customGame.setStyle("-fx-background-color: #F5F5DC;");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        title.setPadding(new Insets(10, 0, 50, 0));
        menu.getChildren().addAll(title, startGame, loadGame, customGame);
        menu.setSpacing(20);
        menu.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #D2B48C;");
        root.getChildren().addAll(menu);
    }

    public Button getStartGame() {
        return startGame;
    }

    public Button getLoadGame() {
        return loadGame;
    }

    public Button getCustomGame() {
        return customGame;
    }
}
