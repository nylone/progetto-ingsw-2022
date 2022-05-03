package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.Controller.GameHandler;
import it.polimi.ingsw.Controller.PlayerAction;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.RemoteView.Messages.ClientEvents.GameStarted;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameLobby {
    private final List<String> players;
    private final Map<String, ClientEventHandler> playerEventSources;
    private final GameHandler gameHandler;

    public GameLobby(GameMode gameMode, List<String> players, Map<String, ClientEventHandler> playerEventSources) {
        this.players = players;
        this.playerEventSources = playerEventSources;
        this.gameHandler = new GameHandler(gameMode, players.toArray(new String[0]));
    }

    protected boolean addPlayer(String nick, ClientEventHandler playerChannel) {
        synchronized (this.players) {
            // in case of reconnection
            if (this.players.contains(nick)) {
                if (!this.playerEventSources.containsKey(nick)) {
                    this.playerEventSources.put(nick, playerChannel);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    protected void removePlayerHandler(String nick) {
        synchronized (this.players) {
            this.playerEventSources.remove(nick);
        }
    }

    protected void startGame() {
        GameStarted event = new GameStarted();
        for (ClientEventHandler ceh: this.playerEventSources.values()){
            try {
                ceh.enqueue(event);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
