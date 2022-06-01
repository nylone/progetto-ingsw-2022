package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.PopupMessage;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;
import it.polimi.ingsw.Server.Messages.ServerResponses.Welcome;

import javax.swing.*;
import java.net.Socket;

public class StartPanel extends JPanel {
    public StartPanel(Context ctx) {
        // unwrapping context into useful variables
        Window window = ctx.getWindow();

        // labels
        JLabel title = new JLabel("Eriantys welcomes You!");
        JLabel serverAddressLabel = new JLabel("Server address:", SwingConstants.RIGHT);
        JLabel serverPortLabel = new JLabel("Server port:", SwingConstants.RIGHT);

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

        connect.addActionListener(actionEvent -> new Thread(() -> {
            try {
                SocketWrapper sw = new SocketWrapper(new Socket(
                        address.getText().trim(),
                        Integer.parseInt(port.getText().trim())));
                if (sw.awaitMessage() instanceof Welcome welcome && welcome.getStatusCode() == StatusCode.Success) {
                    // spawn and change to next view
                    ctx.setSocketWrapper(sw);
                    new UserCredentialsPanel(ctx);
                } else {
                    new PopupMessage("Server did not welcome us", "Failure :(");
                }
            } catch (Exception e) {
                new PopupMessage("No valid server was found", "Failure :(");
            }
        }).start());

        // layout object declaration and setup
        SpringLayout layout = new SpringLayout();

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 20, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, serverAddressLabel, 20, SpringLayout.VERTICAL_CENTER, title);
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

        // display the view
        window.changeView(this);
    }
}
