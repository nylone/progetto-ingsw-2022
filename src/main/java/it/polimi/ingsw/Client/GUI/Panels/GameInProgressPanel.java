package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.PopupMessage;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.ModelReader;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Message;
import it.polimi.ingsw.Server.Messages.ServerResponses.ClientDisconnected;
import it.polimi.ingsw.Server.Messages.ServerResponses.LobbyClosed;
import it.polimi.ingsw.Server.Messages.ServerResponses.ModelUpdated;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameInProgressPanel extends JTabbedPane {

    private final String ownNickname;
    private final Window window;
    private final JPanel islandPanel = new JPanel();

    private final Map<String, PlayerBoardPanel> playerTabs = new HashMap<>();

    public GameInProgressPanel(Context ctx) {
        // unwrapping context into useful variables
        this.ownNickname = ctx.getNickname();
        this.window = ctx.getWindow();
        SocketWrapper sw = ctx.getSocketWrapper();

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
                        default -> throw new IllegalStateException("Unexpected value: " + input.getClass());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void updateViews(ModelReader model) {
        this.removeAll();
        this.add("Islands", this.islandPanel);
        for (PlayerBoard pb : model.getPlayerBoards()) {
            this.playerTabs.put(pb.getNickname(), new PlayerBoardPanel(pb));
        }
        for (Map.Entry<String, PlayerBoardPanel> pbp : this.playerTabs.entrySet()) {
            if (pbp.getKey().equals(this.ownNickname)) {
                this.add("Your board", pbp.getValue());
            } else {
                this.add("Player " + pbp.getKey() + "'s board", pbp.getValue());
            }
        }
        if (model.getGameMode() == GameMode.ADVANCED) {
            final JPanel characterCardsPanel = new CharacterCardsPanel(model.getCharacterCards());
            this.add("CharacterCards", characterCardsPanel);
        }
        // display the view
        this.window.changeView(this);
    }
}
