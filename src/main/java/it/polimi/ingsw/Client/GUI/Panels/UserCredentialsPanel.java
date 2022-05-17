package it.polimi.ingsw.Client.GUI.Panels;

import com.google.gson.Gson;
import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.PopupMessage;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Misc.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.DeclarePlayerRequest;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.LobbyAccept;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.StatusCode;

import javax.swing.*;
import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.hidePassIcon;
import static it.polimi.ingsw.Client.GUI.IconLoader.showPassIcon;

public class UserCredentialsPanel extends JPanel {
    public UserCredentialsPanel(Context ctx) {
        // unwrapping context into useful variables
        Window window = ctx.getWindow();
        SocketWrapper sw = ctx.getSocketWrapper();

        // labels
        JLabel title = new JLabel("Login");
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.RIGHT);
        JLabel passwordLabel = new JLabel("Password:", SwingConstants.RIGHT);

        // text fields
        JTextField username = new JTextField("guest" + (int) (Math.random() * 1000), 10);
        JPasswordField password = new JPasswordField(10);
        password.setEchoChar('*');

        // buttons
        JButton login = new JButton("Login");
        JToggleButton viewPassword = new JToggleButton();
        viewPassword.setPreferredSize(new Dimension(17, 17));
        viewPassword.setContentAreaFilled(true);
        viewPassword.setIcon(showPassIcon);
        viewPassword.setSelectedIcon(hidePassIcon);
        viewPassword.setToolTipText("Show Password");

        // adding all elements to the view
        this.add(title);
        this.add(usernameLabel);
        this.add(passwordLabel);
        this.add(username);
        this.add(password);
        this.add(viewPassword);
        this.add(login);

        // setting correct focus
        username.requestFocusInWindow();

        // actionListeners
        username.addActionListener((actionEvent -> {
            username.setText(username.getText().trim());
            password.requestFocusInWindow();
        }));
        password.addActionListener(actionEvent -> login.requestFocusInWindow());
        viewPassword.addActionListener(actionEvent -> {
            if (viewPassword.isSelected()) {
                viewPassword.setToolTipText("Hide Password");
                password.setEchoChar((char) 0);
            } else {
                viewPassword.setToolTipText("Show Password");
                password.setEchoChar('*');
            }
        });
        login.addActionListener(actionEvent -> {
            // normalize username
            username.setText(username.getText().trim());
            try {
                sw.sendMessage(new DeclarePlayerRequest(
                        username.getText().trim(),
                        new String(password.getPassword()
                        )
                ));
                Message msg = sw.awaitMessage();
                if (msg.getType() == PayloadType.RESPONSE_LOBBY_ACCEPT) {
                    LobbyAccept response = new Gson().fromJson(msg.getData(), LobbyAccept.class);
                    if (response.getStatusCode() == StatusCode.Success) {
                        ctx.setOpenLobbies(response.getPublicLobbies());
                        ctx.setReconnectToTheseLobbies(response.getReconnectToTheseLobbies());
                        new LobbySelectionPanel(ctx);
                    } else {
                        new PopupMessage("Server denied your login", "Failure :(");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // layout object decleration and setup
        SpringLayout layout = new SpringLayout();

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 20, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, usernameLabel, 20, SpringLayout.VERTICAL_CENTER, title);
        layout.putConstraint(SpringLayout.EAST, usernameLabel, -10, SpringLayout.HORIZONTAL_CENTER, title);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, username, 0, SpringLayout.VERTICAL_CENTER, usernameLabel);
        layout.putConstraint(SpringLayout.WEST, username, 10, SpringLayout.HORIZONTAL_CENTER, title);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, passwordLabel, 20, SpringLayout.VERTICAL_CENTER, usernameLabel);
        layout.putConstraint(SpringLayout.EAST, passwordLabel, -10, SpringLayout.HORIZONTAL_CENTER, title);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, password, 0, SpringLayout.VERTICAL_CENTER, passwordLabel);
        layout.putConstraint(SpringLayout.WEST, password, 10, SpringLayout.HORIZONTAL_CENTER, title);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, viewPassword, 0, SpringLayout.VERTICAL_CENTER, password);
        layout.putConstraint(SpringLayout.WEST, viewPassword, 0, SpringLayout.EAST, password);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, login, 40, SpringLayout.VERTICAL_CENTER, passwordLabel);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, login, 0, SpringLayout.HORIZONTAL_CENTER, this);

        // apply layout
        this.setLayout(layout);

        // display the view
        window.changeView(this);
    }
}
