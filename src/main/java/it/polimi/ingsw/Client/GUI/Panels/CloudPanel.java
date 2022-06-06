package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Components.CloudComponent;
import it.polimi.ingsw.Model.Cloud;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class CloudPanel extends JPanel {


    public CloudPanel(List<Cloud> clouds) {
        /*GridLayout layout = new GridLayout(clouds.size() == 2 ? 1 : 2,2, -100, -100);

        ArrayList<ImageIcon> cloudIcons = new ArrayList<>(clouds.size());
        cloudIcons.addAll(Arrays.asList(cloud1Icon, cloud2Icon, cloud3Icon, cloud4Icon));

        for(Cloud cloud : clouds) {
            JButton c = new CloudComponent(cloudIcons.get(clouds.indexOf(cloud)), cloud, clouds.size() == 2);
            this.add(c);
        }

        this.setLayout(layout);*/
        JLabel backGroundLabel = new JLabel(sky);
        backGroundLabel.setLayout(null);
        backGroundLabel.setBounds(0, 0, 1080, 720);
        this.add(backGroundLabel);
        //ArrayList<ImageIcon> cloudIcons = new ArrayList<>(clouds.size());
        //cloudIcons.addAll(Arrays.asList(cloud1Icon, cloud2Icon, cloud3Icon, cloud4Icon));
        ArrayList<CloudComponent> cloudButtons = new ArrayList<>(clouds.size());
        for(Cloud Cloud: clouds){
           cloudButtons.add(new CloudComponent(cloudIcon,Cloud));

        }
        cloudButtons.get(0).setBounds(300,125, 200,200);
        cloudButtons.get(1).setBounds(700,125,200,200);
        if(cloudButtons.size()==3) cloudButtons.get(2).setBounds(300,350,200,200);
        if(cloudButtons.size()==4) cloudButtons.get(3).setBounds(700,350,200,200);
        backGroundLabel.setLayout(null);
        cloudButtons.forEach(backGroundLabel::add);
    }
}
