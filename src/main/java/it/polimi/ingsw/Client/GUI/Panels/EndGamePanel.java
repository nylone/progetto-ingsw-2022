package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Context;
import it.polimi.ingsw.Model.PlayerBoard;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static it.polimi.ingsw.Client.GUI.IconLoader.sky;
import static it.polimi.ingsw.Client.GUI.IconLoader.winnerText;

/**
 * Panel showing game's winner(s); it allows user to close the game or join a new lobby
 */
public class EndGamePanel extends JPanel {

    public EndGamePanel(List<PlayerBoard> playerBoards, Context ctx) {
        //background label
        JLabel winnersBackground = new JLabel(sky);
        winnersBackground.setLayout(null);
        winnersBackground.setBounds(0, 0, 1080, 720);
        this.add(winnersBackground);
        //title's label
        JLabel WinTitle = new JLabel(winnerText);
        JLabel winnersNames = new JLabel();
        winnersNames.setFont(new Font("Monospaced", Font.BOLD, 22));
        //create String containing winner(s)'s names
        StringBuilder winnersText = new StringBuilder("<html>The winner is/are:<br>");
        for (PlayerBoard playerBoard : playerBoards) {
            winnersText.append(playerBoard.getNickname());
            winnersText.append("<br>");
        }
        winnersText.append("</html>");
        winnersNames.setText(winnersText.toString());
        JButton closeButton = new JButton("Close game");
        closeButton.addActionListener(e -> System.exit(0));
        JButton startButton = new JButton("Play again");
        startButton.addActionListener(e -> new UserCredentialsPanel(ctx));
        //---ABSOLUTE POSITIONING---
        WinTitle.setBounds(250, 10, 523, 120);
        winnersNames.setBounds(0, 200, 1080, 150);
        closeButton.setBounds(350, 450, 130, 65);
        startButton.setBounds(530, 450, 130, 65);
        //add components to background label
        winnersBackground.add(WinTitle);
        winnersBackground.add(winnersNames);
        winnersBackground.add(closeButton);
        winnersBackground.add(startButton);
        winnersBackground.setOpaque(true);
    }
}
