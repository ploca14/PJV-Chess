package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.JoinRequest;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.view.ClientView;
import cz.cvut.fel.pjv.view.GameView;
import cz.cvut.fel.pjv.view.PlayerStatsView;
import cz.cvut.fel.pjv.view.GamesStatsView;
import cz.cvut.fel.pjv.view.JoinDialog;
import cz.cvut.fel.pjv.controller.network.ClientGameController;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

public class ClientController {
    private ClientView clientView;
    private Stage stage;

    /**
     * @param clientView The client view that this controller wil be rerendering
     * @param stage The window
     */
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

            startGame(game);
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
                    startGame(loadGameFromFile(file));
                } catch (IOException | ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
            }
        }));

        Button customGame = clientView.getCustomGame();
        customGame.setOnAction(actionEvent -> {
            Game game = new Game();
            game.getBoard().setEditable(true);
            startGame(game);
        });

        Button startVsAiGame = clientView.getStartVsAiGame();
        startVsAiGame.setOnAction(actionEvent -> {
            Game game = new Game();
            game.getBoard().placeChessPieces();
            game.setVersusAi(true);
            startGame(game);
        });

        Button networkGame = clientView.getNetworkGame();
        networkGame.setOnAction(actionEvent -> {
            Optional<JoinRequest> joinRequest = new JoinDialog().showAndWait();
            if (joinRequest.isPresent()) {
                String name = joinRequest.get().getName();
                String ip = joinRequest.get().getIp();
                int port = joinRequest.get().getPort();

                Game game = joinNetworkGame(name, ip, port);
                if (game != null) {
                    startGame(game);
                }
            }
        });
    }

    private Game loadGameFromFile(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Game game = (Game) objectInputStream.readObject();
        return game;
    }

    private void startGame(Game game) {
        GameView gameView = new GameView(game);
        GameController gameController = new GameController(game, gameView);

        setScene(gameController);
    }

    private void setScene(AbstractGameController gameController) {
        stage.setScene(gameController.getGameView().createScene());

        gameController.getGameView().getMainMenuButton().setOnAction(actionEvent -> {
            stage.setScene(clientView.getScene());
            if (!gameController.getGameModel().isVersusAi()) {
                gameController.getWhiteTimerController().stopThread();
                gameController.getBlackTimerController().stopThread();
            }
        });

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                if (!gameController.getGameModel().isVersusAi()) {
                    gameController.getWhiteTimerController().stopThread();
                    gameController.getBlackTimerController().stopThread();
                }
            }
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

    private Game joinNetworkGame(String name, String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            Game game =  (Game) inputStream.readObject();
            Color playingAs = (Color) inputStream.readObject();
            outputStream.writeObject(name);
            outputStream.flush();

            GameView gameView = new GameView(game);

            if (playingAs.equals(Color.WHITE)) {
                gameView.setWhiteName(name);
            } else {
                gameView.setBlackName(name);
            }

            ClientGameController gameController = new ClientGameController(game, gameView, playingAs);
            gameController.setInputStream(inputStream);
            gameController.setOutputStream(outputStream);

            stage.setScene(gameController.getGameView().createScene());

            gameController.getGameView().getMainMenuButton().setOnAction(actionEvent -> {
                stage.setScene(clientView.getScene());
                gameController.getWhiteTimerController().stopThread();
                gameController.getBlackTimerController().stopThread();
                try {
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    gameController.getWhiteTimerController().stopThread();
                    gameController.getBlackTimerController().stopThread();
                    try {
                        inputStream.close();
                        outputStream.close();
                        socket.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            });
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to connect").show();
        }
        return null;
    }
}
