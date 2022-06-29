package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.PopupMessage;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.DeclarePlayerRequest;
import it.polimi.ingsw.Server.Messages.Message;
import it.polimi.ingsw.Server.Messages.ServerResponses.HeartBeatResponse;
import it.polimi.ingsw.Server.Messages.ServerResponses.LobbyAccept;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import javax.swing.*;

/**
 * Panel that allows user to choose his username
 */
public class UserCredentialsPanel extends JPanel {
    /**
     * Create a new UserCredentialsPanel
     *
     * @param ctx context received form StartPanel
     */
    public UserCredentialsPanel(Context ctx) {
        // unwrapping context into useful variables
        SocketWrapper sw = ctx.getSocketWrapper();

        // labels
        JLabel title = new JLabel("Login");
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.RIGHT);

        // text fields
        JTextField username = new JTextField("guest" + (int) (Math.random() * 1000), 10);

        // buttons
        JButton login = new JButton("Login");

        // adding all elements to the view
        this.add(title);
        this.add(usernameLabel);
        this.add(username);
        this.add(login);

        // setting correct focus
        username.requestFocusInWindow();

        // actionListeners
        username.addActionListener((actionEvent -> {
            username.setText(username.getText().trim());
            login.requestFocusInWindow();
        }));
        login.addActionListener(actionEvent -> {
            // normalize username
            ctx.setNickname(username.getText().trim());
            username.setText(ctx.getNickname());
            try {
                sw.sendMessage(new DeclarePlayerRequest(ctx.getNickname()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Thread(() -> {
                try {
                    boolean again = true;
                    do {
                        Message response = sw.awaitMessage();
                        switch (response) {
                            case HeartBeatResponse ignored -> {
                            }
                            case LobbyAccept lobbyAccept -> {
                                if (lobbyAccept.getStatusCode() == StatusCode.Success) {
                                    //Switch to a new LobbySelectionPanel if user has been accepted by Server
                                    ctx.getWindow().changeView(new LobbySelectionPanel(ctx, lobbyAccept.getPublicLobbies()));
                                    again = false;
                                } else {
                                    new PopupMessage("Server denied your login", "Failure :(");
                                }
                            }
                            default -> throw new IllegalStateException("Unexpected value: " + response);
                        }
                    } while (again);
                } catch (Exception e) {
                    new PopupMessage("Error in the connection with the server", "Failure :(");
                    new StartPanel(ctx);
                }
            }).start();
        });

        // layout object declaration and setup
        SpringLayout layout = new SpringLayout();

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 20, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, usernameLabel, 20, SpringLayout.VERTICAL_CENTER, title);
        layout.putConstraint(SpringLayout.EAST, usernameLabel, -10, SpringLayout.HORIZONTAL_CENTER, title);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, username, 0, SpringLayout.VERTICAL_CENTER, usernameLabel);
        layout.putConstraint(SpringLayout.WEST, username, 10, SpringLayout.HORIZONTAL_CENTER, title);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, login, 40, SpringLayout.VERTICAL_CENTER, username);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, login, 0, SpringLayout.HORIZONTAL_CENTER, this);

        // apply layout
        this.setLayout(layout);
    }
}
