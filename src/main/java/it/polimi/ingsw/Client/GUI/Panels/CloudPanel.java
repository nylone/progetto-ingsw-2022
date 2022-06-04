package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Components.CloudComponent;
import it.polimi.ingsw.Model.Cloud;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class CloudPanel extends JPanel {


    public CloudPanel(List<Cloud> clouds) {
        GridLayout layout = new GridLayout(clouds.size() == 2 ? 1 : 2,2, -100, -100);

        ArrayList<ImageIcon> cloudIcons = new ArrayList<>(clouds.size());
        cloudIcons.addAll(Arrays.asList(cloud1Icon, cloud2Icon, cloud3Icon, cloud4Icon));

        for(Cloud cloud : clouds) {
            JLabel c = new CloudComponent(cloudIcons.get(clouds.indexOf(cloud)), cloud);
            this.add(c);

        }

        this.setLayout(layout);
    }
}
