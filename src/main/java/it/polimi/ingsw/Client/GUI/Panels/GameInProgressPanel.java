package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.GUIReader;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Controller.Actions.ChooseCloudTile;
import it.polimi.ingsw.Controller.Actions.MoveMotherNature;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.Network.SocketWrapper;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameInProgressPanel extends JTabbedPane {

    private final String ownNickname;
    private final Window window;

    private final Map<String, PlayerBoardPanel> playerTabs = new HashMap<>();
    private final SocketWrapper sw;
    private GUIReader guiReader;

    public GameInProgressPanel(Context ctx) {
        // unwrapping context into useful variables
        this.ownNickname = ctx.getNickname();
        this.window = ctx.getWindow();
        this.sw = ctx.getSocketWrapper();
        this.window.changeView(this);
        this.guiReader = new GUIReader(this, ctx);
        System.out.println(guiReader);
        Thread readerThread = new Thread(guiReader);
        readerThread.start();
    }

    public GameInProgressPanel(Context ctx, Model model, GUIReader guiReader) {
        this(ctx, guiReader);
        this.removeAll();
        this.add("Islands", new IslandFieldPanel(model, sw, guiReader));
        for (PlayerBoard pb : model.getMutablePlayerBoards()) {
            this.playerTabs.put(pb.getNickname(), new PlayerBoardPanel(pb, model, this.sw, guiReader));
        }
        for (Map.Entry<String, PlayerBoardPanel> pbp : this.playerTabs.entrySet()) {
            if (pbp.getKey().equals(this.ownNickname)) {
                if (pbp.getKey().equals(model.getMutableTurnOrder().getMutableCurrentPlayer().getNickname()))
                    this.add("Your board (current player)", pbp.getValue());
                else
                    this.add("Your board", pbp.getValue());
            } else {
                if (pbp.getKey().equals(model.getMutableTurnOrder().getMutableCurrentPlayer().getNickname()))
                    this.add(pbp.getKey() + "'s PlayerBoard (current player)", pbp.getValue());
                else
                    this.add(pbp.getKey() + "'s PlayerBoard", pbp.getValue());
            }
        }
        this.add("Clouds", new CloudPanel(model.getClouds(), model.getMutableTurnOrder().getMutableCurrentPlayer(), guiReader, sw));
        if (model.getGameMode() == GameMode.ADVANCED) {
            final JPanel characterCardsPanel = new CharacterCardsPanel(model, guiReader);
            this.add("CharacterCards", characterCardsPanel);
        }
        if(guiReader.getSuccessfulRequestsByType(MoveMotherNature.class)==1){
            this.setSelectedIndex(this.getTabCount()-1);
            this.getSelectedComponent();
        }
    }


    public GameInProgressPanel(Context ctx, GUIReader guiReader) {
        this.ownNickname = ctx.getNickname();
        this.window = ctx.getWindow();
        this.sw = ctx.getSocketWrapper();
        this.window.changeView(this);
        this.guiReader = guiReader;
        Thread readerThread = new Thread(guiReader);
        readerThread.start();
    }

    public void enableGUIComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableGUIComponents((Container) component, enable);
            }
        }
    }
}
