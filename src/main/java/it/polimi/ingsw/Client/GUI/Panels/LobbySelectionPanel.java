package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.ConnectLobbyRequest;
import it.polimi.ingsw.Server.Messages.Events.Requests.CreateLobbyRequest;
import it.polimi.ingsw.Server.Messages.Message;
import it.polimi.ingsw.Server.Messages.ServerResponses.LobbyRedirect;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.LobbyInfo;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.UUID;

/**
 * JTabbedPane that allows the user to create or connect to a lobby
 */
public class LobbySelectionPanel extends JTabbedPane {
    /**
     * Create a new LobbySelectionPanel
     *
     * @param ctx           context that will be used by GUI's game's panels
     * @param publicLobbies list of publicLobbies available at that moment
     */
    public LobbySelectionPanel(Context ctx, List<LobbyInfo> publicLobbies) {
        // unwrapping context into useful variables
        Window window = ctx.getWindow();
        SocketWrapper sw = ctx.getSocketWrapper();

        // tabbed pane tabs
        JPanel connectPanel = new JPanel();
        JPanel createPanel = new JPanel();

        // adding tabs to the pane
        this.add("Connect", connectPanel);
        this.add("Create", createPanel);

        // connect tab setup
        {
            // labels
            JLabel title = new JLabel("Select a lobby from the list, or input a lobby id to connect to.");
            JLabel publicLobbiesLabel = new JLabel("These are all the public lobbies you can connect to:");
            JLabel lobbyIDLabel = new JLabel("Connecting to:", SwingConstants.RIGHT);

            // text fields
            JTextField lobbyID = new JTextField(30);

            // buttons
            JButton connect = new JButton("Connect");

            // list cell renderer
            ListCellRenderer<LobbyInfo> cellRenderer = (list, info, index, isSelected, _ignored) -> {
                JLabel displayedText = new JLabel();
                displayedText.setText(
                        "ID: " + info.getID() +
                                " || Admin: " + info.getAdmin() +
                                " || Size: " + (info.getPlayers().size()) + "/" + info.getMaxPlayers());
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

            // list of public lobbies
            JList<LobbyInfo> publicLobbiesList = new JList<>(publicLobbies.toArray(LobbyInfo[]::new));
            publicLobbiesList.setLayoutOrientation(JList.VERTICAL);
            publicLobbiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            publicLobbiesList.setCellRenderer(cellRenderer);

            // wrapping the list of lobbies in a scrollable panel
            JScrollPane scrollablePublicLobbiesList = new JScrollPane();
            scrollablePublicLobbiesList.setViewportView(publicLobbiesList);
            scrollablePublicLobbiesList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollablePublicLobbiesList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollablePublicLobbiesList.setPreferredSize(new Dimension(720, 100));

            // adding all elements to the view
            connectPanel.add(title);
            connectPanel.add(publicLobbiesLabel);
            connectPanel.add(lobbyIDLabel);
            connectPanel.add(lobbyID);
            connectPanel.add(connect);
            connectPanel.add(scrollablePublicLobbiesList);

            // setting correct focus
            scrollablePublicLobbiesList.requestFocusInWindow();

            // actionListeners
            publicLobbiesList.addListSelectionListener(listSelectionEvent -> {
                int selectedIndex = publicLobbiesList.getSelectedIndex();
                lobbyID.setText(publicLobbies.get(selectedIndex).getID().toString());
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
                    boolean again = true;
                    do {
                        Message response = sw.awaitMessage();
                        switch (response) {
                            case LobbyRedirect lobbyRedirect -> {
                                if (lobbyRedirect.getStatusCode() == StatusCode.Success) {
                                    //Switch to a new LobbySelectionPanel if user has been accepted by Server
                                    window.changeView(new GameStartingPanel(ctx, false, lobbyRedirect.getLobbyID()));
                                    again = false;
                                } else {
                                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Try again.", "Error", JOptionPane.INFORMATION_MESSAGE));
                                }
                            }
                            default -> throw new IllegalStateException("Unexpected value: " + response);
                        }
                    } while (again);
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error in the connection with the server", "Error", JOptionPane.INFORMATION_MESSAGE));
                    window.changeView(new StartPanel(ctx));
                }
            });

            // layout object decleration and setup
            SpringLayout layout = new SpringLayout();

            layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 20, SpringLayout.NORTH, connectPanel);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, connectPanel);

            layout.putConstraint(SpringLayout.VERTICAL_CENTER, publicLobbiesLabel, 20, SpringLayout.SOUTH, title);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, publicLobbiesLabel, 0, SpringLayout.HORIZONTAL_CENTER, connectPanel);
            layout.putConstraint(SpringLayout.NORTH, scrollablePublicLobbiesList, 20, SpringLayout.VERTICAL_CENTER, publicLobbiesLabel);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scrollablePublicLobbiesList, 0, SpringLayout.HORIZONTAL_CENTER, connectPanel);

            layout.putConstraint(SpringLayout.VERTICAL_CENTER, lobbyIDLabel, 20, SpringLayout.SOUTH, scrollablePublicLobbiesList);
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
            maxPlayers_3.setActionCommand("3");
            JRadioButton maxPlayers_4 = new JRadioButton("4");
            maxPlayers_4.setActionCommand("4");

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
                    boolean again = true;
                    do {
                        Message response = sw.awaitMessage();
                        switch (response) {
                            case LobbyRedirect lobbyRedirect -> {
                                if (lobbyRedirect.getStatusCode() == StatusCode.Success) {
                                    //Switch to a new LobbySelectionPanel if user has been accepted by Server
                                    window.changeView(new GameStartingPanel(ctx, true, lobbyRedirect.getLobbyID()));
                                    again = false;
                                } else {
                                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Try again.", "Error", JOptionPane.INFORMATION_MESSAGE));
                                }
                            }
                            default -> throw new IllegalStateException("Unexpected value: " + response);
                        }
                    } while (again);
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error in the connection with the server", "Error", JOptionPane.INFORMATION_MESSAGE));
                    window.changeView(new StartPanel(ctx));
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
    }
}
