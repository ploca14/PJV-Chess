package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.controller.GameController;
import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.view.GameView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Chess Game");
        stage.setWidth(615);
        stage.setHeight(700);
        stage.setResizable(false);
        stage.show();

        // This is just temporary
        Button startGame = new Button("Start local game");
        Button loadGame = new Button("Load local game");
        Button customGame = new Button("Start custom game");
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(startGame, loadGame, customGame);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        stage.setScene(scene);


        startGame.setOnAction((event) -> {
            Game game = new Game();
            game.getBoard().placeChessPieces();
            GameView gameView = new GameView(game);
            new GameController(game, gameView);
            stage.setScene(gameView.createScene());
        });

        loadGame.setOnAction((actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Game");
            fileChooser.setInitialFileName("Game");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Game File", "*.ser"));

            File file = fileChooser.showOpenDialog(stage);

            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Game game = (Game) objectInputStream.readObject();
                GameView gameView = new GameView(game);
                new GameController(game, gameView);
                stage.setScene(gameView.createScene());
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }));

        customGame.setOnAction(actionEvent -> {
            Game game = new Game();
            game.getBoard().setEditable(true);
            GameView gameView = new GameView(game);
            new GameController(game, gameView);
            stage.setScene(gameView.createScene());
        });
    }
}
