package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.ActionType;
import it.polimi.ingsw.Client.GUI.CircleLayout;
import it.polimi.ingsw.Client.GUI.Components.NoEntryTileComponent;
import it.polimi.ingsw.Client.GUI.Components.StudentButton;
import it.polimi.ingsw.Client.GUI.Components.TowerComponent;
import it.polimi.ingsw.Client.GUI.GUIReader;
import it.polimi.ingsw.Controller.Actions.MoveMotherNature;
import it.polimi.ingsw.Controller.Actions.MoveStudent;
import it.polimi.ingsw.Controller.Actions.PlayCharacterCard;
import it.polimi.ingsw.Controller.Enums.MoveDestination;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.IslandGroup;
import it.polimi.ingsw.Model.Model;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.PlayerActionRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

/**
 * Class necessary to print all the islands on GUI and perform all player's action that involve an island or islandGroup
 */
public class IslandFieldPanel extends JPanel {
    /**
     * Optional Integer containing student's index inside player's PlayerBoard's entrance (necessary when sending MoveStudentAction to Server)
     */
    private Optional<Integer> entrancePositionToMove = Optional.empty();
    /**
     * Optional Integer containing card's index inside game (0 to 2), it can be empty if no characterCard has been played
     */
    private Optional<Integer> selectedCharacterCard = Optional.empty();

    /**
     * Pawn from character card that player wants to move inside an island
     */
    private Optional<PawnColour> pawnFromCharacterCard = Optional.empty();

    /**
     * Status of islandField
     */
    private ActionType actionType = ActionType.NONE;


    private final Model model;

    private final GUIReader guiReader;


    public IslandFieldPanel(Model model, SocketWrapper sw, GUIReader guiReader) {
        //set IslandFieldPanel's actionType basing on previous actions performed by current Player
        if (guiReader.getSuccessfulRequestsByType(MoveMotherNature.class) == 1) {
            this.setActionType(ActionType.NONE, Optional.empty());
        } else if (guiReader.getSuccessfulRequestsByType(MoveStudent.class) == 3) {
            this.setActionType(ActionType.MOVEMOTHERNATURE, Optional.empty());
        }
        this.setOpaque(true);
        this.setBackground(new Color(105, 186, 233));
        this.setLayout(new CircleLayout());
        this.model = model;
        this.guiReader = guiReader;
        ArrayList<IslandGroup> islandGroups = new ArrayList<>(this.model.getMutableIslandField().getMutableGroups());
        //list containing islands images
        ArrayList<ImageIcon> islandIcons = new ArrayList<>(Arrays.asList(Island1, Island2, Island3));
        ArrayList<JButton> islandButtons = new ArrayList<>(this.model.getMutableIslandField().getMutableGroups().size());
        IslandGroup motherNaturePosition = this.model.getMutableIslandField().getMutableMotherNaturePosition();
        //---DYNAMIC SIZING ISLANDS' IMAGES----
        int widthIsland = 160 + getDimBoost(islandGroups.size());
        int heightIsland = 130 + getDimBoost(islandGroups.size());
        int widthStudent = 35 + (getDimBoost(islandGroups.size()) / 5);
        int heightStudent = 30 + (getDimBoost(islandGroups.size()) / 5);
        int widthMotherNature = 35 + (getDimBoost(islandGroups.size()) / 5);
        int heightMotherNature = 45 + (getDimBoost(islandGroups.size()) / 5);
        int widthTower = 17 + (getDimBoost(islandGroups.size()) / 5);
        int heightTower = 35 + (getDimBoost(islandGroups.size()) / 5);
        Map<PawnColour, Integer> pawnCountMap;
        for (int i = 0; i < islandGroups.size(); i++) {
            //Map containing students and their count on the islandGroup
            pawnCountMap = islandGroups.get(i).getStudentCount();
            //get and scale an island's image
            Image img = islandIcons.get(i % 3).getImage();
            Image newImg = img.getScaledInstance(widthIsland, heightIsland, java.awt.Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(newImg);
            //create a new button with the scaled images
            JButton islandButton = new JButton(icon);
            islandButtons.add(islandButton);
            int finalI = i;
            //add on-click action listener to islandGroup
            islandButton.addActionListener(e -> {
                switch (this.actionType) {
                    case NONE -> {
                    }
                    case CHARACTERCARD -> {
                        PlayCharacterCard playCharacterCard;
                        //create playCharacterCard action
                        if (pawnFromCharacterCard.isPresent()) {
                            playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                    selectedCharacterCard.get(), Optional.of(islandGroups.get(finalI).getMutableIslands().get(0).getId())
                                    , pawnFromCharacterCard, Optional.empty());
                        } else {
                            playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                    selectedCharacterCard.get(), Optional.of(islandGroups.get(finalI).getMutableIslands().get(0).getId())
                                    , Optional.empty(), Optional.empty());
                        }
                        PlayerActionRequest playerActionRequest = new PlayerActionRequest(playCharacterCard);
                        //reset islandFieldPanel's actionType to NONE
                        this.setActionType(ActionType.NONE, Optional.empty());
                        this.guiReader.savePlayerActionRequest(playCharacterCard);
                        try {
                            //send playCharacterCard request to Server
                            sw.sendMessage(playerActionRequest);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    case MOVESTUDENT -> {
                        //create moveStudent action and its playerActionRequest
                        MoveStudent moveStudent = new MoveStudent(this.model.getMutableTurnOrder().getMutableCurrentPlayer().getId(), entrancePositionToMove.get(), MoveDestination.toIsland(islandGroups.get(finalI).getId()));
                        PlayerActionRequest playerAction = new PlayerActionRequest(moveStudent);
                        //save moveStudentAction request inside guiReader
                        this.guiReader.savePlayerActionRequest(moveStudent);
                        try {
                            this.setActionType(ActionType.NONE, Optional.empty());
                            sw.sendMessage(playerAction);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    case MOVEMOTHERNATURE -> {
                        //create moveMotherNature action and its playerActionRequest
                        MoveMotherNature moveMotherNature = new MoveMotherNature(this.model.getMutableTurnOrder().getMutableCurrentPlayer().getId(), getMotherNatureSteps(islandGroups, islandGroups.get(finalI)));
                        PlayerActionRequest playerAction = new PlayerActionRequest(moveMotherNature);
                        //save moveStudentAction request inside guiReader
                        this.guiReader.savePlayerActionRequest(moveMotherNature);
                        try {
                            sw.sendMessage(playerAction);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            });
            //remove border and filled background from islandGroups' button
            islandButton.setPreferredSize(new Dimension(widthIsland, heightIsland));
            islandButton.setBorderPainted(false);
            islandButton.setContentAreaFilled(false);
            islandButton.setFocusPainted(false);
            islandButton.setOpaque(false);
            islandButton.setLayout(new GridLayout(3, 1, -10, 0));
            for (PawnColour p : pawnCountMap.keySet()) {
                if (pawnCountMap.get(p) > 0) {
                    //create new StudentButton
                    StudentButton studentButton = new StudentButton(p, pawnCountMap.get(p), true);
                    //scale StudentButton's image
                    newImg = iconToImage(studentButton.getIcon()).getScaledInstance((int) (widthStudent / 1.5), (int) (heightStudent / 1.5), java.awt.Image.SCALE_SMOOTH);
                    icon = new ImageIcon(newImg);
                    studentButton.setIcon(icon);
                    studentButton.setPreferredSize(new Dimension(widthStudent, heightStudent));
                    islandButton.add(studentButton);
                    //add same IslandGroups' actionListeners to studentButtons
                    studentButton.addActionListener(e -> islandButton.doClick());
                }
            }
            //Draw motherNature only if it's present in IslandGroup
            if (islandGroups.get(i).getMutableIslands().stream().anyMatch(island -> island.getId() == motherNaturePosition.getId())) {
                //load and scale MotherNature's image
                img = motherNature.getImage();
                newImg = img.getScaledInstance(widthMotherNature, heightMotherNature, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);
                //create motherNature's label
                JLabel motherNatureLabel = new JLabel(icon);
                motherNatureLabel.setPreferredSize(new Dimension(widthMotherNature, heightMotherNature));
                //add motherNature to islandGroup's button
                islandButton.add(motherNatureLabel);
            }
            //Drawing eventual tower
            if (islandGroups.get(i).getTowerColour().isPresent()) {
                //scale and set tower's image
                TowerComponent tower = new TowerComponent(islandGroups.get(i).getTowerColour().get(), islandGroups.get(i).getTowerCount());
                newImg = iconToImage(tower.getIcon()).getScaledInstance(widthTower, heightTower, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);
                tower.setIcon(icon);
                //remove borders and filled background from tower's image
                tower.setBorderPainted(false);
                tower.setContentAreaFilled(false);
                tower.setFocusPainted(false);
                tower.setPreferredSize(new Dimension(widthTower, heightTower));
                //add tower's image to IslandGroup's button
                islandButton.add(tower);
            }
            //Drawing eventual NoEntryTile
            if(islandGroups.get(i).getMutableNoEntryTiles().size()>0){
                //scale and set NoEntryTile's image
                NoEntryTileComponent noEntryTileComponent = new NoEntryTileComponent(islandGroups.get(i).getMutableNoEntryTiles().size());
                newImg = iconToImage(noEntryTileComponent.getIcon()).getScaledInstance(40,35, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);
                noEntryTileComponent.setIcon(icon);
                noEntryTileComponent.setPreferredSize(new Dimension(40,35));
                //add NoEntryTile component to island's button
                islandButton.add(noEntryTileComponent);
                //add same IslandGroups' actionListeners to noEntryTileComponent
                noEntryTileComponent.addActionListener(e -> islandButton.doClick());

            }
            islandButton.setToolTipText("<html><p width = 100px>ISLAND GROUP #" + islandGroups.get(i).getId() + "<br>" +
                    "BUTTON NUMBER:" + i + "<br>" +
                    "STUDENTS:<br>" +
                    "RED:" + pawnCountMap.get(PawnColour.RED) + "<br>" +
                    "BLUE:" + pawnCountMap.get(PawnColour.BLUE) + "<br>" +
                    "YELLOW:" + pawnCountMap.get(PawnColour.YELLOW) + "<br>" +
                    "GREEN:" + pawnCountMap.get(PawnColour.GREEN) + "<br>" +
                    "PINK:" + pawnCountMap.get(PawnColour.PINK) + "</p></html>");
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
            this.add(islandButton);
        }
    }

    /**
     * Method used for setting IslandFieldPanel's actionType from external panels
     * @param actionType actionType that IslandFieldPanel will assume
     * @param toRemove PlayerBoard's entrance's index containing the student to move (Optional.empty if action type is not MOVESTUDENT)
     */
    public void setActionType(ActionType actionType, Optional<Integer> toRemove) {
        if (actionType == ActionType.MOVESTUDENT) {
            this.actionType = actionType;
            this.entrancePositionToMove = toRemove;
            return;
        }
        this.actionType = actionType;
    }

    /**
     * Basing on CharacterCard that has been activated, this method setup IslandFieldPanel for send the right PlayerActionRequest to Server
     * @param actionType ActionType that IslandFieldPanel is going to switch to
     * @param card  card's index inside game
     * @param toMove possible pawn to move
     */
    public void setCharacterCardAction(ActionType actionType, Optional<Integer> card, Optional<PawnColour> toMove) {
        this.actionType = actionType;
        if (card.isEmpty()) return;
        this.selectedCharacterCard = card;
        if (toMove.isEmpty()) return;
        this.pawnFromCharacterCard = toMove;
    }

    /**
     * Support method used to calculate difference between clicked islandGroup by user and actual motherNature's islandGroup
     * @param islandGroups list of islandGroups present in game
     * @param destinationIsland IslandGroup that has been clicked by player
     * @return number of steps that motherNature should perform to reach wished IslandGroup
     */
    private int getMotherNatureSteps(ArrayList<IslandGroup> islandGroups, IslandGroup destinationIsland) {
        //get motherNature's islandGroup's index
        int motherNatureIndex = islandGroups.indexOf(model.getMutableIslandField().getMutableMotherNaturePosition());
        int steps = 0;
        //repeat until found islandGroup with same id as the one clicked by user
        while (!islandGroups.get((motherNatureIndex + steps) % islandGroups.size()).equals(destinationIsland)) {
            steps = steps + 1;
        }
        return steps;
    }

    /**
     * Support method used to increase islands' dimensions when the amount of IslandGroups decreases
     * @param IslandsNumbers number of IslandGroups to show
     * @return boost to add to original icons' dimensions
     */
    private int getDimBoost(int IslandsNumbers) {
        return switch (IslandsNumbers) {
            case 12 -> 0;
            case 11 -> 10;
            case 10 -> 20;
            case 9 -> 30;
            case 8 -> 40;
            case 7 -> 50;
            case 6 -> 60;
            case 5 -> 70;
            case default -> 70;
        };
    }

    /**
     * Support method to extract Image from icon
     * @param icon icon to convert
     * @return image represented by icon
     */
    private Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }
}
