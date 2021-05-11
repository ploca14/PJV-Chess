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

    /**
     * This method is used to wait for the two players to connect to the lobby
     */
    private void acceptConnections() {
        try {
            while (lobby.getConnectedPlayersCount() < 2) {
                Socket clientSocket =  serverSocket.accept(); // Here we wait for a player to connect

                // Once a player connects to the lobby
                logger.info("Player " + lobby.getConnectedPlayersCount() + " has connected to lobby " + lobby.getName());
                // We create a new ClientThread from the client socket
                ClientThread clientThread = new ClientThread(clientSocket, lobby.getConnectedPlayersCount());
                clientThread.start();

                // And we add the player to the lobby
                lobby.addPlayer(clientThread);
            }

            logger.info("Lobby " + lobby.getName() + " has started");
        } catch (IOException e) {
            logger.info("Connection failed");
        }
    }

    /**
     * This method is used to start the game and it will run until the game finishes
     */
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
                // Once the player connects we send him his ID (TODO: We could send him the color instead)
                outputStream.writeInt(playerID);
                outputStream.flush();

                // This makes sure the thread runs until the lobby is stopped
                while (!stopRunning) {
                    try {
                        // We wait for the user to send a move
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

        /**
         * This method is used to stop the client thread once the lobby is stopped
         */
        public synchronized void stopRunning() {
            stopRunning = true;
            closeConnection();
        }

        /**
         * This method is used to close the streams and the client socket
         */
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
