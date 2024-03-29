package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.IconLoader;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.DeclarePlayerRequest;
import it.polimi.ingsw.Server.Messages.Message;
import it.polimi.ingsw.Server.Messages.ServerResponses.LobbyServerAccept;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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
        title.setFont(new Font("SansSerif", Font.BOLD, 50));
        title.setForeground(Color.WHITE);
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.RIGHT);
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        usernameLabel.setForeground(Color.WHITE);

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
            login.setEnabled(false);
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
                        if (response instanceof LobbyServerAccept lobbyAccept) {
                            if (lobbyAccept.getStatusCode() == StatusCode.Success) {
                                //Switch to a new LobbySelectionPanel if user has been accepted by Server
                                ctx.getWindow().changeView(new LobbySelectionPanel(ctx, lobbyAccept.getPublicLobbies()));
                                again = false;
                            } else {
                                JOptionPane.showMessageDialog(null, "Server denied your login",
                                        "Warning", JOptionPane.INFORMATION_MESSAGE);
                                login.setEnabled(true);
                            }
                        } else {
                            throw new IllegalStateException("Unexpected value: " + response);
                        }
                    } while (again);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error in the connection with the server",
                            "Warning", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        sw.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    ctx.getWindow().changeView(new StartPanel(ctx));
                }
            }).start();
        });

        // layout object declaration and setup
        SpringLayout layout = new SpringLayout();

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 340, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, usernameLabel, 65, SpringLayout.VERTICAL_CENTER, title);
        layout.putConstraint(SpringLayout.EAST, usernameLabel, -10, SpringLayout.HORIZONTAL_CENTER, title);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, username, 0, SpringLayout.VERTICAL_CENTER, usernameLabel);
        layout.putConstraint(SpringLayout.WEST, username, 10, SpringLayout.HORIZONTAL_CENTER, title);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, login, 40, SpringLayout.VERTICAL_CENTER, username);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, login, 0, SpringLayout.HORIZONTAL_CENTER, this);

        // apply layout
        this.setLayout(layout);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        assert IconLoader.userCredentialBackground != null;
        g.drawImage(IconLoader.userCredentialBackground.getImage(), 0, 0, null);
    }
}
