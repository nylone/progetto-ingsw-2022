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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIReader implements Runnable{

    private GameInProgressPanel gameInProgressPanel;

    private List<Pair<PlayerAction,PlayerActionFeedback>> requestAndFeedback = new ArrayList<>();

    private PlayerAction playerActionRequest;

    private Context ctx;

    private SocketWrapper sw;


    public GUIReader(GameInProgressPanel gameInProgressPanel, Context ctx){
        this.gameInProgressPanel = gameInProgressPanel;
        this.sw = ctx.getSocketWrapper();
        this.ctx = ctx;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message input = sw.awaitMessage();
                switch (input) {
                    case LobbyClosed ignored -> {
                        new PopupMessage("Lobby was closed by the server.\n" +
                                "Client is disconnecting from the server.", "Lobby closed");
                        sw.close();
                        new StartPanel(ctx);
                    }
                    case ClientDisconnected clientDisconnected -> new PopupMessage("Client " + clientDisconnected.getLastDisconnectedNickname() +
                            "just disconnected.", "Client disconnected");
                    case ModelUpdated modelUpdated -> {
                        ctx.getWindow().changeView(new GameInProgressPanel(ctx, modelUpdated.getModel(), this));
                        return;
                    }
                    case PlayerActionFeedback playerActionFeedback -> {
                        requestAndFeedback.add(new Pair<>(this.playerActionRequest, playerActionFeedback));
                        if (playerActionFeedback.getStatusCode() == StatusCode.Fail)
                            JOptionPane.showMessageDialog(null, playerActionFeedback.getReport());
                        enableGUIComponents(gameInProgressPanel, true);
                        if(playerActionRequest.getClass().equals(EndTurnOfActionPhase.class) && playerActionFeedback.getStatusCode()==StatusCode.Success){
                            requestAndFeedback.clear();
                        }
                    }
                    case InvalidRequest invalidRequest -> JOptionPane.showMessageDialog(null, "Your request has not been executed, probably you are trying to play out of turn");
                    default -> throw new IllegalStateException("Unexpected value: " + input.getClass());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void savePlayerActionRequest(PlayerAction playerActionRequest){
        this.playerActionRequest = playerActionRequest;
    }

    public int getSuccessfulRequestsByType(Class<?> playerActionClass){
        return (int) requestAndFeedback.stream().
                filter(pair -> pair.getFirst().getClass().equals(playerActionClass) && pair.getSecond().getStatusCode()== StatusCode.Success).count();
    }

    public void enableGUIComponents(JComponent jComponent, boolean enable){
        gameInProgressPanel.enableGUIComponents(jComponent, enable);
    }
}
