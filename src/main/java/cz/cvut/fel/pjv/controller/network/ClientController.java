package cz.cvut.fel.pjv.controller.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter server port: ");
        int port = scanner.nextInt();

        try {
            Socket socket = new Socket("localhost", port);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

            int playerID = dataInputStream.readInt();
            System.out.println("Connected to server as player #" + playerID);

            String userInput = "";
            while (!userInput.equals("q")) {
                userInput = scanner.nextLine();
                printWriter.println(userInput);
                printWriter.flush();
                System.out.println("echo: " + dataInputStream.readBoolean());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
