package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Operation.ForbiddenOperationException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Server.Messages.Events.ClientEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.ClientConnectEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.ClientDisconnectEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.GameStartEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.LobbyClosedEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static it.polimi.ingsw.Server.LobbyServer.lobbyMap;

public class Lobby {
    private final UUID id;
    private final String admin;
    private final boolean isPublic;
    private final int maxPlayers;
    private final List<String> players;
    private final Map<String, ClientEventHandler> playerEventSources;
    private boolean isClosed;
    private Controller controller;

    protected Lobby(UUID id, boolean isPublic, int maxPlayers, String admin) {
        this.id = id;
        this.admin = admin;
        this.isPublic = isPublic;
        this.isClosed = false;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.playerEventSources = new ConcurrentHashMap<>();
    }

    public void executeAction(PlayerAction pa) throws InputValidationException, OperationException {
        if (controller == null) {
            throw new ForbiddenOperationException("Lobby is in waiting state, no game is running");
        }
        controller.executeAction(pa);
    }

    public UUID getId() {
        return id;
    }

    public String getAdmin() {
        return admin;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isLobbyFull() {
        return this.players.size() == maxPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public List<String> getPlayers() {
        synchronized (this.players) {
            return List.copyOf(this.players);
        }
    }

    public List<String> getDisconnectedPlayers() {
        synchronized (this.players) {
            Set<String> connected = this.playerEventSources.keySet();
            return this.players.stream()
                    .filter(Predicate.not(connected::contains))
                    .toList();
        }
    }

    public boolean addPlayer(String nick, ClientEventHandler playerChannel) {
        synchronized (this.players) {
            if (this.isClosed) {
                return false;
            }
            // in case of new connection check for max players and check that the game has not yet started
            if (this.controller == null && this.maxPlayers > this.players.size()) {
                this.players.add(nick);
                this.playerEventSources.put(nick, playerChannel);
                notifyPlayers(new ClientConnectEvent(nick, List.copyOf(this.players)));
                return true;
            } else {
                return false;
            }
        }
    }

    public void notifyPlayers(ClientEvent event) {
        synchronized (this.players) {
            for (ClientEventHandler ceh : this.playerEventSources.values()) {
                try {
                    ceh.enqueue(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void disconnectPlayer(String nick) {
        synchronized (this.players) {
            this.playerEventSources.remove(nick);
            this.players.remove(nick);
            notifyPlayers(new ClientDisconnectEvent(nick, List.copyOf(this.players)));
            if (this.isGameInProgress() || this.admin.equals(nick)) {
                lobbyMap.remove(this.id);
                this.close();
            }
        }
    }

    public boolean isGameInProgress() {
        return this.controller != null;
    }

    protected void close() {
        notifyPlayers(new LobbyClosedEvent());
        this.players.clear();
        this.playerEventSources.clear();
        this.isClosed = true;
    }

    protected void startGame(GameMode gameMode) throws InputValidationException {
        synchronized (this.players) {
            Map<String, Integer> nickToID = new HashMap<>(this.players.size());
            for (int i = 0; i < this.players.size(); i++) {
                nickToID.put(this.players.get(i), i);
            }
            notifyPlayers(new GameStartEvent(nickToID));
            this.controller = new Controller(
                    gameMode,
                    this,
                    players.toArray(String[]::new)
            );
        }
    }

}
