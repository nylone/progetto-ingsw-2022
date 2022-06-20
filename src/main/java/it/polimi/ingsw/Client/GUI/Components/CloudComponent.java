package it.polimi.ingsw.Client.GUI.Components;

import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Enums.PawnColour;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;


public class CloudComponent extends JButton {

    /**
     * Create a new JButton with cloud as icon and draw students inside it
     *
     * @param cloudImage Cloud's icon to display
     * @param cloud      Cloud that will be represented by button
     */
    public CloudComponent(ImageIcon cloudImage, Cloud cloud) {
        super(cloudImage);
        ArrayList<JLabel> students = new ArrayList<>(cloud.getContents().size());
        //create labels basing on pawnColour
        for (PawnColour studentOnCloud : cloud.getContents()) {
            switch (studentOnCloud) {
                case RED -> students.add(new JLabel(RedStudent));
                case GREEN -> students.add(new JLabel(GreenStudent));
                case YELLOW -> students.add(new JLabel(YellowStudent));
                case BLUE -> students.add(new JLabel(BlueStudent));
                case PINK -> students.add(new JLabel(PinkStudent));
            }
        }
        //remove border from CloudComponent button
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setLayout(new GridLayout(2, 2));
        //add students' labels to CloudComponent
        students.forEach(this::add);


    }
}
