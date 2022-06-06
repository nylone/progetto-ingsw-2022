package it.polimi.ingsw.Client.GUI.Components;

import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.TowerColour;

import javax.swing.*;
import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class TowerComponent extends JButton {

    public TowerComponent(TowerColour tower, int amount) {
            switch (tower) {
                case BLACK -> this.setIcon(BlackTower);
                case GRAY -> this.setIcon(GrayTower);
                case WHITE -> this.setIcon(WhiteTower);
            }

            if (amount > 1) {
                this.setText("x" + amount);
                this.setHorizontalTextPosition(SwingConstants.RIGHT);
                Font font = new Font("SansSerif", Font.BOLD, 18);
                this.setFont(font);
            }
        }
}
