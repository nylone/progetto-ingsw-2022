package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Model.PlayerBoard;
import javax.swing.*;

import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.playerBoardIcon;
public class PlayerBoardPanel extends JPanel {
    public PlayerBoardPanel(PlayerBoard pb) {
        SpringLayout layout = new SpringLayout();

        JLabel boardBackground = new JLabel(playerBoardIcon); // 1660 x 720
        //this.add(boardBackground);

        for (int i = 0; i < pb.getEntranceSize(); i++) {
            JLabel entrancePawnContainer = new JLabel();
            entrancePawnContainer.setText("O");
            switch (pb.getEntranceStudents().get(i).get()) {
                case RED -> entrancePawnContainer.setForeground(Color.red);
                case BLUE -> entrancePawnContainer.setForeground(Color.blue);
                case PINK -> entrancePawnContainer.setForeground(Color.pink);
                case GREEN -> entrancePawnContainer.setForeground(Color.green);
                case YELLOW -> entrancePawnContainer.setForeground(Color.yellow);
            }
            this.add(entrancePawnContainer);
            layout.putConstraint(SpringLayout.NORTH, entrancePawnContainer, 20 + 20 * (i / 2), SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.WEST, entrancePawnContainer, 20 + 200 * (i % 2), SpringLayout.WEST, this);
        }

        this.setLayout(layout);
    }
}
