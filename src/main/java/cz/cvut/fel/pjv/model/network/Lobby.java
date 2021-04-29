package cz.cvut.fel.pjv.model.network;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private final IntegerProperty connectedPlayersCount = new SimpleIntegerProperty();
    private final int port;
    private final String name;
    private final List<Thread> connectedPlayers = new ArrayList<>();

    public Lobby(String name, int port) {
        this.name = name;
        this.port = port;
        connectedPlayersCount.set(0);
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public void addPlayer(Thread playerThread) {
        connectedPlayers.add(playerThread);
        this.connectedPlayersCount.set(getConnectedPlayersCount() + 1);
    }

    public void removePlayer(Thread playerThread) {
        connectedPlayers.remove(playerThread);
        this.connectedPlayersCount.set(getConnectedPlayersCount() - 1);
    }

    public List<Thread> getConnectedPlayers() {
        return connectedPlayers;
    }

    public IntegerProperty getConnectedPlayersCountProperty() {
        return connectedPlayersCount;
    }

    public int getConnectedPlayersCount() {
        return connectedPlayersCount.get();
    }
}
