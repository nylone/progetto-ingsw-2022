package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Exceptions.Input.InputValidationException;
import it.polimi.ingsw.Exceptions.Operation.ForbiddenOperationException;
import it.polimi.ingsw.Exceptions.Operation.OperationException;
import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Server.Messages.Events.ClientEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.ClientConnectEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.ClientDisconnectEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.GameStartEvent;
import it.polimi.ingsw.Server.Messages.Events.Internal.LobbyClosedEvent;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import static it.polimi.ingsw.Server.LobbyServer.lobbyMap;

/**
 * Multiple {@link LobbyServer} instances will need to communicate to run the game. This class groups the the {@link BlockingQueue<ClientEvent>}
 * used by each server to dispatch gameLobby-wide events.
 */
public class Lobby {
    private final UUID id;
    private final String admin;
    private final boolean isPublic;
    private final int maxPlayers;
    private final List<String> players;
    private final Map<String, BlockingQueue<ClientEvent>> playerEventQueues;
    private boolean isClosed;
    private Controller controller;

    /**
     * Create a new lobby
     * @param id a unique ID used to refer to the lobby
     * @param isPublic if the lobby is supposed to be publicly available to new clients
     * @param maxPlayers the amount of clients the game connected to the lobby will host
     * @param admin the name of the admin client. The admin owns the game and even though everyone can start a session, if
     *              the admin disconnects while in the waiting lobby, the lobby is closed.
     */
    public Lobby(UUID id, boolean isPublic, int maxPlayers, String admin) {
        this.id = id;
        this.admin = admin;
        this.isPublic = isPublic;
        this.isClosed = false;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.playerEventQueues = new ConcurrentHashMap<>();
    }

    /**
     * Attempts to forward an action to the game's controller
     * @param pa the action to forward
     * @throws InputValidationException if the controller does not validate the action positively
     * @throws OperationException if no controller is online or if a validated action failed to run properly
     */
    public synchronized void executeAction(PlayerAction pa) throws InputValidationException, OperationException {
        if (controller == null) {
            throw new ForbiddenOperationException("Game lobby", "Lobby is in waiting state, no game is running");
        }
        controller.executeAction(pa);
    }

    /**
     * Get the id of the lobby
     * @return the {@link UUID} of the lobby
     */
    public UUID getId() {
        return id;
    }

    /**
     * Get the name of the admin
     * @return the nickname of the admin client
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * Check if the lobby is public or not
     * @return true if the lobby is public, false otherwise
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Check to see if the lobby is full
     * @return true if the lobby is full, false otherwise
     */
    public boolean isLobbyFull() {
        return this.players.size() == maxPlayers;
    }

    /**
     * Get the maximum amount of players for the lobby
     * @return the max amount of clients that can connect to the lobby
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Get a list of connected players
     * @return a {@link List} of the nicknames of the connected players
     */
    public List<String> getPlayers() {
        synchronized (this.players) {
            return List.copyOf(this.players);
        }
    }

    /**
     * Connect a player to the lobby
     * @param nick the nickname of the player to connect
     * @param playerChannel the queue to forward new {@link ClientEvent}s to
     * @return true if the player was successfully connected, false otherwise
     */
    public boolean addPlayer(String nick, BlockingQueue<ClientEvent> playerChannel) {
        synchronized (this.players) {
            if (this.isClosed) {
                return false;
            }
            // in case of new connection check for max players and check that the game has not yet started
            if (this.controller == null && this.maxPlayers > this.players.size()) {
                this.players.add(nick);
                this.playerEventQueues.put(nick, playerChannel);
                notifyPlayers(new ClientConnectEvent(nick, List.copyOf(this.players)));
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Propagates a {@link ClientEvent} to all players
     * @param event the event to propagate to all players
     */
    public void notifyPlayers(ClientEvent event) {
        synchronized (this.players) {
            for (BlockingQueue<ClientEvent> queue : this.playerEventQueues.values()) {
                try {
                    queue.put(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Removes a player from the lobby. If the game has started for the lobby or the admin decided to leave, the lobby is closed
     * and players are notified through {@link LobbyClosedEvent}
     * @param nick the nickname of the player to remove from the lobby
     */
    protected void disconnectPlayer(String nick) {
        synchronized (this.players) {
            this.playerEventQueues.remove(nick);
            this.players.remove(nick);
            notifyPlayers(new ClientDisconnectEvent(nick, List.copyOf(this.players)));
            if (this.isGameInProgress() || this.admin.equals(nick)) {
                lobbyMap.remove(this.id);
                this.close();
            }
        }
    }

    /**
     * Check to see if the game has started or if clients are still waiting for the game to start
     * @return true if the game is on, otherwise false
     */
    public boolean isGameInProgress() {
        return this.controller != null;
    }

    /**
     * Closes the lobby and notifies players through {@link LobbyClosedEvent}
     */
    protected void close() {
        notifyPlayers(new LobbyClosedEvent());
        this.players.clear();
        this.playerEventQueues.clear();
        this.isClosed = true;
    }

    /**
     * Starts the game. {@link LobbyServer}s will receive a {@link GameStartEvent}
     * @param gameMode the {@link GameMode} to start the game in
     * @throws InputValidationException if the players in lobby are more than 4 or less than 2
     */
    protected void startGame(GameMode gameMode) throws InputValidationException {
        synchronized (this.players) {
            Map<String, Integer> nickToID = new HashMap<>(this.players.size());
            for (int i = 0; i < this.players.size(); i++) {
                nickToID.put(this.players.get(i), i);
            }
            notifyPlayers(new GameStartEvent(nickToID));
            this.controller = Controller.createGame(gameMode,
                    OptionalValue.of(this),
                    players.toArray(String[]::new)
            );
        }
    }

}
