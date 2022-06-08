package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.Components.NoEntryTileComponent;
import it.polimi.ingsw.Client.GUI.Components.StudentButton;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class CharacterCardsPanel extends JPanel {

    private final JLabel firstCoinLabel = new JLabel(coin);
    private final JLabel secondCoinLabel = new JLabel(coin);
    private final JLabel thirdCoinLabel = new JLabel(coin);
    private final List<CharacterCard> characterCards;

    private final JLabel characterCard1State = new JLabel();

    private final JLabel characterCard2State = new JLabel();

    private final JLabel characterCard3State = new JLabel();

    private JButton firstCard;
    private JButton secondCard;
    private JButton thirdCard;

    public CharacterCardsPanel(List<CharacterCard> characterCards) {
        UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 14));
        this.characterCards = characterCards;
        JLabel pageBackground = new JLabel(sky);
        pageBackground.setBounds(0, 0, 1080, 720);
        this.add(pageBackground);

        characterCard1State.setBounds(100, 485, 205, 200);
        characterCard2State.setBounds(441, 485, 205, 200);
        characterCard3State.setBounds(782, 485, 205, 200);
        characterCard1State.setVisible(false);
        characterCard2State.setVisible(false);
        characterCard3State.setVisible(false);

        for (CharacterCard characterCard : characterCards) {
            switch (characterCard) {
                case Card01 Card -> setCardImages(card01, Card);
                case Card02 Card -> setCardImages(card02, Card);
                case Card03 Card -> setCardImages(card03, Card);
                case Card04 Card -> setCardImages(card04, Card);
                case Card05 Card -> setCardImages(card05, Card);
                case Card06 Card -> setCardImages(card06, Card);
                case Card07 Card -> setCardImages(card07, Card);
                case Card08 Card -> setCardImages(card08, Card);
                case Card09 Card -> setCardImages(card09, Card);
                case Card10 Card -> setCardImages(card10, Card);
                case Card11 Card -> setCardImages(card11, Card);
                case Card12 Card -> setCardImages(card12, Card);
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
        pageBackground.add(characterCard1State);
        pageBackground.add(characterCard2State);
        pageBackground.add(characterCard3State);

    }

    private void setCardImages(ImageIcon cardImage, CharacterCard characterCard) {
        if (firstCard == null) {
            firstCard = new JButton(cardImage);
            firstCard.setBounds(100, 133, 205, 340);
            firstCard.setToolTipText(printCharacterCardInfo(characterCards.get(0)));
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
            checkStatefulCard(characterCard, characterCard1State);
            if (characterCards.get(0).getTimeUsed() > 0) firstCoinLabel.setVisible(true);
        } else if (secondCard == null) {
            secondCard = new JButton(cardImage);
            secondCard.setBounds(441, 133, 205, 340);
            secondCard.setToolTipText(printCharacterCardInfo(characterCards.get(1)));
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
            checkStatefulCard(characterCard, characterCard2State);
            if (characterCards.get(1).getTimeUsed() > 0) secondCoinLabel.setVisible(true);
        } else if (thirdCard == null) {
            thirdCard = new JButton(cardImage);
            thirdCard.setBounds(782, 133, 205, 340);
            thirdCard.setToolTipText(printCharacterCardInfo(characterCards.get(2)));
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
            checkStatefulCard(characterCard, characterCard3State);
            if (characterCards.get(2).getTimeUsed() > 0) thirdCoinLabel.setVisible(true);
        }
    }

    private void checkStatefulCard(CharacterCard characterCard, JLabel container) {
        System.out.println(characterCard instanceof StatefulEffect);
        if(!(characterCard instanceof StatefulEffect)) return;
        container.setVisible(true);
        ArrayList<StudentButton> studentButtons;
        container.setLayout(new FlowLayout()); // arranges student and tiles under card horizontally
        if(((StatefulEffect) characterCard).getStateType() == StateType.PAWNCOLOUR){
            studentButtons = getStudentButton(((StatefulEffect) characterCard).getState().stream().map(o -> (PawnColour) o).collect(Collectors.toList()));
            studentButtons.forEach(container::add);
        }else if(((StatefulEffect) characterCard).getStateType() == StateType.NOENTRY){
            System.out.println("add no entry tile");
            container.add(new NoEntryTileComponent(((StatefulEffect) characterCard).getState().size()));
        }
        //container.add(new StudentButton(PawnColour.RED, 5));
        System.out.println(container.getBounds());
    }

    private String printCharacterCardInfo(CharacterCard characterCard) {
        String info;
        switch (characterCard) {
            case Card01 ignored9 ->
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

    private ArrayList<StudentButton> getStudentButton(List<PawnColour> pawns){
        EnumMap<PawnColour, Integer> colourIntegerEnumMap = new EnumMap<>(PawnColour.class);
        ArrayList<StudentButton> studentButtons = new ArrayList<>();
        for(PawnColour p : pawns){
            colourIntegerEnumMap.merge(p,1,Integer::sum);
        }
        for(PawnColour p : colourIntegerEnumMap.keySet()){
            System.out.println("colour:"+p+" amount:"+colourIntegerEnumMap.get(p));
            studentButtons.add(new StudentButton(p, colourIntegerEnumMap.get(p)));
        }
        return studentButtons;
    }
}
