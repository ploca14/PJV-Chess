package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.view.ClientView;
import cz.cvut.fel.pjv.view.GameView;
import cz.cvut.fel.pjv.view.PlayerStatsView;
import cz.cvut.fel.pjv.view.GamesStatsView;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class ClientController {
    private ClientView clientView;
    private Stage stage;

    public ClientController(ClientView clientView, Stage stage) {
        this.clientView = clientView;
        this.stage = stage;
        initController();
    }

    private void initController() {
        Button startGame = clientView.getStartGame();
        startGame.setOnAction((event) -> {
            Game game = new Game();
            game.getBoard().placeChessPieces();
            GameView gameView = new GameView(game);
            new GameController(game, gameView);
            stage.setScene(gameView.createScene());
        });

        Button loadGame = clientView.getLoadGame();
        loadGame.setOnAction((actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Game");
            fileChooser.setInitialFileName("Game");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Game File", "*.ser"));

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
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
            }
        }));

        Button customGame = clientView.getCustomGame();
        customGame.setOnAction(actionEvent -> {
            Game game = new Game();
            game.getBoard().setEditable(true);
            GameView gameView = new GameView(game);
            new GameController(game, gameView);
            stage.setScene(gameView.createScene());
        });

        Button gameStatsOverview = clientView.getGameStatsOverview();
        gameStatsOverview.setOnAction(actionEvent -> {
            GamesStatsView statsView = new GamesStatsView();
            stage.setScene(statsView.createScene());
        });

        Button playerStatsOverview = clientView.getPlayerStatsOverview();
        playerStatsOverview.setOnAction(actionEvent -> {
            PlayerStatsView playerStatsView = new PlayerStatsView();
            stage.setScene(playerStatsView.createScene());
        });
    }
}
