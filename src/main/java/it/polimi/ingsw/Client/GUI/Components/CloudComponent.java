package it.polimi.ingsw.Client.GUI.Components;

import it.polimi.ingsw.Client.GUI.Listeners.ClickListener;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Enums.PawnColour;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class CloudComponent extends JLabel {

    public CloudComponent(ImageIcon cloudImage, Cloud cloud, boolean isDualPlayerGame) {
        super(cloudImage);
        ArrayList<JLabel> students = new ArrayList<>(3);
        for (PawnColour studentOnCloud : cloud.getContents()) {
            switch (studentOnCloud) {
                case RED -> students.add(new JLabel(RedStudent));
                case GREEN -> students.add(new JLabel(GreenStudent));
                case YELLOW -> students.add(new JLabel(YellowStudent));
                case BLUE -> students.add(new JLabel(BlueStudent));
                case PINK -> students.add(new JLabel(PinkStudent));
            }
        }

        int offset = isDualPlayerGame ? 155 : 0;
        students.get(0).setBounds(225, 152 + offset, 50, 50);
        students.get(1).setBounds(290, 137 + offset, 50, 50);
        students.get(2).setBounds(240, 221 + offset, 50, 50);
        if (students.size() == 4) {
            students.get(3).setBounds(308, 200, 50, 50);
        }

        students.forEach(this::add);

        Rectangle clickable = new Rectangle(220, 120 ,150, 150);
        this.addMouseListener(new ClickListener(this, clickable));

    }
}
