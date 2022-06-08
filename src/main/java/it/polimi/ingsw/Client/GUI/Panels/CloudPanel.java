package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Components.CloudComponent;
import it.polimi.ingsw.Model.Cloud;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Client.GUI.IconLoader.cloudIcon;
import static it.polimi.ingsw.Client.GUI.IconLoader.sky;

public class CloudPanel extends JPanel {


    public CloudPanel(List<Cloud> clouds) {
        JLabel backGroundLabel = new JLabel(sky);
        backGroundLabel.setLayout(null);
        backGroundLabel.setBounds(0, 0, 1080, 720);
        this.add(backGroundLabel);
        ArrayList<CloudComponent> cloudButtons = new ArrayList<>(clouds.size());
        for (Cloud Cloud : clouds) {
            cloudButtons.add(new CloudComponent(cloudIcon, Cloud));

        }
        cloudButtons.get(0).setBounds(300, 125, 200, 200);
        cloudButtons.get(1).setBounds(700, 125, 200, 200);
        if (cloudButtons.size() == 3) cloudButtons.get(2).setBounds(300, 350, 200, 200);
        if (cloudButtons.size() == 4) cloudButtons.get(3).setBounds(700, 350, 200, 200);
        cloudButtons.forEach(backGroundLabel::add);
    }
}
