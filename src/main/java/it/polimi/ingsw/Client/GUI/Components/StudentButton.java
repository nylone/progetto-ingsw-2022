package it.polimi.ingsw.Client.GUI.Components;

import it.polimi.ingsw.Model.Enums.PawnColour;

import javax.swing.*;
import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class StudentButton extends JButton {
    public StudentButton(PawnColour pawnColour, int amount) {
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
        switch (pawnColour) {
            case BLUE -> this.setIcon(BlueStudent);
            case YELLOW -> this.setIcon(YellowStudent);
            case GREEN -> this.setIcon(GreenStudent);
            case PINK -> this.setIcon(PinkStudent);
            case RED -> this.setIcon(RedStudent);
        }
        if (amount > 1) {
            this.setText("x" + amount);
            this.setHorizontalTextPosition(SwingConstants.RIGHT);
            Font font = new Font("SansSerif", Font.BOLD, 16);
            this.setFont(font);
            switch (pawnColour) {
                case BLUE -> this.setForeground(new Color(0x27C3F2));
                case YELLOW -> this.setForeground(new Color(0xFBAF2F));
                case GREEN -> this.setForeground(new Color(0x1FB47F));
                case PINK -> this.setForeground(new Color(0xDB5FA2));
                case RED -> this.setForeground(new Color(0xEC1F23));
            }
        }
    }
}
