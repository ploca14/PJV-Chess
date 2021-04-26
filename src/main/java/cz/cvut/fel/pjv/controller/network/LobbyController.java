package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.model.network.Lobby;
import cz.cvut.fel.pjv.model.network.Server;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class LobbyController implements Runnable {
    private final static Logger logger = Logger.getLogger(LobbyController.class.getName());
    private final Lobby lobby;
    private final Server serverModel;
    // This is just a temporary indicator - the current game state will come from the Game class
    private boolean gameRunning = false;
    private ServerSocket serverSocket;

    public LobbyController(Lobby lobby, Server serverModel) {
        this.lobby = lobby;
        this.serverModel = serverModel;
    }

    @Override
    public void run() {
        try {
            // Start server
            serverSocket = new ServerSocket(lobby.getPort());
            logger.info(lobby.getName() + " is waiting for players");

            // Wait for both players to connect
            acceptConnections();

            // Once both players are connected, start the game
            // TODO: Start game
            startGame();

            // Once the game finishes - remove the lobby from active lobbies
            logger.info("Lobby " + lobby.getName() + " has finished");
            serverSocket.close();
            serverModel.removeLobby(lobby);
        } catch (IOException e) {
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Port already in use").show());
            logger.info("Lobby: " + lobby.getName() + " cannot be created. Port " + lobby.getPort() + " is already in use");
        }
    }

    private void acceptConnections() {
        try {
            while (lobby.getConnectedPlayersCount() < 2) {
                Socket clientSocket =  serverSocket.accept();
                lobby.increaseConnectedPlayersCount();
                ClientThread clientThread = new ClientThread(clientSocket, lobby.getConnectedPlayersCount());
                clientThread.start();
                logger.info("Player #" + lobby.getConnectedPlayersCount() + " has connected to lobby " + lobby.getName());
            }

            logger.info("Lobby " + lobby.getName() + " has started");
        } catch (IOException e) {
            logger.warning("Connection failed");
        }
    }

    private void startGame() {
        gameRunning = true;
        while (gameRunning) {
            try { Thread.sleep(10000); }
            catch(InterruptedException ex) { Thread.currentThread().interrupt(); }
            gameRunning = false;
        }
    }

    private class ClientThread extends Thread {
        private Socket socket;
        private Scanner scanner;
        private DataOutputStream dataOutputStream;
        private int playerID;

        public ClientThread(Socket socket, int playerID) {
            this.socket = socket;
            this.playerID = playerID;
            try {
                scanner = new Scanner(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                logger.warning("Unable to setup communication");
            }
        }

        @Override
        public void run() {
            try {
                dataOutputStream.writeInt(playerID);
                dataOutputStream.flush();

                String message = "";
                while (!message.equals("q")) {
                    message = scanner.nextLine();
                    System.out.println("Player #" + playerID + " said: " + message);
                    dataOutputStream.writeBoolean(true);
                    dataOutputStream.flush();
                }

                System.out.println("Connection dropped");
                lobby.decreaseConnectedPlayersCount();
            } catch (IOException e) {
                logger.warning("Unable to communicate");
            }
        }
    }
}
