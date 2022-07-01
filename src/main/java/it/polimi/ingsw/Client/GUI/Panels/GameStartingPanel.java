package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.StartGameRequest;
import it.polimi.ingsw.Server.Messages.Message;
import it.polimi.ingsw.Server.Messages.ServerResponses.*;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.UUID;

/**
 * Panel that allows lobby's admin to start the game (is also possible to select gameMode) after it has been filled
 */
public class GameStartingPanel extends JPanel {
    /**
     * Create a new GameStartingPanel
     *
     * @param ctx     context that will be used by GUI's game's panels
     * @param isAdmin GameStartingPanel has been created by lobby's admin
     * @param lobbyID Lobby's UUID
     */
    public GameStartingPanel(Context ctx, boolean isAdmin, UUID lobbyID) {
        // unwrapping context into useful variables
        Window window = ctx.getWindow();
        SocketWrapper sw = ctx.getSocketWrapper();

        // labels
        JLabel title = new JLabel("Connected to the lobby");
        JLabel lobbyIDlabel = new JLabel("Connected to the lobby");
        JLabel connectedPlayersLablel = new JLabel("Players in lobby:");
        JLabel gameModeLabel = new JLabel("Advanced mode:");

        // text boxes
        JTextField lobbyIDField = new JTextField();
        lobbyIDField.setText(lobbyID.toString());
        lobbyIDField.setEditable(false);

        // buttons
        JCheckBox gameMode = new JCheckBox();
        gameMode.setEnabled(isAdmin);
        gameMode.setToolTipText("Only the lobby admin can select the game mode");
        JButton disconnect = new JButton("Disconnect from the lobby");
        JButton start = new JButton("Start the game");
        start.setToolTipText("Only the lobby admin can start the lobby");
        start.setEnabled(isAdmin); // start button is enabled only for admin of the lobby

        // list cell renderer
        ListCellRenderer<String> cellRenderer = (list, nick, index, isSelected, _ignored) -> {
            JLabel displayedText = new JLabel();
            displayedText.setText(nick);
            //enable this code to let selections be shown
            if (isSelected) {
                displayedText.setBackground(list.getSelectionBackground());
                displayedText.setForeground(list.getSelectionForeground());
            } else {
                displayedText.setBackground(list.getBackground());
                displayedText.setForeground(list.getForeground());
            }
            displayedText.setOpaque(true);
            return displayedText;
        };

        // list of connected players
        JList<String> connectedPlayersList = new JList<>(new String[]{});
        connectedPlayersList.setLayoutOrientation(JList.VERTICAL);
        connectedPlayersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        connectedPlayersList.setCellRenderer(cellRenderer);
        connectedPlayersList.setPreferredSize(new Dimension(720, 100));

        // adding all elements to the view
        this.add(title);
        this.add(disconnect);
        this.add(start);
        this.add(connectedPlayersLablel);
        this.add(connectedPlayersList);
        this.add(lobbyIDlabel);
        this.add(lobbyIDField);
        this.add(gameModeLabel);
        this.add(gameMode);

        disconnect.addActionListener(actionEvent -> {
            try {
                sw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        start.addActionListener(actionEvent -> {
            try {
                sw.sendMessage(new StartGameRequest(gameMode.isSelected() ? GameMode.ADVANCED : GameMode.SIMPLE));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // layout object declaration and setup
        SpringLayout layout = new SpringLayout();

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 20, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.NORTH, lobbyIDlabel, 20, SpringLayout.SOUTH, title);
        layout.putConstraint(SpringLayout.EAST, lobbyIDlabel, -10, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lobbyIDField, 0, SpringLayout.VERTICAL_CENTER, lobbyIDlabel);
        layout.putConstraint(SpringLayout.WEST, lobbyIDField, 10, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.NORTH, connectedPlayersLablel, 20, SpringLayout.SOUTH, lobbyIDField);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, connectedPlayersLablel, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, connectedPlayersList, 20, SpringLayout.SOUTH, connectedPlayersLablel);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, connectedPlayersList, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.NORTH, gameModeLabel, 20, SpringLayout.SOUTH, connectedPlayersList);
        layout.putConstraint(SpringLayout.EAST, gameModeLabel, -10, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, gameMode, 0, SpringLayout.VERTICAL_CENTER, gameModeLabel);
        layout.putConstraint(SpringLayout.WEST, gameMode, 10, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, start, 20, SpringLayout.SOUTH, gameMode);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, start, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, disconnect, 20, SpringLayout.SOUTH, start);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, disconnect, 0, SpringLayout.HORIZONTAL_CENTER, this);

        // apply layout
        this.setLayout(layout);

        // start socket listener task
        new Thread(() -> {
            while (true) {
                try {
                    Message input = sw.awaitMessage();
                    switch (input) {
                        case LobbyClosed ignored -> {
                            JOptionPane.showMessageDialog(null, "Lobby was closed by the server.\n" +
                                    "Client is disconnecting from the server.", "Lobby closed", JOptionPane.INFORMATION_MESSAGE);
                            sw.close();
                            window.changeView(new StartPanel(ctx));
                        }
                        case ClientConnected clientConnected -> {
                            synchronized (connectedPlayersList) {
                                connectedPlayersList.setListData(clientConnected.getPlayers().toArray(String[]::new));
                            }
                        }
                        case ClientDisconnected clientDisconnected -> {
                            synchronized (connectedPlayersList) {
                                connectedPlayersList.setListData(clientDisconnected.getPlayers().toArray(String[]::new));
                            }
                        }
                        case GameInit gameInit -> {
                            if (gameInit.getStatusCode() == StatusCode.Fail) {
                                JOptionPane.showMessageDialog(null, gameInit.getErrorMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        case GameStarted ignored -> {
                            window.changeView(new GameInProgressPanel(ctx));
                            return;
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + input.getClass());
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Disconnected from server", "Error", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        sw.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    window.changeView(new StartPanel(ctx));
                    return;
                }
            }
        }).start();
    }
}
