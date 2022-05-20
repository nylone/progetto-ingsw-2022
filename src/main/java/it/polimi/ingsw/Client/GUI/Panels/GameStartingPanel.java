package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.PopupMessage;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.StartGameRequest;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.*;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameStartingPanel extends JPanel {
    public GameStartingPanel(Context ctx, boolean isAdmin) {
        // unwrapping context into useful variables
        Window window = ctx.getWindow();
        SocketWrapper sw = ctx.getSocketWrapper();

        // labels
        JLabel title = new JLabel("Connected to the lobby");
        JLabel connectedPlayersLablel = new JLabel("Players in lobby:");

        // buttons
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

        disconnect.addActionListener(actionEvent -> {
            sw.close();
            new PopupMessage("Disconnected", "Disconnected from server");
            new StartPanel(ctx);
        });

        start.addActionListener(actionEvent -> {
            try {
                sw.sendMessage(new StartGameRequest(GameMode.ADVANCED));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // layout object decleration and setup
        SpringLayout layout = new SpringLayout();

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 20, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.NORTH, connectedPlayersLablel, 20, SpringLayout.SOUTH, title);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, connectedPlayersLablel, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, connectedPlayersList, 20, SpringLayout.SOUTH, connectedPlayersLablel);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, connectedPlayersList, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.NORTH, start, 20, SpringLayout.SOUTH, connectedPlayersList);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, start, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, disconnect, 20, SpringLayout.SOUTH, start);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, disconnect, 0, SpringLayout.HORIZONTAL_CENTER, this);

        // apply layout
        this.setLayout(layout);

        // display the view
        window.changeView(this);

        // start socket listener task
        new Thread(() -> {
            while (true) {
                try {
                    switch (sw.awaitMessage()) {
                        case LobbyClosed ignored -> {
                            new PopupMessage("Lobby was closed by the server.\n" +
                                    "Client is disconnecting from the server.",  "Lobby closed");
                            sw.close();
                            new StartPanel(ctx);
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
                                new PopupMessage("Failure",  gameInit.getErrorMessage());
                            } else {
                                new PopupMessage("Success", "Game is now starting");
                            }
                        }
                        case GameStarted ignored -> {
                            new PopupMessage("unimplemented", "unimplemented");
                            return;
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + sw.awaitMessage());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
