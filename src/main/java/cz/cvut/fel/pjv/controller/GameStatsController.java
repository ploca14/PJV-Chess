package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.GameStatistic;
import cz.cvut.fel.pjv.view.GamesStatsView;
import javafx.scene.control.Alert;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameStatsController {
    private final GamesStatsView gamesStatsView;

    public GameStatsController(GamesStatsView gamesStatsView) {
        this.gamesStatsView = gamesStatsView;

        initController();
    }

    private void initController() {
        gamesStatsView.getSubmitBtn().setOnAction((actionEvent) -> {
            String ip = gamesStatsView.getIpField().getText();

            try {
                Socket socket = new Socket(ip, 6969);

                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeUTF("gameStatistics");

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                List<GameStatistic> gameStatistics =  (List<GameStatistic>) inputStream.readObject();

                gamesStatsView.setTableViewItems(gameStatistics);
            } catch (IOException | ClassNotFoundException exception) {
                new Alert(Alert.AlertType.ERROR, "Unable to connect").show();
            }
        });
    }

    public GamesStatsView getGameStatsView() {
        return gamesStatsView;
    }
}
