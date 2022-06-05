package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Model.IslandGroup;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IslandFieldPanel extends JPanel {

    IslandFieldPanel(List<IslandGroup> islandGroups){
        this.setBackground(new Color(105,186,233));
        ArrayList<JButton> islands = new ArrayList<>(islandGroups.size());


    }
}
