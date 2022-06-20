package it.polimi.ingsw.Client.GUI.Components;

import javax.swing.*;
import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.noEntryTileIcon;


public class NoEntryTileComponent extends JButton {
    /**
     * Create a new JButton with NoEntryTile as icon
     *
     * @param amount NoEntryTiles' multiplicity
     */
    public NoEntryTileComponent(int amount) {
        //remove borders and fill from NoEntryTileComponent
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setIcon(noEntryTileIcon);

        //write eventual multiplicity beside the JButton
        if (amount > 1) {
            this.setText("x" + amount);
            this.setHorizontalTextPosition(SwingConstants.RIGHT);
            Font font = new Font("SansSerif", Font.BOLD, 13);
            this.setFont(font);
        }
    }
}
