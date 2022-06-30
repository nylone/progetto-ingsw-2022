package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.IconLoader;
import it.polimi.ingsw.Network.KeepAliveSocketWrapper;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;
import it.polimi.ingsw.Server.Messages.ServerResponses.Welcome;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;

/**
 * Panel that allows the user to connect to a Server by selecting ip address and port
 */
public class StartPanel extends JPanel {
    /**
     * Create a new StartPanel
     *
     * @param ctx Context that will be used by GUI's panels
     */
    public StartPanel(Context ctx) {
        // labels
        JLabel title = new JLabel("Eriantys welcomes You!");
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        JLabel serverAddressLabel = new JLabel("Server address:", SwingConstants.RIGHT);
        serverAddressLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        serverAddressLabel.setForeground(Color.WHITE);
        JLabel serverPortLabel = new JLabel("Server port:", SwingConstants.RIGHT);
        serverPortLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        serverPortLabel.setForeground(Color.WHITE);

        // text fields
        JTextField address = new JTextField("localhost", 10);
        JTextField port = new JTextField("8080", 10);

        // buttons
        JButton connect = new JButton("Connect");

        // adding all elements to the view
        this.add(title);
        this.add(serverAddressLabel);
        this.add(serverPortLabel);
        this.add(address);
        this.add(port);
        this.add(connect);

        // setting correct focus
        address.requestFocusInWindow();

        // actionListeners
        // enter on address moves focus to port
        address.addActionListener((actionEvent -> port.requestFocusInWindow()));
        // enter on port moves focus to connect
        port.addActionListener((actionEvent -> connect.requestFocusInWindow()));

        connect.addActionListener(actionEvent -> {
            connect.setEnabled(false);
            new Thread(() -> {
                try {
                    SocketWrapper sw = new KeepAliveSocketWrapper(new Socket(
                            address.getText().trim(),
                            Integer.parseInt(port.getText().trim())), 5000, true);
                    if (sw.awaitMessage() instanceof Welcome welcome && welcome.getStatusCode() == StatusCode.Success) {
                        // spawn and change to next view
                        ctx.setSocketWrapper(sw);
                        ctx.getWindow().changeView(new UserCredentialsPanel(ctx));
                    } else {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Server did not welcome us", "Warning", JOptionPane.INFORMATION_MESSAGE));
                        connect.setEnabled(true);
                    }
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "No valid server was found", "Warning", JOptionPane.INFORMATION_MESSAGE));
                    connect.setEnabled(true);
                }
            }).start();
        });

        // layout object declaration and setup
        SpringLayout layout = new SpringLayout();

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 340, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, serverAddressLabel, 60, SpringLayout.VERTICAL_CENTER, title);
        layout.putConstraint(SpringLayout.EAST, serverAddressLabel, -10, SpringLayout.HORIZONTAL_CENTER, title);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, address, 0, SpringLayout.VERTICAL_CENTER, serverAddressLabel);
        layout.putConstraint(SpringLayout.WEST, address, 10, SpringLayout.HORIZONTAL_CENTER, title);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, serverPortLabel, 20, SpringLayout.VERTICAL_CENTER, serverAddressLabel);
        layout.putConstraint(SpringLayout.EAST, serverPortLabel, -10, SpringLayout.HORIZONTAL_CENTER, title);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, port, 0, SpringLayout.VERTICAL_CENTER, serverPortLabel);
        layout.putConstraint(SpringLayout.WEST, port, 10, SpringLayout.HORIZONTAL_CENTER, title);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, connect, 40, SpringLayout.VERTICAL_CENTER, serverPortLabel);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, connect, 0, SpringLayout.HORIZONTAL_CENTER, this);

        // apply layout
        this.setLayout(layout);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(IconLoader.userCredentialBackground.getImage(), 0, 0, null);
    }
}
