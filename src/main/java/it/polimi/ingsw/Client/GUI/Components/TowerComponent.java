package it.polimi.ingsw.Client.GUI.Components;

import it.polimi.ingsw.Model.Enums.TowerColour;

import javax.swing.*;
import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class TowerComponent extends JButton {
    /**
     * Create a new JButton tower as icon
     *
     * @param tower  tower's colour
     * @param amount towers' multiplicity
     */
    public TowerComponent(TowerColour tower, int amount) {
        //set icon basing on tower's colour
        switch (tower) {
            case BLACK -> this.setIcon(BlackTower);
            case GRAY -> this.setIcon(GrayTower);
            case WHITE -> this.setIcon(WhiteTower);
        }
        //write eventual multiplicity beside the JButton
        if (amount > 1) {
            this.setText("x" + amount);
            this.setHorizontalTextPosition(SwingConstants.RIGHT);
            Font font = new Font("SansSerif", Font.BOLD, 13);
            this.setFont(font);
        }
    }
}
