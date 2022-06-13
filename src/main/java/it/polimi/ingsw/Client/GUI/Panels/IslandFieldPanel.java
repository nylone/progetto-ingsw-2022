package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.ActionType;
import it.polimi.ingsw.Client.GUI.CircleLayout;
import it.polimi.ingsw.Client.GUI.Components.StudentButton;
import it.polimi.ingsw.Client.GUI.Components.TowerComponent;
import it.polimi.ingsw.Client.GUI.GUIReader;
import it.polimi.ingsw.Controller.Actions.ChooseCloudTile;
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

public class IslandFieldPanel extends JPanel {

    private Optional<Integer> entrancePositionToMove = Optional.empty();

    private Optional<Integer> selectedCharacterCard = Optional.empty();

    private Optional<PawnColour> pawnFromCharacterCard = Optional.empty();

    private ActionType actionType = ActionType.NONE;

    private ArrayList<JButton> islandButtons;


    private Model model;

    private GUIReader guiReader;


    public IslandFieldPanel(Model model, SocketWrapper sw, GUIReader guiReader) {
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
        ArrayList<ImageIcon> islandIcons = new ArrayList<>(Arrays.asList(Island1, Island2, Island3));
        this.islandButtons = new ArrayList<>(this.model.getMutableIslandField().getMutableGroups().size());
        IslandGroup motherNaturePosition = this.model.getMutableIslandField().getMutableMotherNaturePosition();
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
            pawnCountMap = islandGroups.get(i).getStudentCount();
            Image img = islandIcons.get(i % 3).getImage();
            Image newImg = img.getScaledInstance(widthIsland, heightIsland, java.awt.Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(newImg);
            JButton islandButton = new JButton(icon);
            this.islandButtons.add(islandButton);
            int finalI = i;
            islandButton.addActionListener(e -> {
                   /* Container c = this.getParent();
                    while (!(c instanceof JTabbedPane jTabbedPane)) {
                        c = c.getParent();
                    }*/
                    switch (this.actionType) {
                        case NONE -> {
                        }
                        case CHARACTERCARD -> {
                            PlayerActionRequest playerActionRequest = null;
                            switch (this.selectedCharacterCard.get()){
                                case 3: {
                                    PlayCharacterCard playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                            selectedCharacterCard.get(),Optional.of(islandGroups.get(finalI).getMutableIslands().get(0).getId())
                                            ,Optional.empty(), Optional.empty());
                                    playerActionRequest = new PlayerActionRequest(playCharacterCard);
                                    this.setActionType(ActionType.NONE, Optional.empty());
                                }
                                case 5: {
                                    PlayCharacterCard playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                            selectedCharacterCard.get(),Optional.empty(), Optional.empty(), Optional.empty());
                                    playerActionRequest = new PlayerActionRequest(playCharacterCard);
                                    this.setActionType(ActionType.NONE, Optional.empty());
                                }
                            }
                            try {
                                sw.sendMessage(playerActionRequest);
                                this.setActionType(ActionType.NONE, Optional.empty());
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        case MOVESTUDENT -> {
                            MoveStudent moveStudent = new MoveStudent(this.model.getMutableTurnOrder().getMutableCurrentPlayer().getId(), entrancePositionToMove.get(), MoveDestination.toIsland(islandGroups.get(finalI).getId()));
                            PlayerActionRequest playerAction = new PlayerActionRequest(moveStudent);
                            this.guiReader.savePlayerActionRequest(moveStudent);
                            try {
                                this.setActionType(ActionType.NONE, Optional.empty());
                                sw.sendMessage(playerAction);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    case MOVEMOTHERNATURE -> {
                        MoveMotherNature moveMotherNature = new MoveMotherNature(this.model.getMutableTurnOrder().getMutableCurrentPlayer().getId(), getMotherNatureSteps(islandGroups, islandGroups.get(finalI)));
                        PlayerActionRequest playerAction = new PlayerActionRequest(moveMotherNature);
                        this.guiReader.savePlayerActionRequest(moveMotherNature);
                        try {
                            sw.sendMessage(playerAction);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }
            });
            islandButton.setPreferredSize(new Dimension(widthIsland, heightIsland));
            islandButton.setBorderPainted(false);
            islandButton.setContentAreaFilled(false);
            islandButton.setFocusPainted(false);
            islandButton.setOpaque(false);
            islandButton.setLayout(new GridLayout(3, 1, -10, 0));
            for (PawnColour p : pawnCountMap.keySet()) {
                if (pawnCountMap.get(p) > 0) {
                    StudentButton studentButton = new StudentButton(p, pawnCountMap.get(p), true);
                    newImg = iconToImage(studentButton.getIcon()).getScaledInstance((int) (widthStudent / 1.5), (int) (heightStudent / 1.5), java.awt.Image.SCALE_SMOOTH);
                    icon = new ImageIcon(newImg);
                    studentButton.setIcon(icon);
                    studentButton.setPreferredSize(new Dimension(widthStudent, heightStudent));
                    islandButton.add(studentButton);
                    studentButton.addActionListener(e -> islandButton.doClick());
                }
            }
            if (islandGroups.get(i).getMutableIslands().stream().anyMatch(island -> island.getId() == motherNaturePosition.getId())) {
                img = motherNature.getImage();
                newImg = img.getScaledInstance(widthMotherNature, heightMotherNature, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);
                JLabel motherNatureLabel = new JLabel(icon);
                motherNatureLabel.setPreferredSize(new Dimension(widthMotherNature, heightMotherNature));
                islandButton.add(motherNatureLabel);
            }
            if (islandGroups.get(i).getTowerColour().isPresent()) {
                TowerComponent tower = new TowerComponent(islandGroups.get(i).getTowerColour().get(), islandGroups.get(i).getTowerCount());
                newImg = iconToImage(tower.getIcon()).getScaledInstance(widthTower, heightTower, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);
                tower.setIcon(icon);
                tower.setBorderPainted(false);
                tower.setContentAreaFilled(false);
                tower.setFocusPainted(false);
                tower.setPreferredSize(new Dimension(widthTower, heightTower));
                islandButton.add(tower);
            }
            islandButton.setToolTipText("<html><p width = 100px>ISLAND GROUP #" + islandGroups.get(i).getId() + "<br>" +
                    "BUTTON NUMBER:"+ i + "<br>"+
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

    public void setActionType(ActionType actionType, Optional<Integer> toRemove){
        if(actionType == ActionType.MOVESTUDENT){
            this.actionType = actionType;
            this.entrancePositionToMove = toRemove;
            return;
        }
        this.actionType = actionType;
    }

    public void setCharacterCardAction(ActionType actionType, Optional<Integer> card, Optional<PawnColour> toMove){
        this.actionType = actionType;
        if(card.isEmpty()) return;
        this.selectedCharacterCard = card;
        if(toMove.isEmpty()) return;
        this.pawnFromCharacterCard = toMove;
    }

    private int getMotherNatureSteps(ArrayList<IslandGroup> islandGroups, IslandGroup destinationIsland){
        int motherNatureIndex = islandGroups.indexOf(model.getMutableIslandField().getMutableMotherNaturePosition());
        int steps=0;
        while(true){
            if(islandGroups.get((motherNatureIndex+steps)%islandGroups.size()).equals(destinationIsland)) break;
            steps = steps+1;
        }
        return steps;
    }

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
