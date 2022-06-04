package it.polimi.ingsw.Client.GUI.Components;

import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Enums.PawnColour;

import javax.swing.*;
import java.util.ArrayList;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;
import static it.polimi.ingsw.Client.GUI.IconLoader.PinkStudent;

public class CloudComponent extends JLabel {

    public CloudComponent(ImageIcon cloudImage, Cloud cloud) {
        super(cloudImage);
        ArrayList<JButton> students = new ArrayList<>(3);
        for (PawnColour studentOnCloud : cloud.getContents()) {
            switch (studentOnCloud) {
                case RED -> students.add(new Student(RedStudent));
                case GREEN -> students.add(new Student(GreenStudent));
                case YELLOW -> students.add(new Student(YellowStudent));
                case BLUE -> students.add(new Student(BlueStudent));
                case PINK -> students.add(new Student(PinkStudent));
            }
        }

        students.get(0).setBounds(227, 179, 50, 50);
        students.get(1).setBounds(288, 165, 50, 50);
        students.get(2).setBounds(241, 239, 50, 50);
        if (students.size() == 4) {
            students.get(3).setBounds(302, 227, 50, 50);
        }

        students.forEach(this::add);
    }
}
