package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.PopupMessage;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Message;
import it.polimi.ingsw.Server.Messages.ServerResponses.*;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameInProgressPanel extends JTabbedPane {

    private final String ownNickname;
    private final Window window;

    private final Map<String, PlayerBoardPanel> playerTabs = new HashMap<>();

    private final SocketWrapper sw;

    public GameInProgressPanel(Context ctx) {
        // unwrapping context into useful variables
        this.ownNickname = ctx.getNickname();
        this.window = ctx.getWindow();
        this.sw = ctx.getSocketWrapper();// display the view
        this.window.changeView(this);

        // start socket listener task
        new Thread(() -> {
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
                        case ClientDisconnected clientDisconnected -> {
                            new PopupMessage("Client " + clientDisconnected.getLastDisconnectedNickname() +
                                    "just disconnected.", "Client disconnected");
                        }
                        case ModelUpdated modelUpdated -> {
                            this.updateViews(modelUpdated.getModel());
                        }
                        case PlayerActionFeedback playerActionFeedback -> {
                            if (playerActionFeedback.getStatusCode() == StatusCode.Fail)
                                JOptionPane.showMessageDialog(null, playerActionFeedback.getReport());
                        }
                        case InvalidRequest invalidRequest -> {
                            JOptionPane.showMessageDialog(null, "Your request has not been executed, probably you are trying to play out of turn");
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + input.getClass());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void updateViews(Model model) {
        this.removeAll();
        this.add("Islands", new IslandFieldPanel(model.getMutableIslandField().getMutableGroups(), model.getMutableIslandField().getMutableMotherNaturePosition()));
        for (PlayerBoard pb : model.getMutablePlayerBoards()) {
            this.playerTabs.put(pb.getNickname(), new PlayerBoardPanel(pb, model.getOwnTeachers(pb), model.getTeamMapper().getMutableTowerStorage(pb), model.getMutableTurnOrder(), this.sw));
        }
        for (Map.Entry<String, PlayerBoardPanel> pbp : this.playerTabs.entrySet()) {
            if (pbp.getKey().equals(this.ownNickname)) {
                if(pbp.getKey().equals(model.getMutableTurnOrder().getMutableCurrentPlayer().getNickname()))
                    this.add("Your board (current player)", pbp.getValue());
                else
                    this.add("Your board", pbp.getValue());
            } else {
                if(pbp.getKey().equals(model.getMutableTurnOrder().getMutableCurrentPlayer().getNickname()))
                    this.add(pbp.getKey() + "'s PlayerBoard (current player)", pbp.getValue());
                else
                    this.add(pbp.getKey() + "'s PlayerBoard", pbp.getValue());
            }
        }
        this.add("Clouds", new CloudPanel(model.getClouds()));
        if (model.getGameMode() == GameMode.ADVANCED) {
            final JPanel characterCardsPanel = new CharacterCardsPanel(model.getCharacterCards());
            this.add("CharacterCards", characterCardsPanel);
        }
        // display the view
        this.window.changeView(this);
    }
}
