package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.GameHandler;
import it.polimi.ingsw.Server.Messages.ClientMessages.ConnectLobby;
import it.polimi.ingsw.Server.Messages.ClientMessages.CreateLobby;
import it.polimi.ingsw.Server.Messages.ClientMessages.DeclarePlayer;
import it.polimi.ingsw.Server.Messages.ClientMessages.Request;
import it.polimi.ingsw.Server.Messages.Enums.StatusCode;
import it.polimi.ingsw.Server.Messages.ServerMessages.LobbyServerAccept;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LobbyServer {
    private final Set<Integer> availableLobbies;

    private final Map<String, String> registeredNicknames; // maps username to password
    private final Map<SocketWrapper, String> socketToName;
    private final Map<SocketWrapper, Integer> socketToGameID;
    private final Map<Integer, GameHandler> gameIDToGameHandler;

    public LobbyServer() {
        this.availableLobbies = new HashSet<>();
        this.registeredNicknames = new HashMap<>();
        this.socketToName = new HashMap<>();
        this.socketToGameID = new HashMap<>();
        this.gameIDToGameHandler = new HashMap<>();
    }

    protected void asyncHandle(SocketWrapper sw) {
        new Thread(() -> {
            try {
                // accept phase: wait for valid nickname
                while (true) {
                    DeclarePlayer message = sw.awaitRequest(DeclarePlayer.class);
                    String nickname = message.getNickname();
                    String password = message.getPassword();
                    if (this.registeredNicknames.get(nickname) == null) {
                        sw.sendResponse(new LobbyServerAccept(StatusCode.Fail));
                    } else {
                        this.registeredNicknames.put(nickname, password);
                        this.socketToName.put(sw, nickname);
                        sw.sendResponse(new LobbyServerAccept(StatusCode.Success));
                        break;
                    }
                }
                // redirect phase: wait for valid lobby action
                // either:
                // - create
                // - join
                // - join started game (todo)
                while (true) {
                    Request request = sw.awaitRequest(Request.class);
                    switch (request) {
                        case ConnectLobby connectLobby: {
                        }
                        case CreateLobby createLobby: {
                        }
                    }
                    if (this.registeredNicknames.contains(nickname)) {
                        sw.sendResponse(new LobbyServerAccept(StatusCode.Fail));
                    } else {
                        this.registeredNicknames.add(nickname);
                        this.socketToName.put(sw, nickname);
                        sw.sendResponse(new LobbyServerAccept(StatusCode.Success));
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
