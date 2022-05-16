package it.polimi.ingsw.Client.GUI.Panels;

import com.google.gson.Gson;
import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.PopupMessage;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Misc.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.ConnectLobbyRequest;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.CreateLobbyRequest;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.LobbyRedirect;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.StatusCode;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class LobbySelectionPanel extends JTabbedPane {
    public LobbySelectionPanel(Context ctx) {
        // unwrapping context into useful variables
        Window window = ctx.getWindow();
        SocketWrapper sw = ctx.getSocketWrapper();
        List<Pair<UUID, String>> openLobbies = ctx.getOpenLobbies();

        // tabbed pane tabs
        JPanel connectPanel = new JPanel();
        JPanel createPanel = new JPanel();

        // adding tabs to the pane
        this.add("Connect", connectPanel);
        this.add("Create", createPanel);

        // connect tab setup
        {
            // labels
            JLabel title = new JLabel("Select a lobby");
            JLabel lobbyIDLabel = new JLabel("Connecting to:", SwingConstants.RIGHT);

            // text fields
            JTextField lobbyID = new JTextField(30);

            // buttons
            JButton connect = new JButton("Connect");

            // list of lobbies
            JList lobbies = new JList(ctx.getOpenLobbies().toArray());
            lobbies.setLayoutOrientation(JList.VERTICAL);
            lobbies.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            lobbies.setCellRenderer((list, entry, index, isSelected, hasCellFocus) -> {
                JLabel displayedText = new JLabel();
                Pair<UUID, String> pair = (Pair<UUID, String>) entry;
                displayedText.setText("ID: " + pair.getFirst() + " || Admin: " + pair.getSecond());
                if (isSelected) {
                    displayedText.setBackground(list.getSelectionBackground());
                    displayedText.setForeground(list.getSelectionForeground());
                } else {
                    displayedText.setBackground(list.getBackground());
                    displayedText.setForeground(list.getForeground());
                }
                displayedText.setOpaque(true);
                return displayedText;
            });

            // wrapping the list of lobbies in a scrollable panel
            JScrollPane scrollLobbies = new JScrollPane();
            scrollLobbies.setViewportView(lobbies);
            scrollLobbies.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollLobbies.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollLobbies.setPreferredSize(new Dimension(720, 100));

            // adding all elements to the view
            connectPanel.add(title);
            connectPanel.add(lobbyIDLabel);
            connectPanel.add(lobbyID);
            connectPanel.add(connect);
            connectPanel.add(scrollLobbies);

            // setting correct focus
            scrollLobbies.requestFocusInWindow();

            // actionListeners
            lobbies.addListSelectionListener(listSelectionEvent -> {
                int selectedIndex = lobbies.getSelectedIndex();
                lobbyID.setText(openLobbies.get(selectedIndex).getFirst().toString());
            });
            lobbyID.addActionListener(actionEvent -> {
                lobbyID.setText(lobbyID.getText().trim());
                connect.requestFocusInWindow();
            });
            connect.addActionListener(actionEvent -> {
                // normalize id
                String idString = lobbyID.getText().trim();
                lobbyID.setText(idString);
                UUID id = UUID.fromString(idString);
                try {
                    sw.sendMessage(new ConnectLobbyRequest(id));
                    Message response = sw.awaitMessage();
                    if (response.getType() == PayloadType.RESPONSE_LOBBY_REDIRECT &&
                            new Gson().fromJson(response.getData(), LobbyRedirect.class).getStatusCode() == StatusCode.Success) {
                        new PopupMessage("Success!", "Success!");
                    } else {
                        new PopupMessage("Try again.", "Failure :(");
                    }
                } catch (Exception e) {
                    new PopupMessage("Try again.", "Failure :(");
                }
            });

            // layout object decleration and setup
            SpringLayout layout = new SpringLayout();

            layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 20, SpringLayout.NORTH, connectPanel);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, connectPanel);

            layout.putConstraint(SpringLayout.NORTH, scrollLobbies, 20, SpringLayout.VERTICAL_CENTER, title);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scrollLobbies, 0, SpringLayout.HORIZONTAL_CENTER, connectPanel);

            layout.putConstraint(SpringLayout.VERTICAL_CENTER, lobbyIDLabel, 20, SpringLayout.SOUTH, scrollLobbies);
            layout.putConstraint(SpringLayout.EAST, lobbyIDLabel, -10, SpringLayout.HORIZONTAL_CENTER, connectPanel);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, lobbyID, 0, SpringLayout.VERTICAL_CENTER, lobbyIDLabel);
            layout.putConstraint(SpringLayout.WEST, lobbyID, 10, SpringLayout.HORIZONTAL_CENTER, connectPanel);

            layout.putConstraint(SpringLayout.VERTICAL_CENTER, connect, 40, SpringLayout.VERTICAL_CENTER, lobbyIDLabel);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, connect, 0, SpringLayout.HORIZONTAL_CENTER, connectPanel);

            // apply layout
            connectPanel.setLayout(layout);
        }

        // create tab setup
        {
            // labels
            JLabel title = new JLabel("Create a lobby");
            JLabel openLobbyLabel = new JLabel("Public lobby:", SwingConstants.RIGHT);
            JLabel maxPlayersLabel = new JLabel("Max Players:", SwingConstants.RIGHT);


            // buttons
            JButton create = new JButton("Create");
            JCheckBox openLobby = new JCheckBox();
            JRadioButton maxPlayers_2 = new JRadioButton("2");
            maxPlayers_2.setActionCommand("2");
            JRadioButton maxPlayers_3 = new JRadioButton("3");
            maxPlayers_2.setActionCommand("3");
            JRadioButton maxPlayers_4 = new JRadioButton("4");
            maxPlayers_2.setActionCommand("4");

            // radio buttons get grouped up
            ButtonGroup maxPlayers = new ButtonGroup();
            maxPlayers_2.setSelected(true);
            maxPlayers.add(maxPlayers_2);
            maxPlayers.add(maxPlayers_3);
            maxPlayers.add(maxPlayers_4);

            // adding all elements to the view
            createPanel.add(title);
            createPanel.add(openLobbyLabel);
            createPanel.add(maxPlayersLabel);
            createPanel.add(create);
            createPanel.add(openLobby);
            createPanel.add(maxPlayers_2);
            createPanel.add(maxPlayers_3);
            createPanel.add(maxPlayers_4);

            create.addActionListener(actionEvent -> {
                // normalize id
                try {

                    sw.sendMessage(new CreateLobbyRequest(
                            openLobby.isSelected(),
                            Integer.parseInt(maxPlayers.getSelection().getActionCommand())
                    ));
                    Message response = sw.awaitMessage();
                    if (response.getType() == PayloadType.RESPONSE_LOBBY_REDIRECT &&
                            new Gson().fromJson(response.getData(), LobbyRedirect.class).getStatusCode() == StatusCode.Success) {
                        new PopupMessage("Success!", "Success!");
                    } else {
                        new PopupMessage("Try again.", "Failure :(");
                    }
                } catch (Exception e) {
                    new PopupMessage("Try again.", "Failure :(");
                }
            });

            // layout object decleration and setup
            SpringLayout layout = new SpringLayout();

            layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 20, SpringLayout.NORTH, createPanel);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, createPanel);

            layout.putConstraint(SpringLayout.VERTICAL_CENTER, openLobbyLabel, 20, SpringLayout.SOUTH, title);
            layout.putConstraint(SpringLayout.EAST, openLobbyLabel, -10, SpringLayout.HORIZONTAL_CENTER, createPanel);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, openLobby, 0, SpringLayout.VERTICAL_CENTER, openLobbyLabel);
            layout.putConstraint(SpringLayout.WEST, openLobby, 10, SpringLayout.HORIZONTAL_CENTER, createPanel);

            layout.putConstraint(SpringLayout.VERTICAL_CENTER, maxPlayersLabel, 20, SpringLayout.SOUTH, openLobbyLabel);
            layout.putConstraint(SpringLayout.EAST, maxPlayersLabel, -10, SpringLayout.HORIZONTAL_CENTER, createPanel);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, maxPlayers_2, 0, SpringLayout.VERTICAL_CENTER, maxPlayersLabel);
            layout.putConstraint(SpringLayout.WEST, maxPlayers_2, 10, SpringLayout.HORIZONTAL_CENTER, createPanel);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, maxPlayers_3, 0, SpringLayout.VERTICAL_CENTER, maxPlayersLabel);
            layout.putConstraint(SpringLayout.WEST, maxPlayers_3, 10, SpringLayout.EAST, maxPlayers_2);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, maxPlayers_4, 0, SpringLayout.VERTICAL_CENTER, maxPlayersLabel);
            layout.putConstraint(SpringLayout.WEST, maxPlayers_4, 10, SpringLayout.EAST, maxPlayers_3);

            layout.putConstraint(SpringLayout.VERTICAL_CENTER, create, 40, SpringLayout.VERTICAL_CENTER, maxPlayersLabel);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, create, 0, SpringLayout.HORIZONTAL_CENTER, createPanel);

            // apply layout
            createPanel.setLayout(layout);
        }

        // display the view
        window.changeView(this);
    }
}
