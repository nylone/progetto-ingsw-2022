package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.GUIReader;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Controller.Actions.MoveMotherNature;
import it.polimi.ingsw.Controller.Actions.MoveStudent;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.Network.SocketWrapper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class container of all others Panel inside a JTabbedPane
 */
public class GameInProgressPanel extends JTabbedPane {

    /**
     * nickname of GUI's player
     */
    private final String ownNickname;
    /**
     * Frame containing all others components
     */
    private final Window window;

    /**
     * SocketWrapper used to communicate with Server
     */
    private final SocketWrapper sw;
    /**
     * Contains GuiReader's information necessary to record user's requests during his turn
     */
    private final GUIReader guiReader;

    /**
     * Constructor that should be called only upon the creation of first GameInProgressPanel
     *
     * @param ctx Context to use during the game
     */
    public GameInProgressPanel(Context ctx) {
        // unwrapping context into useful variables
        this.ownNickname = ctx.getNickname();
        this.window = ctx.getWindow();
        this.sw = ctx.getSocketWrapper();
        this.window.changeView(this);
        this.guiReader = new GUIReader(this, ctx);
        //run GuiReader thread
        Thread readerThread = new Thread(guiReader);
        readerThread.start();
    }

    /**
     * Should be used upon the second creation of GameInProgressPanel
     *
     * @param ctx       Context to use during the game
     * @param guiReader created upon first gameInProgressPanel creation
     */
    private GameInProgressPanel(Context ctx, GUIReader guiReader) {
        this.ownNickname = ctx.getNickname();
        this.window = ctx.getWindow();
        this.sw = ctx.getSocketWrapper();
        this.window.changeView(this);
        this.guiReader = guiReader;
        Thread readerThread = new Thread(guiReader);
        readerThread.start();
    }

    /**
     * Public constructor to create GameInProgressPanel's object starting from the second creation; it should never be called the first time
     *
     * @param ctx       Context to use during the game
     * @param model     Model containing all game's information
     * @param guiReader created upon first gameInProgressPanel creation
     */
    public GameInProgressPanel(Context ctx, Model model, GUIReader guiReader) {
        this(ctx, guiReader);
        this.removeAll();
        if (!model.isGameOver()) {
            //add IslandFieldPanel to JTabbedPane
            this.add("Islands", new IslandFieldPanel(model, sw, guiReader));
            Map<String, PlayerBoardPanel> playerTabs = new HashMap<>();
            ArrayList<String> tooltips = new ArrayList<>(model.getMutablePlayerBoards().size());
            //set ToolTip font
            UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 15));
            for (PlayerBoard pb : model.getMutablePlayerBoards()) {
                //for all playerBoards create and add a PlayerBoardPanel to JTabbedPane
                playerTabs.put(pb.getNickname(), new PlayerBoardPanel(pb, model, this.sw, guiReader));
            }
            for (Map.Entry<String, PlayerBoardPanel> pbp : playerTabs.entrySet()) {
                //set a proper name to playerBoardPanel's tab inside jTabbedPane
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
                PlayerBoard pb = null;
                try {
                    pb = model.getMutablePlayerBoardByNickname(pbp.getValue().getPlayerBoardNickname());
                } catch (InvalidContainerIndexException e) {
                    e.printStackTrace();
                }
                String tooltip;
                //create ToolTip's string for all playerBoardPanels' tabs (containing assistantCard that has been played and eventual coin balance)
                if (model.getMutableTurnOrder().getMutableSelectedCard(pb).isPresent()) {
                    tooltip = "<html>Played assistant card: #" + model.getMutableTurnOrder().getMutableSelectedCard(pb).get().getPriority() + "<br>";
                } else {
                    tooltip = "<html>No assistant card has been played<br>";
                }
                if (model.getGameMode() == GameMode.ADVANCED)
                    tooltip = tooltip + "Available coins:" + (pb != null ? pb.getCoinBalance() : 0);
                tooltip = tooltip + "</html>";
                tooltips.add(tooltip);
            }
            //add ToolTip to all playerBoardPanels' tabs
            for (int i = 1; i <= tooltips.size(); i++) {
                this.setToolTipTextAt(i, tooltips.get(i - 1));
            }
            //If the game is an advanced game then create and add a new CharacterCardsPanel to JTabbedPane
            if (model.getGameMode() == GameMode.ADVANCED) {
                final JPanel characterCardsPanel = new CharacterCardsPanel(model, sw, guiReader);
                this.add("CharacterCards", characterCardsPanel);
            }
            //create and add CloudPanel to JTabbedPane
            this.add("Clouds", new CloudPanel(model.getClouds(), model.getMutableTurnOrder().getMutableCurrentPlayer(), guiReader, sw));
            //set JTabbedPane to last tab whether the user has moved MotherNature (las tab is always the CloudPanel)
            if (guiReader.getSuccessfulRequestsByType(MoveMotherNature.class) == 1) {
                this.setSelectedIndex(this.getTabCount() - 1);
                this.getSelectedComponent();
            }
            if (model.getMutableTurnOrder().getMutableCurrentPlayer().getNickname().equals(ownNickname)) {
                if (guiReader.getSuccessfulRequestsByType(PlayAssistantCard.class) == 0
                        || (guiReader.getSuccessfulRequestsByType(PlayAssistantCard.class) == 1 && guiReader.getSuccessfulRequestsByType(MoveStudent.class) == 0)
                ) {
                    JLabel resLabel = new JLabel("It's your turn!!");
                    resLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
                    JOptionPane.showMessageDialog(this.getParent(), resLabel, "Turn change", JOptionPane.PLAIN_MESSAGE);
                }
            }
        } else {
            this.add("WINNERS", new EndGamePanel(model.getWinners().get(), ctx));
        }
    }

    /**
     * Method to disable/enable all GUI's components; necessary for blocking GUI while waiting for Server's response
     *
     * @param container Container to disable/enable
     * @param enable    <p>TRUE --> enable all Container's components</p>
     *                  <p>FALSE --> disable all Container's components</p>
     */
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
