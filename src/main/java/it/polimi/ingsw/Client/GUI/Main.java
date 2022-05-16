package it.polimi.ingsw.Client.GUI;

import com.google.gson.Gson;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Misc.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.ConnectLobbyRequest;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.DeclarePlayerRequest;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.LobbyAccept;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.LobbyRedirect;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.StatusCode;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.Welcome;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class Main {

    private static final ImageIcon showPassIcon = loadIcon("eye");
    private static final ImageIcon hidePassIcon = loadIcon("eye-barred");

    private final JFrame frame;
    private SocketWrapper sw;

    public Main() {
        frame = new JFrame("Eriantys");
        frame.setMinimumSize(new Dimension(300, 150));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverConnectView();
    }

    private void serverConnectView() {
        cleanView();

        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);
        frame.add(panel);

        JLabel title = new JLabel("Eriantys welcomes You!");
        panel.add(title);

        JLabel serverAddressLabel = new JLabel("Server address:", SwingConstants.RIGHT);
        panel.add(serverAddressLabel);
        JLabel serverPortLabel = new JLabel("Server port:", SwingConstants.RIGHT);
        panel.add(serverPortLabel);

        JTextField address = new JTextField("localhost", 10);
        JTextField port = new JTextField("8080", 10);
        JButton connect = new JButton("Connect");
        address.addActionListener((actionEvent -> port.requestFocusInWindow()));
        panel.add(address);
        port.addActionListener((actionEvent -> connect.requestFocusInWindow()));
        panel.add(port);
        connect.addActionListener(actionEvent -> {
            try {
                sw = new SocketWrapper(new Socket(address.getText(), Integer.parseInt(port.getText())));
                Message response = sw.awaitMessage();
                if (response.getType() == PayloadType.RESPONSE_WELCOME &&
                        new Gson().fromJson(response.getData(), Welcome.class).getStatusCode() == StatusCode.Success) {
                    userCredentialsView();
                } else {
                    title.setText("fail...");
                }
            } catch (Exception e) {
                title.setText("fail...");
            }
        });
        panel.add(connect);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 20, SpringLayout.NORTH, panel);
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

        commitView();
    }

    private void cleanView() {
        frame.setVisible(false);
        frame.getContentPane().removeAll();
    }

    private void userCredentialsView() {
        cleanView();

        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);
        frame.add(panel);

        JLabel title = new JLabel("Login");
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.RIGHT);
        JLabel passwordLabel = new JLabel("Password:", SwingConstants.RIGHT);
        JTextField username = new JTextField("guest" + (int) (Math.random() * 1000), 10);
        JPasswordField password = new JPasswordField(10);
        password.setEchoChar('*');
        JButton login = new JButton("Login");
        JToggleButton viewPassword = new JToggleButton();
        viewPassword.setPreferredSize(new Dimension(17, 17));
        viewPassword.setContentAreaFilled(true);
        viewPassword.setIcon(showPassIcon);
        viewPassword.setSelectedIcon(hidePassIcon);
        viewPassword.setToolTipText("Show Password");

        panel.add(title);
        panel.add(usernameLabel);
        panel.add(passwordLabel);
        panel.add(username);
        panel.add(password);
        panel.add(viewPassword);
        panel.add(login);

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
                sw.sendMessage(new DeclarePlayerRequest(username.getText().trim(), new String(password.getPassword())));
                Message msg = sw.awaitMessage();
                if (msg.getType() == PayloadType.RESPONSE_LOBBY_ACCEPT) {
                    LobbyAccept response = new Gson().fromJson(msg.getData(), LobbyAccept.class);
                    if (response.getStatusCode() == StatusCode.Success) {
                        title.setText("Success!");
                        lobbySelectionView(response.getOpenLobbies());
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            title.setText("Try again");
        });

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 20, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, panel);

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
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, login, 0, SpringLayout.HORIZONTAL_CENTER, panel);

        username.requestFocusInWindow();

        commitView();
    }

    private void commitView() {
        frame.setVisible(true);
        frame.pack();
    }

    private void lobbySelectionView(List<Pair<UUID, String>> openLobbies) {
        cleanView();
        frame.setMinimumSize(new Dimension(1080, 720));

        JTabbedPane tabbedPane = new JTabbedPane();
        frame.add(tabbedPane);

        JPanel connectPanel = new JPanel();
        JPanel createPanel = new JPanel();
        tabbedPane.add("Connect", connectPanel);
        tabbedPane.add("Create", createPanel);

        SpringLayout layout = new SpringLayout();
        connectPanel.setLayout(layout);

        JLabel title = new JLabel("Select a lobby");
        JLabel lobbyIDLabel = new JLabel("Connecting to:", SwingConstants.RIGHT);
        JTextField lobbyID = new JTextField(30);
        JButton connect = new JButton("Connect");
        JList lobbies = new JList(openLobbies.toArray());
        lobbies.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lobbies.setCellRenderer((list, entry, index, isSelected, hasCellFocus) -> {
            JLabel displayedText = new JLabel();
            Pair<UUID, String> pair = (Pair<UUID, String>) entry;
            displayedText.setText("ID: " + pair.getFirst() + " || Admin: " + pair.getSecond());
            System.out.println(isSelected);
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
        JScrollPane scrollLobbies = new JScrollPane();
        scrollLobbies.setViewportView(lobbies);
        scrollLobbies.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollLobbies.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollLobbies.setPreferredSize(new Dimension(720, 100));
        lobbies.setLayoutOrientation(JList.VERTICAL);

        connectPanel.add(title);
        connectPanel.add(lobbyIDLabel);
        connectPanel.add(lobbyID);
        connectPanel.add(connect);
        connectPanel.add(scrollLobbies);

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
                    title.setText("Success!");
                } else {
                    title.setText("Try again.");
                }
            } catch (Exception e) {
                title.setText("Try again");
            }
        });

        // centra il titolo
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, title, 20, SpringLayout.NORTH, connectPanel);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, connectPanel);

        // centra la lista delle lobby
        layout.putConstraint(SpringLayout.NORTH, scrollLobbies, 20, SpringLayout.VERTICAL_CENTER, title);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scrollLobbies, 0, SpringLayout.HORIZONTAL_CENTER, connectPanel);

        // centra il campo lobbyID
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lobbyIDLabel, 20, SpringLayout.SOUTH, scrollLobbies);
        layout.putConstraint(SpringLayout.EAST, lobbyIDLabel, -10, SpringLayout.HORIZONTAL_CENTER, connectPanel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, lobbyID, 0, SpringLayout.VERTICAL_CENTER, lobbyIDLabel);
        layout.putConstraint(SpringLayout.WEST, lobbyID, 10, SpringLayout.HORIZONTAL_CENTER, connectPanel);

        layout.putConstraint(SpringLayout.VERTICAL_CENTER, connect, 40, SpringLayout.VERTICAL_CENTER, lobbyIDLabel);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, connect, 0, SpringLayout.HORIZONTAL_CENTER, connectPanel);
        commitView();
    }

    private static ImageIcon loadIcon(String name) {
        Image img;
        try {
            img = ImageIO.read(Main.class.getResource("/icons/" + name + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageIcon(img.getScaledInstance(12, 12, Image.SCALE_SMOOTH));
    }

}
