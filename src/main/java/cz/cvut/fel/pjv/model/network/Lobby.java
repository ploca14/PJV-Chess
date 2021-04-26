package cz.cvut.fel.pjv.model.network;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Lobby {
    private final IntegerProperty connectedPlayersCount = new SimpleIntegerProperty();
    private final int port;
    private final String name;

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

    public void increaseConnectedPlayersCount() {
        this.connectedPlayersCount.set(getConnectedPlayersCount() + 1);
    }

    public void decreaseConnectedPlayersCount() {
        this.connectedPlayersCount.set(getConnectedPlayersCount() - 1);
    }

    public IntegerProperty getConnectedPlayersCountProperty() {
        return connectedPlayersCount;
    }

    public int getConnectedPlayersCount() {
        return connectedPlayersCount.get();
    }

    public IntegerProperty connectedPlayersCountProperty() {
        return connectedPlayersCount;
    }
}
