package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.model.network.Packet;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter server port: ");
        try {
            int port = Integer.parseInt(scanner.nextLine());

            Socket socket = new Socket("localhost", port);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            int playerID = inputStream.readInt();
            System.out.println("Connected to server as player #" + playerID);

            String userInput = scanner.nextLine();
            while (!userInput.equals("q")) {
                outputStream.writeObject(new Packet(userInput));
                outputStream.flush();
                System.out.println("Response from server: " + inputStream.readBoolean());
                userInput = scanner.nextLine();
            }
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }

    }
}
