package org.example;

import org.example.network.DatabaseService;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 6543;
    private final DatabaseService databaseService;

    public Server() {
        this.databaseService = new DatabaseService();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, databaseService)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
