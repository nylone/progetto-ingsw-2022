package it.polimi.ingsw.Client.GUI.Listeners;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.Panels.GameInProgressPanel;
import it.polimi.ingsw.Client.GUI.Panels.StartPanel;
import it.polimi.ingsw.Controller.Actions.EndTurnOfActionPhase;
import it.polimi.ingsw.Controller.Actions.PlayerAction;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Message;
import it.polimi.ingsw.Server.Messages.ServerResponses.*;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles messages received from server and keep a record of current player's actions executed during its turn
 */
public class GUISocketListener implements Runnable {
    /**
     * JTabbedPane containing all others JPanels
     */
    private final GameInProgressPanel gameInProgressPanel;
    /**
     * List of actions executed by current player and their feedbacks
     */
    private final List<Pair<PlayerAction, PlayerActionFeedback>> requestAndFeedback = new ArrayList<>();
    /**
     * Context received from GameInProgressPanel
     */
    private final Context ctx;
    /**
     * SocketWrapper used to communicate with Server
     */
    private final SocketWrapper sw;
    /**
     * Last player's request sent to Server
     */
    private PlayerAction playerActionRequest;


    /**
     * Create a new GUIReader
     *
     * @param ctx Context containing socket and GUI's window
     */
    public GUISocketListener(GameInProgressPanel gameInProgressPanel, Context ctx) {
        this.gameInProgressPanel = gameInProgressPanel;
        this.playerActionRequest = null;
        this.sw = ctx.getSocketWrapper();
        this.ctx = ctx;
    }

    /**
     * Listen for Server's responses and updated window basing on responses
     */
    @Override
    public void run() {
        while (true) {
            try {
                //wait server's response
                Message input = sw.awaitMessage();
                switch (input) {
                    case LobbyClosed ignored -> {
                        JOptionPane.showMessageDialog(null, "Lobby was closed by the server.\n" +
                                "Client is disconnecting from the server.", "Lobby closed", JOptionPane.INFORMATION_MESSAGE);
                        //close socket and return to StartPanel
                        sw.close();
                        ctx.getWindow().changeView(new StartPanel(ctx));
                        return;
                    }
                    case ClientDisconnected clientDisconnected ->
                            JOptionPane.showMessageDialog(null, "Client " + clientDisconnected.getLastDisconnectedNickname() +
                                    " just disconnected.", "Client disconnected", JOptionPane.INFORMATION_MESSAGE);
                    case ModelUpdated modelUpdated -> {
                        //create a new GameInProgressPanel with updated model
                        ctx.getWindow().changeView(new GameInProgressPanel(ctx, modelUpdated.getModel(), this));
                        return;
                    }
                    case PlayerActionFeedback playerActionFeedback -> {
                        //create a new Pair with saved player's request and feedback received
                        requestAndFeedback.add(new Pair<>(this.playerActionRequest, playerActionFeedback));
                        //show eventual fail report
                        if (playerActionFeedback.getStatusCode() == StatusCode.Fail)
                            JOptionPane.showMessageDialog(null, playerActionFeedback.getReport());
                        //clear history when endTurn action has been performed by user
                        if (playerActionRequest.getClass().equals(EndTurnOfActionPhase.class) && playerActionFeedback.getStatusCode() == StatusCode.Success) {
                            requestAndFeedback.clear();
                        }
                        playerActionRequest = null;
                    }
                    case GameOver ignored -> {
                        return;
                    }
                    case InvalidRequest ignored ->
                            JOptionPane.showMessageDialog(null, "Your request has not been executed, probably you are trying to play out of turn",
                                    "Warning", JOptionPane.INFORMATION_MESSAGE);
                    default -> throw new IllegalStateException("Unexpected value: " + input.getClass());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error in the connection with the server", "Error", JOptionPane.INFORMATION_MESSAGE);
                try {
                    sw.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                ctx.getWindow().changeView(new StartPanel(ctx));
                return;
            }
        }
    }

    /**
     * Save user's request that will be added in history after receiving its feedback
     *
     * @param playerActionRequest playerActionRequest to save
     */
    public void savePlayerActionRequest(PlayerAction playerActionRequest) {
        if (this.playerActionRequest == null) {
            this.playerActionRequest = playerActionRequest;
        }
    }

    /**
     * Check to see if a new {@link it.polimi.ingsw.Server.Messages.Events.Requests.PlayerActionRequest} can be sent to the server
     * or if the gui should wait before allowing any more actions to be sent
     *
     * @return true if the listener is polling for a feedback to a previous player's action, false otherwise
     */
    public boolean awaitingPlayerActionFeedback() {
        return playerActionRequest != null;
    }

    /**
     * Count PlayerActions that have received a successful response from Server
     *
     * @param playerActionClass PlayerAction's class that will be counted
     * @return PlayerAction amount that received a successful response from Server
     */
    public int getSuccessfulRequestsByType(Class<?> playerActionClass) {
        List<PlayerActionFeedback> actions = requestAndFeedback.stream().
                filter(pair -> pair.first().getClass().equals(playerActionClass)).map(Pair::second).toList();

        return (int) actions.stream().filter(playerActionFeedback -> playerActionFeedback.getStatusCode() == StatusCode.Success).count();
    }
}
