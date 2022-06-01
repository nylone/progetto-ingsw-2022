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
import java.util.List;

import static it.polimi.ingsw.Client.GUI.IconLoader.card01;
import static it.polimi.ingsw.Client.GUI.IconLoader.card02;
import static it.polimi.ingsw.Client.GUI.IconLoader.card03;
import static it.polimi.ingsw.Client.GUI.IconLoader.card04;
import static it.polimi.ingsw.Client.GUI.IconLoader.card05;
import static it.polimi.ingsw.Client.GUI.IconLoader.card06;
import static it.polimi.ingsw.Client.GUI.IconLoader.card07;
import static it.polimi.ingsw.Client.GUI.IconLoader.card08;
import static it.polimi.ingsw.Client.GUI.IconLoader.card09;
import static it.polimi.ingsw.Client.GUI.IconLoader.card10;
import static it.polimi.ingsw.Client.GUI.IconLoader.card11;
import static it.polimi.ingsw.Client.GUI.IconLoader.card12;
import static it.polimi.ingsw.Client.GUI.IconLoader.sky;

public class CharacterCardsPanel extends JPanel {

    private JLabel firstCard;

    private JLabel secondCard;

    private JLabel thirdCard;

    private void setCardImages(ImageIcon cardImage) {
        if(firstCard == null){
            firstCard = new JLabel(cardImage);
            firstCard.setBounds(69,80,140,190);
        } else if(secondCard == null){
            secondCard = new JLabel(cardImage);
            secondCard.setBounds(285, 80, 140, 190);
        } else if(thirdCard == null){
            thirdCard = new JLabel(cardImage);
            thirdCard.setBounds(502,80,140, 190);
        }
    }


    public CharacterCardsPanel(List<CharacterCard> characterCards){
        this.setLayout(new OverlayLayout(this));
        JLabel pageBackground = new JLabel(sky);
        pageBackground.setLayout(null);
        pageBackground.setBounds(0,0, 720, 480);
        this.add(pageBackground);

        for(CharacterCard characterCard : characterCards){
            switch (characterCard) {
                case Card01 ignored -> setCardImages(card01);
                case Card02 ignored -> setCardImages(card02);
                case Card03 ignored -> setCardImages(card03);
                case Card04 ignored -> setCardImages(card04);
                case Card05 ignored -> setCardImages(card05);
                case Card06 ignored -> setCardImages(card06);
                case Card07 ignored -> setCardImages(card07);
                case Card08 ignored -> setCardImages(card08);
                case Card09 ignored -> setCardImages(card09);
                case Card10 ignored -> setCardImages(card10);
                case Card11 ignored -> setCardImages(card11);
                case Card12 ignored -> setCardImages(card12);
                default -> {}
            }
        }

        pageBackground.add(firstCard);
        pageBackground.add(secondCard);
        pageBackground.add(thirdCard);

    }


}
