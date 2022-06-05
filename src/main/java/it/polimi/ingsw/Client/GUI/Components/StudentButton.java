package it.polimi.ingsw.Client.GUI.Components;

import it.polimi.ingsw.Model.Enums.PawnColour;

import javax.swing.*;

import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class StudentButton extends JButton {
        public StudentButton(PawnColour pawnColour, int amount){
            switch (pawnColour){
                case BLUE -> this.setIcon(BlueStudent);
                case YELLOW -> this.setIcon(YellowStudent);
                case GREEN -> this.setIcon(GreenStudent);
                case PINK -> this.setIcon(PinkStudent);
                case RED -> this.setIcon(RedStudent);
            }
            if(amount > 1){
                JLabel amountLabel = new JLabel("x"+amount);
                amountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                amountLabel.setBounds(0,45,350,35);
               // this.setIconTextGap(50 - this.getIcon().getIconWidth());
                this.add(amountLabel);
            }
        }
}
