package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.PlayerStats;
import cz.cvut.fel.pjv.view.PlayerStatsView;
import javafx.scene.control.Alert;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;


public class PlayerStatsController {
    private final PlayerStatsView playerStatsView;

    /**
     * @param playerStatsView The view this controller will be rerendering
     */
    public PlayerStatsController(PlayerStatsView playerStatsView) {
        this.playerStatsView = playerStatsView;

        initController();
    }

    private void initController() {
        playerStatsView.getSubmitBtn().setOnAction((actionEvent -> {
            String ip = playerStatsView.getIpField().getText();

            try {
                Socket socket = new Socket(ip, 6969);

                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeUTF("playerStatistics");

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                List<PlayerStats> playerStats =  (List<PlayerStats>) inputStream.readObject();

                playerStatsView.setTableViewItems(playerStats);
            } catch (IOException | ClassNotFoundException exception) {
                new Alert(Alert.AlertType.ERROR, "Unable to connect").show();
            }
        }));
    }

    public PlayerStatsView getPlayerStatsView() {
        return playerStatsView;
    }
}
