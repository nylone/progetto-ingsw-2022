package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.Model.TowerStorage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class PlayerBoardPanel extends JPanel {
    public PlayerBoardPanel(PlayerBoard pb, List<PawnColour> teachers, TowerStorage towerStorage) {
        ArrayList<JButton> assistantCardsLabels = new ArrayList<>(10);
        ArrayList<JButton> entranceStudentsButton = new ArrayList<>(pb.getEntranceSize());
        ArrayList<JLabel> towersLabels = new ArrayList<>(towerStorage.getTowerCount());
        Map<PawnColour, ArrayList<JButton>> diningRoomButtons = new EnumMap<>(PawnColour.class);
        JPanel assistantCardsPanel = new JPanel();
        assistantCardsPanel.setLayout(new FlowLayout());
        for (PawnColour p : PawnColour.values()) {
            diningRoomButtons.put(p, new ArrayList<>(pb.getDiningRoomCount(p)));
        }
        for(int i=0; i<towerStorage.getTowerCount(); i++){
            switch (towerStorage.getColour()){
                case GRAY -> towersLabels.add(new JLabel(GrayTower));
                case BLACK -> towersLabels.add(new JLabel(BlackTower));
                case WHITE -> towersLabels.add(new JLabel(WhiteTower));
            }
        }

        for(PawnColour p : diningRoomButtons.keySet()){
            for(int i=0; i<pb.getDiningRoomCount(p); i++){
                switch (p){
                    case RED -> diningRoomButtons.get(p).add(new JButton(RedStudent));
                    case GREEN -> diningRoomButtons.get(p).add(new JButton(GreenStudent));
                    case YELLOW -> diningRoomButtons.get(p).add(new JButton(YellowStudent));
                    case BLUE -> diningRoomButtons.get(p).add(new JButton(BlueStudent));
                    case PINK -> diningRoomButtons.get(p).add(new JButton(PinkStudent));
                }
                diningRoomButtons.get(p).get(diningRoomButtons.get(p).size()-1).setBorderPainted(false);
                diningRoomButtons.get(p).get(diningRoomButtons.get(p).size()-1).setContentAreaFilled(false);
                diningRoomButtons.get(p).get(diningRoomButtons.get(p).size()-1).setFocusPainted(false);
                diningRoomButtons.get(p).get(diningRoomButtons.get(p).size()-1).setOpaque(false);
            }
        }

        ArrayList<JButton> assistantCardsLabelToShow;
        JLabel boardBackground = new JLabel(playerBoardBackground);
        JLabel playerBoardLabel = new JLabel(playerBoard);
        JButton assistantCard1Button = new JButton(assistantCard1);
        JButton assistantCard2Button = new JButton(assistantCard2);
        JButton assistantCard3Button = new JButton(assistantCard3);
        JButton assistantCard4Button = new JButton(assistantCard4);
        JButton assistantCard5Button = new JButton(assistantCard5);
        JButton assistantCard6Button = new JButton(assistantCard6);
        JButton assistantCard7Button = new JButton(assistantCard7);
        JButton assistantCard8Button = new JButton(assistantCard8);
        JButton assistantCard9Button = new JButton(assistantCard9);
        JButton assistantCard10Button = new JButton(assistantCard10);

        JLabel redTeacherLabel = new JLabel(RedTeacher);
        JLabel blueTeacherLabel = new JLabel(BlueTeacher);
        JLabel pinkTeacherLabel = new JLabel(PinkTeacher);
        JLabel greenTeacherLabel = new JLabel(GreenTeacher);
        JLabel yellowTeacherLabel = new JLabel(YellowTeacher);

        for(int i=0; i<pb.getEntranceSize(); i++){
            switch (pb.getEntranceStudents().get(i).get()){
                case RED -> entranceStudentsButton.add(new JButton(RedStudent));
                case GREEN -> entranceStudentsButton.add(new JButton(GreenStudent));
                case YELLOW -> entranceStudentsButton.add(new JButton(YellowStudent));
                case BLUE -> entranceStudentsButton.add(new JButton(BlueStudent));
                case PINK -> entranceStudentsButton.add(new JButton(PinkStudent));
            }
            entranceStudentsButton.get(entranceStudentsButton.size()-1).setBorderPainted(false);
            entranceStudentsButton.get(entranceStudentsButton.size()-1).setContentAreaFilled(false);
            entranceStudentsButton.get(entranceStudentsButton.size()-1).setFocusPainted(false);
            entranceStudentsButton.get(entranceStudentsButton.size()-1).setOpaque(false);
        }



        assistantCardsLabels.add(assistantCard1Button);
        assistantCardsLabels.add(assistantCard2Button);
        assistantCardsLabels.add(assistantCard3Button);
        assistantCardsLabels.add(assistantCard4Button);
        assistantCardsLabels.add(assistantCard5Button);
        assistantCardsLabels.add(assistantCard6Button);
        assistantCardsLabels.add(assistantCard7Button);
        assistantCardsLabels.add(assistantCard8Button);
        assistantCardsLabels.add(assistantCard9Button);
        assistantCardsLabels.add(assistantCard10Button);

        assistantCardsLabelToShow = GetCardsToShow(assistantCardsLabels,
                pb.getMutableAssistantCards().stream().filter(assistantCard -> !assistantCard.getUsed()).collect(Collectors.toCollection(ArrayList::new)));
        assistantCardsLabelToShow.forEach(jButton -> {
                    jButton.setBorderPainted(false);
                    jButton.setContentAreaFilled(false);
                    jButton.setFocusPainted(false);
                    jButton.setOpaque(false);
                }
                );
        assistantCardsPanel.setPreferredSize( new Dimension(assistantCardsLabelToShow.size()*165, 250));
        JScrollPane assistantCardsScrollPane = new JScrollPane(assistantCardsPanel);
        assistantCardsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        assistantCardsScrollPane.setOpaque(false);
        assistantCardsScrollPane.getViewport().setOpaque(false);
        assistantCardsPanel.setOpaque(false);



        boardBackground.setLayout(null);
        boardBackground.setBounds(0, 0, 1080, 720);
        playerBoardLabel.setBounds(0,0, 1080, 420);
        redTeacherLabel.setBounds(765, 120, 50, 45);
        greenTeacherLabel.setBounds(765,50,50,45);
        yellowTeacherLabel.setBounds(765,185,50,45);
        pinkTeacherLabel.setBounds(765,257,50,45);
        blueTeacherLabel.setBounds(765,325,50,45);

        redTeacherLabel.setVisible(false);
        blueTeacherLabel.setVisible(false);
        greenTeacherLabel.setVisible(false);
        yellowTeacherLabel.setVisible(false);
        pinkTeacherLabel.setVisible(false);
        for(PawnColour p : teachers){
            switch (p){
                case RED -> redTeacherLabel.setVisible(true);
                case GREEN -> greenTeacherLabel.setVisible(true);
                case YELLOW -> yellowTeacherLabel.setVisible(true);
                case BLUE -> blueTeacherLabel.setVisible(true);
                case PINK -> pinkTeacherLabel.setVisible(true);
            }
        }
        int count=0;
        int secondCount = 0;
        for(int i=0; i<pb.getEntranceSize(); i++){
            if(i % 2 == 0){
                entranceStudentsButton.get(i).setBounds(100, 50+70*count, 50, 45);
                count++;
            }else{
                entranceStudentsButton.get(i).setBounds(35, 123+70*(secondCount), 50, 45);
                secondCount++;
            }
        }
        count = 0;
        secondCount = 0;
        for(int i=0; i<towerStorage.getTowerCount(); i++){
            if(i % 2 == 0){
                towersLabels.get(i).setBounds(880,50+70*count, 50,75);
                count++;
            }else{
                towersLabels.get(i).setBounds(965,50+70*secondCount,50,75);
                secondCount++;
            }
        }

        for(PawnColour p : diningRoomButtons.keySet()){
            count = 0;
            for(int i = 0; i<pb.getDiningRoomCount(p); i++){
                switch (p){
                    case RED -> diningRoomButtons.get(p).get(i).setBounds(200+50*count,120,50,45);
                    case GREEN -> diningRoomButtons.get(p).get(i).setBounds(200+50*count,50,50,45);
                    case YELLOW -> diningRoomButtons.get(p).get(i).setBounds(200+50*count,190,50,45);
                    case BLUE -> diningRoomButtons.get(p).get(i).setBounds(200+50*count,330,50,45);
                    case PINK -> diningRoomButtons.get(p).get(i).setBounds(200+50*count,260,50,45);
                }
                count++;
            }
        }

        assistantCardsScrollPane.setBounds(0, 430, 1080, 290);
        this.add(boardBackground);
        boardBackground.add(playerBoardLabel);
        assistantCardsLabelToShow.forEach(assistantCardsPanel::add);
        boardBackground.add(assistantCardsScrollPane);
        entranceStudentsButton.forEach(playerBoardLabel::add);
        towersLabels.forEach(playerBoardLabel::add);
        playerBoardLabel.add(redTeacherLabel);
        playerBoardLabel.add(blueTeacherLabel);
        playerBoardLabel.add(greenTeacherLabel);
        playerBoardLabel.add(yellowTeacherLabel);
        playerBoardLabel.add(pinkTeacherLabel);
        for(PawnColour p : diningRoomButtons.keySet()){
            diningRoomButtons.get(p).forEach(playerBoardLabel::add);
        }

        }
    private ArrayList<JButton> GetCardsToShow(ArrayList<JButton> assistantCardsLabels, ArrayList<AssistantCard> assistantCards){
        ArrayList<JButton> assistantsToShow = new ArrayList<>(assistantCards.size());
        for(AssistantCard assistantCard : assistantCards){
            assistantsToShow.add(assistantCardsLabels.get(assistantCard.getPriority()-1));
        }
        return assistantsToShow;
    }
}
