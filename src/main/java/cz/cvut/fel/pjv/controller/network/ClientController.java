package cz.cvut.fel.pjv.controller.network;

import java.io.IOException;
import java.net.Socket;

public class ClientController {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 42096);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
