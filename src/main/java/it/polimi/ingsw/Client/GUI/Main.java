package it.polimi.ingsw.Client.GUI;

import com.google.gson.Gson;
import it.polimi.ingsw.Misc.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.StatusCode;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.Welcome;

import javax.swing.*;
import java.net.Socket;

public class Main {

    public Main() {
        SpringLayout layout = new SpringLayout();
        JFrame frame = new JFrame("Eriantys");
        frame.setSize(500, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(layout);
        frame.add(panel);

        JLabel title = new JLabel("Eriantys welcomes You!");
        panel.add(title);

        JLabel serverAddressLabel = new JLabel("Server address: ");
        panel.add(serverAddressLabel);
        JLabel serverPortLabel = new JLabel("Server port: ");
        panel.add(serverPortLabel);

        JTextField address = new JTextField("localhost", 15);
        JTextField port = new JTextField("8080", 15);
        JButton connect = new JButton("Connect");
        address.addActionListener((actionEvent -> port.requestFocusInWindow()));
        panel.add(address);
        port.addActionListener((actionEvent -> connect.requestFocusInWindow()));
        panel.add(port);
        connect.addActionListener(actionEvent -> {
            try {
                SocketWrapper sw = new SocketWrapper(new Socket(address.getText(), Integer.parseInt(port.getText())));
                Message response = sw.awaitMessage();
                if (response.getType() == PayloadType.RESPONSE_WELCOME &&
                        new Gson().fromJson(response.getData(), Welcome.class).getStatusCode() == StatusCode.Success) {
                    title.setText("success!");
                } else {
                    title.setText("fail...");
                }
            } catch (Exception e) {
                title.setText("fail...");
            }
        });
        panel.add(connect);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 15, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, panel);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, serverAddressLabel, 20, SpringLayout.VERTICAL_CENTER, title);
        layout.putConstraint(SpringLayout.EAST, serverAddressLabel, -10, SpringLayout.HORIZONTAL_CENTER, title);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, address, 0, SpringLayout.VERTICAL_CENTER, serverAddressLabel);
        layout.putConstraint(SpringLayout.WEST, address, 10, SpringLayout.HORIZONTAL_CENTER, title);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, serverPortLabel, 20, SpringLayout.VERTICAL_CENTER, serverAddressLabel);
        layout.putConstraint(SpringLayout.EAST, serverPortLabel, -10, SpringLayout.HORIZONTAL_CENTER, title);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, port, 0, SpringLayout.VERTICAL_CENTER, serverPortLabel);
        layout.putConstraint(SpringLayout.WEST, port, 10, SpringLayout.HORIZONTAL_CENTER, title);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, connect, 40, SpringLayout.VERTICAL_CENTER, serverPortLabel);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, connect, 0, SpringLayout.HORIZONTAL_CENTER, panel);

        address.requestFocusInWindow();
        frame.setVisible(true);
    }
}
