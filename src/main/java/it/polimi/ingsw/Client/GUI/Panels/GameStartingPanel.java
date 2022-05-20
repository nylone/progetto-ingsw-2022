package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Client.GUI.PopupMessage;
import it.polimi.ingsw.Client.GUI.Window;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.StartGameRequest;
import it.polimi.ingsw.RemoteView.Messages.Message;

import javax.swing.*;
import java.io.IOException;

public class GameStartingPanel extends JPanel {
    public GameStartingPanel(Context ctx, boolean isAdmin) {
        // unwrapping context into useful variables
        Window window = ctx.getWindow();
        SocketWrapper sw = ctx.getSocketWrapper();

        // labels
        JLabel title = new JLabel("Connected to the lobby");

        // buttons
        JButton disconnect = new JButton("Disconnect from the lobby");
        JButton start = new JButton("Start the game");
        start.setToolTipText("Only the lobby admin can start the lobby");
        start.setEnabled(isAdmin); // start button is enabled only for admin of the lobby

        // adding all elements to the view
        this.add(title);
        this.add(disconnect);
        this.add(start);

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

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, start, 20, SpringLayout.NORTH, title);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, start, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, disconnect, 20, SpringLayout.NORTH, start);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, disconnect, 0, SpringLayout.HORIZONTAL_CENTER, this);

        // apply layout
        this.setLayout(layout);

        // display the view
        window.changeView(this);
    }
}
