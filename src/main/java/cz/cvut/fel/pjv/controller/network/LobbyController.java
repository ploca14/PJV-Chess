package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.model.network.Lobby;
import cz.cvut.fel.pjv.model.network.Packet;
import cz.cvut.fel.pjv.model.network.Server;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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

            endLobby();
        } catch (IOException e) {
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Port already in use").show());
            logger.info("Lobby: " + lobby.getName() + " cannot be created. Port " + lobby.getPort() + " is already in use");
        }
    }

    private void endLobby() {
        // Stop all client threads
        for (Thread playerThread: lobby.getConnectedPlayers()) {
            ((ClientThread) playerThread).stopRunning();
        }

        // Stop the server socket
        try {
            serverSocket.close();
        } catch (IOException exception) {
            logger.info("Unable to close socket.");
        }

        serverModel.removeLobby(lobby);
        logger.info("Lobby " + lobby.getName() + " has finished");
    }

    private void acceptConnections() {
        try {
            while (lobby.getConnectedPlayersCount() < 2) {
                Socket clientSocket =  serverSocket.accept();
                logger.info("Player " + lobby.getConnectedPlayersCount() + " has connected to lobby " + lobby.getName());
                ClientThread clientThread = new ClientThread(clientSocket, lobby.getConnectedPlayersCount());
                clientThread.start();
                lobby.addPlayer(clientThread);
            }

            logger.info("Lobby " + lobby.getName() + " has started");
        } catch (IOException e) {
            logger.info("Connection failed");
        }
    }

    private void startGame() {
        gameRunning = true;
        while (gameRunning) {
            // This simulates the game running for 10 seconds and then ending
            try { Thread.sleep(10000); }
            catch(InterruptedException ex) { Thread.currentThread().interrupt(); }
            gameRunning = false;
        }
    }

    private class ClientThread extends Thread {
        private final Socket socket;
        private final ObjectOutputStream outputStream;
        private final ObjectInputStream inputStream;
        private final int playerID;
        private boolean stopRunning;

        public ClientThread(Socket socket, int playerID) throws IOException {
            this.socket = socket;
            this.playerID = playerID;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        }

        @Override
        public void run() {
            try {
                outputStream.writeInt(playerID);
                outputStream.flush();

                while (!stopRunning) {
                    try {
                        Packet packet = (Packet) inputStream.readObject();
                        System.out.println(packet.getMove());
                        outputStream.writeBoolean(true);
                        outputStream.flush();
                    } catch (ClassNotFoundException e) {
                        logger.warning("Packet could not be deserialized");
                        outputStream.writeBoolean(false);
                        outputStream.flush();
                    }

                }
            } catch (IOException exception) {
                logger.info("Player " + lobby.getConnectedPlayersCount() + " has disconnected from lobby " + lobby.getName());
                lobby.removePlayer(this);
                closeConnection();
            }
        }

        public synchronized void stopRunning() {
            stopRunning = true;
            closeConnection();
        }

        private void closeConnection() {
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException exception) {
                logger.info("Unable to close socket.");
            }
        }
    }
}
