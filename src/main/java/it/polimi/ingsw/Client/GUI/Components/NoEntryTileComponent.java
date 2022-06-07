package it.polimi.ingsw.Client.GUI.Components;

import javax.swing.*;
import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.noEntryTileIcon;


public class NoEntryTileComponent extends JButton {

    public NoEntryTileComponent(int amount) {
        this.setIcon(noEntryTileIcon);

        if (amount > 1) {
            this.setText("x" + amount);
            this.setHorizontalTextPosition(SwingConstants.RIGHT);
            Font font = new Font("SansSerif", Font.BOLD, 18);
            this.setFont(font);
        }
    }
}
