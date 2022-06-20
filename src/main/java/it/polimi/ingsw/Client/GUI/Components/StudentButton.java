package it.polimi.ingsw.Client.GUI.Components;

import it.polimi.ingsw.Model.Enums.PawnColour;

import javax.swing.*;
import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class StudentButton extends JButton {
    /**
     * Create a new JButton with pawn as icon
     *
     * @param pawnColour         pawn's colour
     * @param amount             pawns' multiplicity
     * @param overrideTextColour force text's colour to black
     */
    public StudentButton(PawnColour pawnColour, int amount, boolean overrideTextColour) {
        //remove borders and fill from StudentButton
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
        //set icon basing on pawn's colour
        switch (pawnColour) {
            case BLUE -> this.setIcon(BlueStudent);
            case YELLOW -> this.setIcon(YellowStudent);
            case GREEN -> this.setIcon(GreenStudent);
            case PINK -> this.setIcon(PinkStudent);
            case RED -> this.setIcon(RedStudent);
        }
        //write eventual multiplicity beside the JButton
        if (amount > 1) {
            this.setText("x" + amount);
            this.setHorizontalTextPosition(SwingConstants.RIGHT);
            Font font = new Font("SansSerif", Font.BOLD, 13);
            this.setFont(font);
            //set text's colour
            switch (pawnColour) {
                case BLUE -> this.setForeground(new Color(0x27C3F2));
                case YELLOW -> this.setForeground(new Color(0xFBAF2F));
                case GREEN -> this.setForeground(new Color(0x1FB47F));
                case PINK -> this.setForeground(new Color(0xDB5FA2));
                case RED -> this.setForeground(new Color(0xEC1F23));
            }
            if (overrideTextColour) {
                this.setForeground(Color.BLACK);
            }
        }
    }
}
