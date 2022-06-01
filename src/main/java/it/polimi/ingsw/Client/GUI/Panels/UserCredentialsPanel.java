package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.PopupMessage;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.DeclarePlayerRequest;
import it.polimi.ingsw.Server.Messages.ServerResponses.LobbyAccept;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import javax.swing.*;
import java.io.IOException;

public class UserCredentialsPanel extends JPanel {
    public UserCredentialsPanel(Context ctx) {
        // unwrapping context into useful variables
        Window window = ctx.getWindow();
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
                    if (sw.awaitMessage() instanceof LobbyAccept lobbyAccept) {
                        if (lobbyAccept.getStatusCode() == StatusCode.Success) {
                            new LobbySelectionPanel(ctx, lobbyAccept.getPublicLobbies(), lobbyAccept.getReconnectToTheseLobbies());
                        } else {
                            new PopupMessage("Server denied your login", "Failure :(");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        // layout object decleration and setup
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

        // display the view
        window.changeView(this);
    }
}
