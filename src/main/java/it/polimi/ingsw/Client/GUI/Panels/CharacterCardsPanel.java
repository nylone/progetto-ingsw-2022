package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class CharacterCardsPanel extends JPanel {

    private final JLabel firstCoinLabel = new JLabel(coin);
    private final JLabel secondCoinLabel = new JLabel(coin);
    private final JLabel thirdCoinLabel = new JLabel(coin);
    private final List<CharacterCard> characterCards;
    private JLabel firstCard;
    private JLabel secondCard;
    private JLabel thirdCard;

    public CharacterCardsPanel(List<CharacterCard> characterCards) {
        UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 14));
        this.characterCards = characterCards;
        JLabel pageBackground = new JLabel(sky);
        pageBackground.setLayout(null);
        pageBackground.setBounds(0, 0, 1080, 720);
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

        firstCoinLabel.setBounds(125, 320, 150, 160);
        firstCoinLabel.setVisible(false);
        secondCoinLabel.setBounds(465, 320, 150, 160);
        secondCoinLabel.setVisible(false);
        thirdCoinLabel.setBounds(810, 320, 150, 160);
        thirdCoinLabel.setVisible(false);
        pageBackground.add(firstCoinLabel);
        pageBackground.add(secondCoinLabel);
        pageBackground.add(thirdCoinLabel);
        pageBackground.add(firstCard);
        pageBackground.add(secondCard);
        pageBackground.add(thirdCard);

    }

    private void setCardImages(ImageIcon cardImage) {
        if (firstCard == null) {
            firstCard = new JLabel(cardImage);
            firstCard.setBounds(100, 133, 205, 340);
            firstCard.setToolTipText(printCharacterCardInfo(characterCards.get(0)));
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
            if (characterCards.get(0).getTimeUsed() > 0) firstCoinLabel.setVisible(true);
        } else if (secondCard == null) {
            secondCard = new JLabel(cardImage);
            secondCard.setBounds(441, 133, 205, 340);
            secondCard.setToolTipText(printCharacterCardInfo(characterCards.get(1)));
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
            if (characterCards.get(1).getTimeUsed() > 0) secondCoinLabel.setVisible(true);
        } else if (thirdCard == null) {
            thirdCard = new JLabel(cardImage);
            thirdCard.setBounds(782, 133, 205, 340);
            thirdCard.setToolTipText(printCharacterCardInfo(characterCards.get(2)));
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
            if (characterCards.get(2).getTimeUsed() > 0) thirdCoinLabel.setVisible(true);
        }
    }

    private String printCharacterCardInfo(CharacterCard characterCard) {
        String info;
        switch (characterCard) {
            case Card01 card01 ->
                    info = "<html><p width = 300px>EFFECT: Take 1 Student from this card and place it on" +
                            "an Island of your choice. Then, draw a new Student from the Bag and place it on this card.</p></html>";
            case Card02 ignored -> info = "<html><p width = 300px>EFFECT: During this turn, you take control of any" +
                    " number of Professors even if you have the same number of Students as the player who currently controls them.</p></html>";
            case Card03 ignored1 ->
                    info = "<html><p width = 300px>EFFECT: Choose an Island and resolve the Island as if" +
                            "Mother Nature had ended her movement there. Mother" +
                            "Nature will still move and the Island where she ends her movement will also be resolved.</p></html>";
            case Card04 ignored2 -> info = "<html><p width = 300px>EFFECT: You may move Mother Nature up to 2" +
                    " additional Islands than is indicated by the Assistant card you've played.</p></html>";
            case Card05 ignored3 ->
                    info = "<html><p width = 300px>EFFECT: Place a No Entrytile on an Island of your choice." +
                            "The first time Mother Nature ends her movement there, put the No Entry tile back onto this card" +
                            "DO NOT calculate influence on that Island, or place any Towers.</p></html>";
            case Card06 ignored4 ->
                    info = "<html><p width = 300px>EFFECT: When resolving a Conquering on an Island, Towers do not count towards influence.</p></html>";
            case Card07 card07 ->
                    info = "<html><p width = 300px>EFFECT: you may take up to 3 students from this card and replace them with " +
                            "the same number of Students from your Entrance</html>";
            case Card08 ignored5 ->
                    info = "<html><p width = 300px>EFFECT: During the influence calculation this turn, you count as having 2 more influence</p></html>";
            case Card09 ignored6 ->
                    info = "<html><p width = 300px>EFFECT: Choose a color of Student: during the influence calculation this turn, that color adds no influence</p></html>";
            case Card10 ignored7 ->
                    info = "<html><p width = 300px>EFFECT: You may exchange up to 2 Students between your entrance and your Dining Room</p></html>";
            case Card11 card11 ->
                    info = "<html><p width = 300px>EFFECT: Take 1 Student from this card and place it in your Dining Room." +
                            "Then, draw a new Student from the Bag and place it on this card.</p></html>";
            case Card12 ignored8 ->
                    info = "<html><p width = 300px>EFFECT: Choose a type of Student: every player (including yourself) must return 3 Students of that type" +
                            "from their Dining Room to the bag. If any player has fewer than 3 Students of that type" +
                            "return as many Students as they have.</p></html>";
            case default -> info = "CharacterCard not recognized";
        }
        return info;
    }
}
