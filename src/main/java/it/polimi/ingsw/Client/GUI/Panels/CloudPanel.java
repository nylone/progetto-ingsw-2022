package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Components.CloudComponent;
import it.polimi.ingsw.Client.GUI.Listeners.GUISocketListener;
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

/**
 * Class used for printing all game's clouds and their students. It also permits to end user's turn
 */
public class CloudPanel extends JPanel {

    /**
     * Contains GuiReader's information necessary to record user's requests during his turn
     */
    private final GUISocketListener guiSocketListener;

    /**
     * Create a new JPanel and draw all clouds (and their students)
     *
     * @param clouds            clouds from model that needs to be drawn
     * @param currentPlayer     current Player's playerBoard
     * @param guiSocketListener guiReader necessary for checking and saving actions requested by user
     * @param sw                socketWrapper to send messages to Server
     */
    public CloudPanel(List<Cloud> clouds, PlayerBoard currentPlayer, GUISocketListener guiSocketListener, SocketWrapper sw) {
        this.guiSocketListener = guiSocketListener;
        //create the label that contains all others components
        JLabel backGroundLabel = new JLabel(sky);
        backGroundLabel.setLayout(null);
        backGroundLabel.setBounds(0, 0, 1080, 720);
        this.add(backGroundLabel);
        //list containing clouds' buttons
        ArrayList<CloudComponent> cloudButtons = new ArrayList<>(clouds.size());
        //create endTurn's button and set its layout
        JButton endTurnButton = new JButton("END YOUR TURN");
        endTurnButton.setBackground(new Color(255, 153, 51));
        endTurnButton.setForeground(Color.BLACK);
        endTurnButton.setFocusPainted(false);
        //set visible only whether the player has played a chooseCloudTile action
        endTurnButton.setVisible(guiSocketListener.getSuccessfulRequestsByType(ChooseCloudTile.class) == 1);
        //add on-click action listener to endTurnButton
        endTurnButton.addActionListener(e -> {
            // skip execution of the action if a previous action still hasn't been processed by the server
            if (guiSocketListener.awaitingPlayerActionFeedback()) {
                JOptionPane.showMessageDialog(null, "Please wait for the server to process your previous" +
                        "request before making a new one");
                return;
            }
            if (guiSocketListener.getSuccessfulRequestsByType(ChooseCloudTile.class) == 1) {

                //create endTurn action and its playerActionRequest
                EndTurnOfActionPhase endTurnOfActionPhase = new EndTurnOfActionPhase(currentPlayer.getId());
                PlayerActionRequest playerActionRequest = new PlayerActionRequest(endTurnOfActionPhase);
                //save action inside guiReader
                guiSocketListener.savePlayerActionRequest(endTurnOfActionPhase);
                try {
                    sw.sendMessage(playerActionRequest);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        //for every cloud:
        for (int i = 0; i < clouds.size(); i++) {
            //create a new CloudComponent and add it on cloudButtons list
            cloudButtons.add(new CloudComponent(cloudIcon, clouds.get(i)));
            int finalI = i;
            //add on-click action listener to cloudComponent
            cloudButtons.get(cloudButtons.size() - 1).addActionListener(e -> {
                // skip execution of the action if a previous action still hasn't been processed by the server
                if (guiSocketListener.awaitingPlayerActionFeedback()) {
                    JOptionPane.showMessageDialog(null, "Please wait for the server to process your previous" +
                            "request before making a new one");
                    return;
                }
                if (guiSocketListener.getSuccessfulRequestsByType(MoveMotherNature.class) == 1) {
                    //create chooseCloudTile action and its playerActionRequest
                    ChooseCloudTile chooseCloudTile = new ChooseCloudTile(currentPlayer.getId(), finalI);
                    PlayerActionRequest playerActionRequest = new PlayerActionRequest(chooseCloudTile);
                    //save action inside guiReader
                    this.guiSocketListener.savePlayerActionRequest(chooseCloudTile);
                    try {
                        sw.sendMessage(playerActionRequest);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }
        //--ABSOLUTE POSITIONING--
        cloudButtons.get(0).setBounds(300, 125, 200, 200);
        cloudButtons.get(1).setBounds(700, 125, 200, 200);
        if (cloudButtons.size() >= 3) cloudButtons.get(2).setBounds(300, 350, 200, 200);
        if (cloudButtons.size() == 4) cloudButtons.get(3).setBounds(700, 350, 200, 200);
        endTurnButton.setBounds(500, 550, 150, 75);
        //add cloudButtons and endTurnButton to backgroundLabel
        cloudButtons.forEach(backGroundLabel::add);
        backGroundLabel.add(endTurnButton);
    }
}
