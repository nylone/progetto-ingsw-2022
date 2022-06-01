package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Model.Card01;
import it.polimi.ingsw.Model.Card02;
import it.polimi.ingsw.Model.Card03;
import it.polimi.ingsw.Model.Card04;
import it.polimi.ingsw.Model.Card05;
import it.polimi.ingsw.Model.Card06;
import it.polimi.ingsw.Model.Card07;
import it.polimi.ingsw.Model.Card08;
import it.polimi.ingsw.Model.Card09;
import it.polimi.ingsw.Model.Card10;
import it.polimi.ingsw.Model.Card11;
import it.polimi.ingsw.Model.Card12;
import it.polimi.ingsw.Model.CharacterCard;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;
import static it.polimi.ingsw.Client.GUI.IconLoader.sky;

public class CharacterCardsPanel extends JPanel {

    private JLabel firstCard;

    private JLabel secondCard;

    private JLabel thirdCard;


    public CharacterCardsPanel(List<CharacterCard> characterCards){
        this.setLayout(new OverlayLayout(this));
        JLabel pageBackground = new JLabel(sky);
        pageBackground.setLayout(null);
        pageBackground.setBounds(0,0, 720, 480);
        this.add(pageBackground);
        for(CharacterCard characterCard : characterCards){
            switch (characterCard) {
                case Card01 Card01 -> {
                    if(firstCard == null){
                        firstCard = new JLabel(card01);
                        firstCard.setBounds(69,80,140,190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card01);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card01);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                case Card02 Card02 -> {
                    if(firstCard == null){
                        firstCard = new JLabel(card02);
                        firstCard.setBounds(69,80,140, 190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card02);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card02);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                case Card03 Card03 -> {
                    if(firstCard == null){
                        firstCard = new JLabel(card03);
                        firstCard.setBounds(69,80,140, 190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card03);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card03);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                case Card04 Card04-> {
                    if(firstCard == null){
                        firstCard = new JLabel(card04);
                        firstCard.setBounds(69,80,140, 190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card04);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card04);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                case Card05 Card05 -> {
                    if(firstCard == null){
                        firstCard = new JLabel(card05);
                        firstCard.setBounds(69,80,140, 190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card05);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card05);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                case Card06 Card06 -> {
                    if(firstCard == null){
                        firstCard = new JLabel(card06);
                        firstCard.setBounds(69,80,140, 190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card06);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card06);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                case Card07 Card07 -> {
                    if(firstCard == null){
                        firstCard = new JLabel(card07);
                        firstCard.setBounds(69,80,140, 190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card07);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card07);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                case Card08 Card08 -> {
                    if(firstCard == null){
                        firstCard = new JLabel(card08);
                        firstCard.setBounds(69,80,140, 190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card08);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card08);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                case Card09 Card09 -> {
                    if(firstCard == null){
                        firstCard = new JLabel(card09);
                        firstCard.setBounds(69,80,140, 190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card09);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card09);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                case Card10 Card10 -> {
                    if(firstCard == null){
                        firstCard = new JLabel(card10);
                        firstCard.setBounds(69,80,140, 190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card10);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card10);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                case Card11 Card11 -> {
                    if(firstCard == null){
                        firstCard = new JLabel(card11);
                        firstCard.setBounds(69,80,140, 190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card11);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card11);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                case Card12 Card12 -> {
                    if(firstCard == null){
                        firstCard = new JLabel(card12);
                        firstCard.setBounds(69,80,140, 190);
                        pageBackground.add(firstCard);
                        continue;
                    }
                    if(secondCard == null){
                        secondCard = new JLabel(card12);
                        secondCard.setBounds(285, 80, 140, 190);
                        pageBackground.add(secondCard);
                        continue;
                    }
                    if(thirdCard == null){
                        thirdCard = new JLabel(card12);
                        thirdCard.setBounds(502,80,140, 190);
                        pageBackground.add(thirdCard);
                    }
                }
                default -> {}
            }
        }

    }


}
