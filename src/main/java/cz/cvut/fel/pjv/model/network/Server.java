package cz.cvut.fel.pjv.model.network;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server {
    private final ObservableList<Lobby> lobbies = FXCollections.observableArrayList();

    public void addLobby(Lobby lobby) {
        lobbies.add(lobby);
    }

    public void removeLobby(Lobby lobby) {
        lobbies.remove(lobby);
    }

    public ObservableList<Lobby> getLobbies() {
        return lobbies;
    }
}
