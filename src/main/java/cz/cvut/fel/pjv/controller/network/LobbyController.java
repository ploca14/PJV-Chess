package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.ServerApp;
import cz.cvut.fel.pjv.model.network.Lobby;
import cz.cvut.fel.pjv.model.network.Server;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLOutput;

public class LobbyController implements Runnable {
    private final Lobby lobby;
    private final Server serverModel;
    private boolean gameRunning = false;

    public LobbyController(Lobby lobby, Server serverModel) {
        this.lobby = lobby;
        this.serverModel = serverModel;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(lobby.getPort());
            while (lobby.getConnectedPlayersCount() < 2) {
                System.out.println("Waiting for players...");
                ss.accept();
                lobby.increaseConnectedPlayersCount();
                System.out.println(serverModel.getLobbies().get(0).getConnectedPlayersCount());
            }
            // TODO: Logging
            System.out.println("Lobby " + lobby.getName() + " has started");
            // TODO: Start game
            gameRunning = true;
            while (gameRunning) {
                try { Thread.sleep(10000); }
                catch(InterruptedException ex) { Thread.currentThread().interrupt(); }
                gameRunning = false;
            }

            ss.close();
        } catch (IOException e) {
            // TODO: Logging
            String message = String.format("Lobby: %s cannot be created. Port %d is already in use", lobby.getName(), lobby.getPort());
            System.out.println(message);
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Port already in use").show());
        }

        // Once the game finishes - remove the lobby from active lobbies
        System.out.println("Lobby "+ lobby.getName() +" has finished");
        serverModel.removeLobby(lobby);
    }
}
