package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.model.Game;
import cz.cvut.fel.pjv.model.Move;
import cz.cvut.fel.pjv.model.chestpieces.Chesspiece;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.model.network.Lobby;
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
    private Game game;
    private ServerGameController gameController;
    private ServerSocket serverSocket;
    private final Object syncObject = new Object();

    /**
     * @param lobby the lobby model this controller will be updating
     * @param serverModel the server model this lobby is running on
     */
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

            // Create new game
            game = new Game();
            game.getBoard().placeChessPieces();
            gameController = new ServerGameController(game, lobby);

            // Wait for both players to connect
            acceptConnections();

            startGame();

            // Once the game finishes - remove the lobby from active lobbies
            endLobby();
        } catch (IOException e) {
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Port already in use").show());
            logger.info("Lobby: " + lobby.getName() + " cannot be created. Port " + lobby.getPort() + " is already in use");
        }
    }

    /**
     * This method is used to end the game, stop all the client threads, close the server socket and remove the lobby from the serverModel
     */
    public void endLobby() {
        // First we set the game to finished
        game.setFinished(true);

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

        // Lastly we remove the lobby from the server model
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
                ClientThread clientThread = new ClientThread(clientSocket, getColor());
                clientThread.start();

                // And we add the player to the lobby
                lobby.addPlayer(clientThread);
            }

            // Once both players are connected  we set the opponent thread for each client thread
            setOpponents();

            logger.info("Lobby " + lobby.getName() + " has started");
        } catch (IOException e) {
            logger.info("Connection failed");
        }
    }

    /**
     * This method is used to set the opponent player fot both of the players
     */
    private void setOpponents() {
        lobby.getFirstPlayer().setOpponent(lobby.getSecondPlayer());
        lobby.getSecondPlayer().setOpponent(lobby.getFirstPlayer());
    }

    /**
     * @throws IOException When we are unable to send the name to the client
     */
    private void sendNames() throws IOException {
        // First we check if both players are connected
        if (lobby.getConnectedPlayersCount() == 2) {
            // If yes we set the opponent for each player
            lobby.getFirstPlayer().sendName(lobby.getSecondPlayer().getPlayerName());
            lobby.getSecondPlayer().sendName(lobby.getFirstPlayer().getPlayerName());
        }
    }

    /**
     * This method is used to decide the color of the newly connected player
     * @return The color for the player
     */
    private Color getColor() {
        // If the player is the first one to connect we return white
        if (lobby.getConnectedPlayersCount() == 0) {
            return Color.WHITE;
        } else {
            // If hes the second one we return black
            return Color.BLACK;
        }
    }

    /**
     * This method is used to start the game and it will run until the game finishes
     */
    private void startGame() {
        try {
            synchronized(syncObject) {
                // We wait till the game is finished
                while(!game.getFinished()) {
                    syncObject.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class ClientThread extends Thread {
        private final Socket socket;
        private final ObjectOutputStream outputStream;
        private final ObjectInputStream inputStream;
        private final Color playingAs;
        private boolean stopRunning;
        private ClientThread opponent;
        private String name;

        /**
         * @param socket The client socket
         * @param playingAs The color if the player
         * @throws IOException When the socket is already closed
         */
        public ClientThread(Socket socket, Color playingAs) throws IOException {
            this.socket = socket;
            this.playingAs = playingAs;

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        }

        @Override
        public void run() {
            try {
                // Once the player connects we send him the gameModel and his Color
                outputStream.writeObject(game);
                outputStream.writeObject(playingAs);
                outputStream.flush();
                // Then we wait for the players name
                this.name = (String) inputStream.readObject();

                // Then we send the names to the opponents
                sendNames();

                // This makes sure the thread runs until the lobby is stopped
                while (!stopRunning) {
                    // Then we wait for the players move
                    receiveAndProcessMove();
                }
            } catch (IOException | ClassNotFoundException exception) {
                logger.info("Player " + lobby.getConnectedPlayersCount() + " has disconnected from lobby " + lobby.getName());
                lobby.removePlayer(this);
                endLobby();
            }
        }

        private void sendName(String name) throws IOException {
            outputStream.writeObject(name);
            outputStream.flush();
        }

        /**
         * This method is used to stop the client thread once the lobby is stopped
         */
        public synchronized void stopRunning() {
            stopRunning = true;
            endLobby();
        }

        /**
         * This method is used to close the streams and the client socket
         */
        private void closeConnection() throws IOException {
            inputStream.close();
            outputStream.close();
            socket.close();
        }

        /**
         * This method is used to close the streams and the client socket
         */
        private void endLobby() {
            try {
                closeConnection();
                synchronized(syncObject) {
                    game.setFinished(true);
                    syncObject.notify();
                    opponent.closeConnection();
                }
            } catch (IOException exception) {
                logger.info("Unable to close socket.");
            }
        }

        private void receiveAndProcessMove() throws IOException {
            try {
                // We wait for the user to send a move
                Move move = NetworkUtilities.parseMove(inputStream.readObject(), gameController.getBoardController().getBoardModel());
                // Then we check if its a valid move
                if (gameController.validateMove(move)) {
                    // If it us we perform the move on the game model
                    gameController.makeMove(move);

                    // Then we notify the client that the move was successful
                    outputStream.writeBoolean(true);
                    outputStream.flush();

                    // Then we send the move to the opponent
                    opponent.sendOpponentsMove(move);

                    // If the move is special we perform additional steps
                    if (move.isSpecial()) {
                        // Is the move is pawn promoting
                        if (move.isPawnPromoting()) {
                            // We wait for the selected piece
                            Chesspiece chesspiece = (Chesspiece) inputStream.readObject();
                            // Then we set the selected piece to the ending position tile of the move
                            gameController.getBoardController().setChosenChesspiece(move.getEndingPosition(), chesspiece);
                            // We notify the client that the pawn promotion was successful
                            outputStream.writeBoolean(true);
                            outputStream.flush();

                            opponent.sendChosenChesspiece(chesspiece);
                        } else {
                            gameController.getBoardController().checkSpecialMoves(move);
                        }
                    }
                    // Then we switch the current player in the model
                    gameController.takeTurn();
                } else {
                    // Or we notify the client that the move wasn't successful
                    outputStream.writeBoolean(false);
                }
            } catch (ClassNotFoundException e) {
                logger.warning("Packet could not be deserialized");
                outputStream.writeBoolean(false);
            }
            outputStream.flush();
        }

        private void sendOpponentsMove(Move move) throws IOException {
            outputStream.writeObject(move);
            outputStream.flush();
        }

        private void sendChosenChesspiece(Chesspiece chesspiece) throws IOException {
            outputStream.writeObject(chesspiece);
            outputStream.flush();
        }

        public void setOpponent(ClientThread opponent) {
            this.opponent = opponent;
        }

        public String getPlayerName() {
            return name;
        }
    }
}
