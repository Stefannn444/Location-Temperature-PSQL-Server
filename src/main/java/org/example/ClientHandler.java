package org.example;

import com.google.gson.Gson;
import org.example.network.DatabaseService;
import org.example.network.Request;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final DatabaseService databaseService;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Gson gson;

    public ClientHandler(Socket socket, DatabaseService databaseService) throws IOException {
        this.clientSocket = socket;
        this.databaseService = databaseService;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String jsonRequest = (String) in.readObject();
                Request request = gson.fromJson(jsonRequest, Request.class);
                String response = handleRequest(request);
                out.writeObject(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String handleRequest(Request request) {
        try {
            switch (request.getCommand()) {
                case "LOGIN":
                    return databaseService.loginUser(request.getUsername());
                case "ADD_LOCATION":
                    if (!request.getUsername().equalsIgnoreCase("ADMIN")) {
                        return "Error: Unauthorized access";
                    }
                    return databaseService.addLocation(request.getData());

                case "GET_WEATHER":
                    return databaseService.getWeather(request.getUsername());

                case "CHANGE_LOCATION":
                    return databaseService.updateUserLocation(
                            request.getUsername(),
                            (Double) request.getData().get("latitude"),
                            (Double) request.getData().get("longitude")
                    );

                default:
                    return "Error: Unknown command";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
