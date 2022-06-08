package it.polimi.ingsw.Client.GUI.Components;

import javax.swing.*;
import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.noEntryTileIcon;


public class NoEntryTileComponent extends JButton {

    public NoEntryTileComponent(int amount) {
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setIcon(noEntryTileIcon);

        if (amount > 1) {
            this.setText("x" + amount);
            this.setHorizontalTextPosition(SwingConstants.RIGHT);
            Font font = new Font("SansSerif", Font.BOLD, 16);
            this.setFont(font);
        }
    }
}
