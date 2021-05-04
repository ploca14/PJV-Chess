package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.controller.BoardController;
import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.view.GameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClientApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Chess Game");
        stage.setWidth(615);
        stage.setHeight(700);
        stage.show();

        // This is just temporary
        BorderPane pane = new BorderPane();
        Button startGame = new Button("Start local game");
        pane.setCenter(startGame);
        Scene scene = new Scene(pane);
        stage.setScene(scene);

        startGame.setOnAction((event) -> {
            Game game = new Game();
            game.getBoard().placeChessPieces();
            GameView gameView = new GameView(game);
            new BoardController(game.getBoard(), gameView.getBoardView());
            stage.setScene(gameView.getScene());
        });
    }
}
