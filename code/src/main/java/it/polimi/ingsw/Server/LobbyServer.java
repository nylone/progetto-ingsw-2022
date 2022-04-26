package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.GameHandler;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class LobbyServer {
    private final Map<Integer, Integer> lobbies;
    private final Map<Integer, GameHandler> games;

    public LobbyServer() {
        this.lobbies = new HashMap<>();
        this.games = new HashMap<>();
    }

    protected void handle(Socket connection) {
        new Thread(() -> {
            try {
                InputStream inputStream = connection.getInputStream();
                BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write("You are now connected\n".getBytes());
                System.out.println(input.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
