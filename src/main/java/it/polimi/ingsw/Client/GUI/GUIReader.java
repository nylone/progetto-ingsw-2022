package it.polimi.ingsw.Client.GUI;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Handles messages received from server and keep a record of current player's actions executed during its turn
 */
public class GUIReader implements Runnable {

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
     * @param gameInProgressPanel GameInProgressPanel that call the constructor
     * @param ctx                 Context containing socket and GUI's window
     */
    public GUIReader(GameInProgressPanel gameInProgressPanel, Context ctx) {
        this.gameInProgressPanel = gameInProgressPanel;
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
                    case HeartBeatResponse ignored -> {
                    }
                    case LobbyClosed ignored -> {
                        new PopupMessage("Lobby was closed by the server.\n" +
                                "Client is disconnecting from the server.", "Lobby closed");
                        //close socket and return to StartPanel
                        sw.close();
                        new StartPanel(ctx);
                        return;
                    }
                    case ClientDisconnected clientDisconnected ->
                            new PopupMessage("Client " + clientDisconnected.getLastDisconnectedNickname() +
                                    " just disconnected.", "Client disconnected");
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
                            JOptionPane.showMessageDialog(ctx.getWindow().getFrame(), playerActionFeedback.getReport());
                        //enable all GUI components
                        gameInProgressPanel.enableGUIComponents(gameInProgressPanel, true);
                        //clear history when endTurn action has been performed by user
                        if (playerActionRequest.getClass().equals(EndTurnOfActionPhase.class) && playerActionFeedback.getStatusCode() == StatusCode.Success) {
                            requestAndFeedback.clear();
                        }
                    }
                    case GameOver gameOver -> {
                        return;
                    }
                    case InvalidRequest ignored ->
                            JOptionPane.showMessageDialog(null, "Your request has not been executed, probably you are trying to play out of turn");
                    default -> throw new IllegalStateException("Unexpected value: " + input.getClass());
                }
            } catch (Exception e) {
                new PopupMessage("Error in the connection with the server", "Failure :(");
                new StartPanel(ctx);
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
        this.playerActionRequest = playerActionRequest;
    }

    /**
     * Count PlayerActions that have received a successful response from Server
     *
     * @param playerActionClass PlayerAction's class that will be counted
     * @return PlayerAction amount that received a successful response from Server
     */
    public int getSuccessfulRequestsByType(Class<?> playerActionClass) {
        List<PlayerActionFeedback> actions = requestAndFeedback.stream().
                filter(pair -> pair.getFirst().getClass().equals(playerActionClass)).map(Pair::getSecond).toList();

        return (int) actions.stream().filter(playerActionFeedback -> playerActionFeedback.getStatusCode() == StatusCode.Success).count();
    }
}
