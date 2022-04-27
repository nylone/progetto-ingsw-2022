package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Controller.GameHandler;
import it.polimi.ingsw.Server.Messages.Enums.StatusCode;
import it.polimi.ingsw.Server.Messages.Message;
import it.polimi.ingsw.Server.Messages.ServerMessages.ConnectionResult;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
                // get the input stream
                InputStream inputStream = connection.getInputStream();
                BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
                // get the output stream
                OutputStream outputStream = connection.getOutputStream();
                // get serializer/deserializer
                Gson gson = new GsonBuilder().create();
                outputStream.write(gson.toJson(new ConnectionResult(StatusCode.Success)).getBytes(StandardCharsets.UTF_8));
                System.out.println(gson.fromJson(input, Message.class).getHumanMeaning());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
