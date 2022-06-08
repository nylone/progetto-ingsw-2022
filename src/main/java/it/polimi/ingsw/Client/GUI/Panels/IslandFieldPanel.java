package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.CircleLayout;
import it.polimi.ingsw.Client.GUI.Components.StudentButton;
import it.polimi.ingsw.Client.GUI.Components.TowerComponent;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.IslandGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class IslandFieldPanel extends JPanel {

    IslandFieldPanel(List<IslandGroup> islandGroups, IslandGroup motherNaturePosition) {
        this.setOpaque(true);
        this.setBackground(new Color(105, 186, 233));
        this.setLayout(new CircleLayout());
        ArrayList<ImageIcon> islandIcons = new ArrayList<>(Arrays.asList(Island1, Island2, Island3));
        int widthIsland = 160 + getDimBoost(islandGroups.size());
        int heightIsland = 130 + getDimBoost(islandGroups.size());
        int widthStudent = 35 + (getDimBoost(islandGroups.size()) / 5);
        int heightStudent = 30 + (getDimBoost(islandGroups.size()) / 5);
        int widthMotherNature = 35 + (getDimBoost(islandGroups.size()) / 5);
        int heightMotherNature = 45 + (getDimBoost(islandGroups.size()) / 5);
        Map<PawnColour, Integer> pawnCountMap;
        for (int i = 0; i < islandGroups.size(); i++) {
            pawnCountMap = islandGroups.get(i).getStudentCount();
            Image img = islandIcons.get(i % 3).getImage();
            Image newImg = img.getScaledInstance(widthIsland, heightIsland, java.awt.Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(newImg);
            JButton IslandButton = new JButton(icon);
            IslandButton.setPreferredSize(new Dimension(widthIsland, heightIsland));
            IslandButton.setBorderPainted(false);
            IslandButton.setContentAreaFilled(false);
            IslandButton.setFocusPainted(false);
            IslandButton.setOpaque(false);
            IslandButton.setLayout(new FlowLayout());
            for (PawnColour p : pawnCountMap.keySet()) {
                if (pawnCountMap.get(p) > 0) {
                    StudentButton studentButton = new StudentButton(p, pawnCountMap.get(p));
                    newImg = iconToImage(studentButton.getIcon()).getScaledInstance(widthStudent, heightStudent, java.awt.Image.SCALE_SMOOTH);
                    icon = new ImageIcon(newImg);
                    studentButton.setIcon(icon);
                    studentButton.setPreferredSize(new Dimension(widthStudent, heightStudent));
                    IslandButton.add(studentButton);
                }
            }
            if (islandGroups.get(i).getMutableIslands().stream().anyMatch(island -> island.getId() == motherNaturePosition.getId())) {
                img = motherNature.getImage();
                newImg = img.getScaledInstance(widthMotherNature, heightMotherNature, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);
                JLabel motherNatureLabel = new JLabel(icon);
                motherNatureLabel.setPreferredSize(new Dimension(widthMotherNature, heightMotherNature));
                IslandButton.add(motherNatureLabel);
            }
            if (islandGroups.get(i).getTowerColour().isPresent()) {
                TowerComponent tower = new TowerComponent(islandGroups.get(i).getTowerColour().get(), islandGroups.get(i).getTowerCount());
                newImg = iconToImage(tower.getIcon()).getScaledInstance(widthMotherNature, heightMotherNature, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);
                tower.setIcon(icon);
                tower.setPreferredSize(new Dimension(widthStudent, heightStudent));
                IslandButton.add(tower);
            }
            IslandButton.setToolTipText("<html><p width = 100px>STUDENTS:<br>" +
                    "RED:" + pawnCountMap.get(PawnColour.RED) + "<br>" +
                    "BLUE:" + pawnCountMap.get(PawnColour.BLUE) + "<br>" +
                    "YELLOW:" + pawnCountMap.get(PawnColour.YELLOW) + "<br>" +
                    "GREEN:" + pawnCountMap.get(PawnColour.GREEN) + "<br>" +
                    "PINK:" + pawnCountMap.get(PawnColour.PINK) + "</p></html>");
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
            this.add(IslandButton);
        }
    }

    private int getDimBoost(int IslandsNumbers) {
        switch (IslandsNumbers) {
            case 12:
                return 0;
            case 11:
                return 10;
            case 10:
                return 20;
            case 9:
                return 30;
            case 8:
                return 40;
            case 7:
                return 50;
            case 6:
                return 60;
            case 5:
                return 70;
            case default:
                return 70;
        }
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
