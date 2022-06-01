package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Model.*;

import javax.swing.*;
import java.util.List;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class CharacterCardsPanel extends JPanel {

    private JLabel firstCard;

    private JLabel secondCard;

    private JLabel thirdCard;

    public CharacterCardsPanel(List<CharacterCard> characterCards) {
        this.setLayout(new OverlayLayout(this));
        JLabel pageBackground = new JLabel(sky);
        pageBackground.setLayout(null);
        pageBackground.setBounds(0, 0, 720, 480);
        this.add(pageBackground);

        for (CharacterCard characterCard : characterCards) {
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
                default -> {
                }
            }
        }

        pageBackground.add(firstCard);
        pageBackground.add(secondCard);
        pageBackground.add(thirdCard);

    }

    private void setCardImages(ImageIcon cardImage) {
        if (firstCard == null) {
            firstCard = new JLabel(cardImage);
            firstCard.setBounds(69, 80, 140, 190);
        } else if (secondCard == null) {
            secondCard = new JLabel(cardImage);
            secondCard.setBounds(285, 80, 140, 190);
        } else if (thirdCard == null) {
            thirdCard = new JLabel(cardImage);
            thirdCard.setBounds(502, 80, 140, 190);
        }
    }
}
