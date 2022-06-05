package it.polimi.ingsw.Client.GUI.Components;

import it.polimi.ingsw.Model.Enums.PawnColour;

import javax.swing.*;

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
        }
}
