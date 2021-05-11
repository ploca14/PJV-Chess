package cz.cvut.fel.pjv.model.network;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server {
    private final ObservableList<Lobby> lobbies = FXCollections.observableArrayList();

    /**
     * @param lobby The lobby to add to the active lobbies list
     */
    public void addLobby(Lobby lobby) {
        lobbies.add(lobby);
    }

    /**
     * @param lobby The lobby to remove from the active lobbies list
     */
    public void removeLobby(Lobby lobby) {
        lobbies.remove(lobby);
    }

    public ObservableList<Lobby> getLobbies() {
        return lobbies;
    }
}
