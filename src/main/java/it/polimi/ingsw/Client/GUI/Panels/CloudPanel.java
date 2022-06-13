package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Components.CloudComponent;
import it.polimi.ingsw.Client.GUI.GUIReader;
import it.polimi.ingsw.Controller.Actions.ChooseCloudTile;
import it.polimi.ingsw.Controller.Actions.EndTurnOfActionPhase;
import it.polimi.ingsw.Controller.Actions.MoveMotherNature;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.PlayerActionRequest;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.Client.GUI.IconLoader.cloudIcon;
import static it.polimi.ingsw.Client.GUI.IconLoader.sky;

public class CloudPanel extends JPanel {

    private GUIReader guiReader;

    private SocketWrapper sw;

    public CloudPanel(List<Cloud> clouds, PlayerBoard currentPlayer, GUIReader guiReader, SocketWrapper sw) {
        this.guiReader = guiReader;
        this.sw = sw;
        JLabel backGroundLabel = new JLabel(sky);
        backGroundLabel.setLayout(null);
        backGroundLabel.setBounds(0, 0, 1080, 720);
        this.add(backGroundLabel);
        ArrayList<CloudComponent> cloudButtons = new ArrayList<>(clouds.size());
        JButton endTurnButton = new JButton("END YOUR TURN");
        endTurnButton.setBackground(new Color(255, 153, 51));
        endTurnButton.setForeground(Color.BLACK);
        endTurnButton.setFocusPainted(false);
        if (guiReader.getSuccessfulRequestsByType(ChooseCloudTile.class) == 1) {
            endTurnButton.setVisible(true);
        } else {
            endTurnButton.setVisible(false);
        }
        endTurnButton.addActionListener(e -> {
            if (guiReader.getSuccessfulRequestsByType(ChooseCloudTile.class) == 1) {
                EndTurnOfActionPhase endTurnOfActionPhase = new EndTurnOfActionPhase(currentPlayer.getId());
                PlayerActionRequest playerActionRequest = new PlayerActionRequest(endTurnOfActionPhase);
                guiReader.savePlayerActionRequest(endTurnOfActionPhase);
                try {
                    sw.sendMessage(playerActionRequest);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        for (int i = 0; i < clouds.size(); i++) {
            cloudButtons.add(new CloudComponent(cloudIcon, clouds.get(i)));
            int finalI = i;
            cloudButtons.get(cloudButtons.size() - 1).addActionListener(e -> {
                if (guiReader.getSuccessfulRequestsByType(MoveMotherNature.class) == 1) {
                   /* Container c = this.getParent();
                    while (!(c instanceof JTabbedPane jTabbedPane)) {
                        c = c.getParent();
                    }*/
                    ChooseCloudTile chooseCloudTile = new ChooseCloudTile(currentPlayer.getId(), finalI);
                    PlayerActionRequest playerActionRequest = new PlayerActionRequest(chooseCloudTile);
                    this.guiReader.savePlayerActionRequest(chooseCloudTile);
                    try {
                        sw.sendMessage(playerActionRequest);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

        }
        cloudButtons.get(0).setBounds(300, 125, 200, 200);
        cloudButtons.get(1).setBounds(700, 125, 200, 200);
        if (cloudButtons.size() == 3) cloudButtons.get(2).setBounds(300, 350, 200, 200);
        if (cloudButtons.size() == 4) cloudButtons.get(3).setBounds(700, 350, 200, 200);
        endTurnButton.setBounds(550, 550, 150, 75);
        cloudButtons.forEach(backGroundLabel::add);
        backGroundLabel.add(endTurnButton);
    }
}
