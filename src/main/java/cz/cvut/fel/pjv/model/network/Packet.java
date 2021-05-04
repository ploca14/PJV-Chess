package cz.cvut.fel.pjv.model.network;

import java.io.Serializable;

public class Packet implements Serializable {
    private final String move; // This will be later changed to a Move class

    public Packet(String move) {
        this.move = move;
    }

    public String getMove() {
        return move;
    }
}
