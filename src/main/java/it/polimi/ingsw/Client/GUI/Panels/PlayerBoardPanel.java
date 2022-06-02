package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.PlayerBoard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class PlayerBoardPanel extends JPanel {
    public PlayerBoardPanel(PlayerBoard pb) {
        //SpringLayout layout = new SpringLayout();
        ArrayList<JButton> assistantCardsLabels = new ArrayList<>(10);
        ArrayList<JButton> entranceStudentsButton = new ArrayList<>(pb.getEntranceSize());
        ArrayList<JButton> assistantCardsLabelToShow;
        JLabel boardBackground = new JLabel(playerBoardBackground);
        JLabel playerBoardLabel = new JLabel(playerBoard);
        JButton assistantCard1Label = new JButton(assistantCard1);
        JButton assistantCard2Label = new JButton(assistantCard2);
        JButton assistantCard3Label = new JButton(assistantCard3);
        JButton assistantCard4Label = new JButton(assistantCard4);
        JButton assistantCard5Label = new JButton(assistantCard5);
        JButton assistantCard6Label = new JButton(assistantCard6);
        JButton assistantCard7Label = new JButton(assistantCard7);
        JButton assistantCard8Label = new JButton(assistantCard8);
        JButton assistantCard9Label = new JButton(assistantCard9);
        JButton assistantCard10Label = new JButton(assistantCard10);

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

        assistantCardsLabels.add(assistantCard1Label);
        assistantCardsLabels.add(assistantCard2Label);
        assistantCardsLabels.add(assistantCard3Label);
        assistantCardsLabels.add(assistantCard4Label);
        assistantCardsLabels.add(assistantCard5Label);
        assistantCardsLabels.add(assistantCard6Label);
        assistantCardsLabels.add(assistantCard7Label);
        assistantCardsLabels.add(assistantCard8Label);
        assistantCardsLabels.add(assistantCard9Label);
        assistantCardsLabels.add(assistantCard10Label);

        assistantCardsLabelToShow = GetCardsToShow(assistantCardsLabels,
                pb.getMutableAssistantCards().stream().filter(assistantCard -> !assistantCard.getUsed()).collect(Collectors.toCollection(ArrayList::new)));


        boardBackground.setLayout(null);
        boardBackground.setBounds(0, 0, 1080, 720);
        playerBoardLabel.setBounds(0,0, 1080, 420);
        int count=0;
        int x = Math.min(1080/assistantCardsLabelToShow.size(), 165);
        for(JButton button : assistantCardsLabelToShow){
            button.setBounds(x*count,465, x, 256);
            count++;
            button.setIcon(new ImageIcon(((ImageIcon) button.getIcon()).getImage().getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH)));
        }
        count = 0;
        int countLeft = 0;
        for(int i=0; i<pb.getEntranceSize(); i++){
            if(i % 2 == 0){
                entranceStudentsButton.get(i).setBounds(100, 50+70*count, 50, 45);
                count++;
            }else{
                entranceStudentsButton.get(i).setBounds(35, 123+70*(countLeft), 50, 45);
                countLeft++;
            }
        }

        this.add(boardBackground);
        boardBackground.add(playerBoardLabel);
        assistantCardsLabelToShow.forEach(boardBackground::add);
        entranceStudentsButton.forEach(playerBoardLabel::add);

        }
    private ArrayList<JButton> GetCardsToShow(ArrayList<JButton> assistantCardsLabels, ArrayList<AssistantCard> assistantCards){
        ArrayList<JButton> assistantsToShow = new ArrayList<>(assistantCards.size());
        for(AssistantCard assistantCard : assistantCards){
            assistantsToShow.add(assistantCardsLabels.get(assistantCard.getPriority()-1));
        }
        return assistantsToShow;
    }
}
