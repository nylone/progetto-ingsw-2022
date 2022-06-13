package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.ActionType;
import it.polimi.ingsw.Client.GUI.Components.NoEntryTileComponent;
import it.polimi.ingsw.Client.GUI.Components.StudentButton;
import it.polimi.ingsw.Client.GUI.GUIReader;
import it.polimi.ingsw.Controller.Actions.PlayCharacterCard;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.PlayerActionRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

public class CharacterCardsPanel extends JPanel {


    public CharacterCardsPanel(Model model, SocketWrapper socketWrapper, GUIReader guiReader) {
        UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 14));
        ArrayList<CharacterCard> characterCards = new ArrayList<>(model.getCharacterCards());
        JLabel pageBackground = new JLabel(sky);
        pageBackground.setBounds(0, 0, 1080, 720);
        this.add(pageBackground);
        ArrayList<JButton> characterCardsButton = new ArrayList<>(characterCards.size());
        ArrayList<JLabel> coinLabels = new ArrayList<>(characterCards.size());
        ArrayList<JLabel> characterCardsStatelabes = new ArrayList<>(characterCards.size());


        for (int i = 0; i < characterCards.size(); i++) {
            coinLabels.add(new JLabel(coin));
            coinLabels.get(i).setVisible(false);
            characterCardsStatelabes.add(new JLabel());
            characterCardsStatelabes.get(i).setVisible(false);
            JButton button = null;
            switch (characterCards.get(i)) {
                case Card01 ignored -> {
                    button = new JButton(card01);
                    characterCardsButton.add(button);
                }
                case Card02 ignored -> {
                    button = new JButton(card02);
                    characterCardsButton.add(button);
                }
                case Card03 ignored -> {
                    button = new JButton(card03);
                    characterCardsButton.add(button);
                }
                case Card04 ignored -> {
                    button = new JButton(card04);
                    characterCardsButton.add(button);
                }
                case Card05 ignored -> {
                    button = new JButton(card05);
                    characterCardsButton.add(button);
                }
                case Card06 ignored -> {
                    button = new JButton(card06);
                    characterCardsButton.add(button);
                }
                case Card07 ignored -> {
                    button = new JButton(card07);
                    characterCardsButton.add(button);
                }
                case Card08 ignored -> {
                    button = new JButton(card08);
                    characterCardsButton.add(button);
                }
                case Card09 ignored -> {
                    button = new JButton(card09);
                    characterCardsButton.add(button);
                }
                case Card10 ignored -> {
                    button = new JButton(card10);
                    characterCardsButton.add(button);
                }
                case Card11 ignored -> {
                    button = new JButton(card11);
                    characterCardsButton.add(button);
                }
                case Card12 ignored -> {
                    button = new JButton(card12);
                    characterCardsButton.add(button);
                }
                default -> {
                }
            }
            button.setToolTipText(printCharacterCardInfo(characterCards.get(i)));
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
            int finalI = i;
            button.addActionListener(e -> {
                PlayCharacterCard playCharacterCard = null;
                PlayerActionRequest playerActionRequest = null;
                Container c = this.getParent();
                while (!(c instanceof JTabbedPane jTabbedPane)) {
                    c = c.getParent();
                }
                switch (characterCards.get(finalI)){
                    case Card01 card01 -> {
                        //setCharacterCardStateEnabled(characterCardsStatelabes.get(finalI), true);
                        JList jList = new JList(card01.getState().toArray(new Object[0]));
                        JOptionPane.showMessageDialog(
                                null, jList, "Select PawnColour", JOptionPane.PLAIN_MESSAGE);


                    }
                    case Card02 ignored2 -> {
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, Optional.empty(), Optional.empty(),Optional.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    case Card03 Card -> {
                        jTabbedPane.setSelectedIndex(0);
                        IslandFieldPanel islandFieldPanel = (IslandFieldPanel) jTabbedPane.getSelectedComponent();
                        JOptionPane.showMessageDialog(null, "click on the island on which you want to calculate the influence");
                        islandFieldPanel.setCharacterCardAction(ActionType.CHARACTERCARD, Optional.of(finalI),Optional.empty());
                        return;
                    }
                    case Card04 ignored4 -> {
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, Optional.empty(), Optional.empty(),Optional.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    case Card05 ignored5 -> {
                        jTabbedPane.setSelectedIndex(0);
                        IslandFieldPanel islandFieldPanel = (IslandFieldPanel) jTabbedPane.getSelectedComponent();
                        JOptionPane.showMessageDialog(null, "click on the island on which you want to move NoEntry tile");
                        islandFieldPanel.setCharacterCardAction(ActionType.CHARACTERCARD, Optional.of(finalI), Optional.empty());
                        return;
                    }
                    case Card06 ignored6 -> {
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, Optional.empty(), Optional.empty(),Optional.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);

                    }
                    case Card07 card07 -> {
                        ArrayList<PawnColour> pawnsFromCard = card07.getState().stream().map(o -> (PawnColour) o).collect(Collectors.toCollection(ArrayList::new));
                        JCheckBox[] checkBoxes = new JCheckBox[((StatefulEffect) card07).getState().size()];
                        CheckBoxListener checkBoxListener = new CheckBoxListener(3, checkBoxes);
                        for(int j=0; j<pawnsFromCard.size(); j++){
                            checkBoxes[j] = new JCheckBox(pawnsFromCard.get(j).toString());
                            checkBoxes[j].addItemListener(checkBoxListener);
                        }
                        JPanel optionPanel = new JPanel();
                        JOptionPane.showOptionDialog(optionPanel, "Select Pawns from card", "Pick pawns from card",JOptionPane.OK_OPTION,
                                0, null, checkBoxes, null);


                    }
                    case Card08 ignored8 -> {
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, Optional.empty(), Optional.empty(),Optional.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    case Card09 ignored9 -> {
                        PawnColour toExclude;
                        String[] options = new String[] {"RED", "PINK", "GREEN", "YELLOW", "BLUE"};
                        int option =  JOptionPane.showOptionDialog(null, "Select pawnColour to make it irrelevant", "Select PawnColour",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, options, options[0]);
                        System.out.println(option);
                        switch(options[option]){
                            case "RED" -> toExclude = PawnColour.RED;
                            case "YELLOW" -> toExclude = PawnColour.YELLOW;
                            case "PINK" -> toExclude = PawnColour.PINK;
                            case "GREEN" -> toExclude = PawnColour.GREEN;
                            case "BLUE" -> toExclude = PawnColour.BLUE;
                            case default -> toExclude = null;

                        }
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, Optional.empty(), Optional.of(toExclude),Optional.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    case Card10 card10 -> {

                    }
                    case Card11 card11 -> {
                        PawnColour toMove;
                        Object[] options = card11.getState().toArray(new Object[0]);
                        int option =  JOptionPane.showOptionDialog(null, "Select the pawn to move", "Select PawnColour",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, options, options[0]);
                        if(option == -1) return;
                        switch((PawnColour) options[option]){
                            case RED -> toMove = PawnColour.RED;
                            case YELLOW -> toMove = PawnColour.YELLOW;
                            case BLUE -> toMove = PawnColour.BLUE;
                            case PINK -> toMove = PawnColour.PINK;
                            case GREEN -> toMove = PawnColour.GREEN;
                            default -> toMove = null;
                        }
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, Optional.empty(), Optional.of(toMove),Optional.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    case Card12 ignored12 -> {
                        PawnColour toRemove;
                        String[] options = new String[] {"RED", "PINK", "GREEN", "YELLOW", "BLUE"};
                        int option =  JOptionPane.showOptionDialog(null, "Title", "Message",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, options, options[0]);
                        if(option == -1) return;
                        switch(options[option]){
                            case "RED" -> toRemove = PawnColour.RED;
                            case "YELLOW" -> toRemove = PawnColour.YELLOW;
                            case "PINK" -> toRemove = PawnColour.PINK;
                            case "GREEN" -> toRemove = PawnColour.GREEN;
                            case "BLUE" -> toRemove = PawnColour.BLUE;
                            case default -> toRemove = null;

                        }
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, Optional.empty(), Optional.of(toRemove),Optional.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    default -> {}
                }
                guiReader.savePlayerActionRequest(playCharacterCard);
                try {
                    socketWrapper.sendMessage(playerActionRequest);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            checkStatefulCard(characterCards.get(i), characterCardsStatelabes.get(i));
            if (characterCards.get(i).getTimeUsed() > 0) coinLabels.get(i).setVisible(true);
        }

        coinLabels.get(0).setBounds(125, 320, 150, 160);
        coinLabels.get(1).setBounds(465, 320, 150, 160);
        coinLabels.get(2).setBounds(810, 320, 150, 160);
        characterCardsButton.get(0).setBounds(100, 133, 205, 340);
        characterCardsButton.get(1).setBounds(441, 133, 205, 340);
        characterCardsButton.get(2).setBounds(782, 133, 205, 340);
        characterCardsStatelabes.get(0).setBounds(100, 485, 205, 200);
        characterCardsStatelabes.get(1).setBounds(441, 485, 205, 200);
        characterCardsStatelabes.get(2).setBounds(782, 485, 205, 200);
        coinLabels.forEach(pageBackground::add);
        characterCardsButton.forEach(pageBackground::add);
        characterCardsStatelabes.forEach(pageBackground::add);

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

    private void checkStatefulCard(CharacterCard characterCard, JLabel container) {
        if (!(characterCard instanceof StatefulEffect)) return;
        container.setVisible(true);
        ArrayList<StudentButton> studentButtons;
        container.setLayout(new FlowLayout()); // arranges student and tiles under card horizontally
        if (((StatefulEffect) characterCard).getStateType() == StateType.PAWNCOLOUR) {
            studentButtons = getStudentButton(((StatefulEffect) characterCard).getState().stream().map(o -> (PawnColour) o).collect(Collectors.toList()));
            studentButtons.forEach(container::add);
        } else if (((StatefulEffect) characterCard).getStateType() == StateType.NOENTRY) {
            container.add(new NoEntryTileComponent(((StatefulEffect) characterCard).getState().size()));
        }
        //container.add(new StudentButton(PawnColour.RED, 5));
        System.out.println(container.getBounds());
    }

    private ArrayList<StudentButton> getStudentButton(List<PawnColour> pawns) {
        EnumMap<PawnColour, Integer> colourIntegerEnumMap = new EnumMap<>(PawnColour.class);
        ArrayList<StudentButton> studentButtons = new ArrayList<>();
        for (PawnColour p : pawns) {
            colourIntegerEnumMap.merge(p, 1, Integer::sum);
        }
        for (PawnColour p : colourIntegerEnumMap.keySet()) {
            studentButtons.add(new StudentButton(p, colourIntegerEnumMap.get(p), false));
        }
        return studentButtons;
    }

    private PawnColour getPawnColourFromText(String text){
        switch (text){
            case "red" -> {
                return PawnColour.RED;
            }
            case "blue" -> {
                return PawnColour.BLUE;
            }
            case "pink" -> {
                return PawnColour.PINK;
            }case "yellow" -> {
                return PawnColour.YELLOW;
            }
            case "green" -> {
                return PawnColour.GREEN;
            }
        }
        return null;
    }

    private class CheckBoxListener implements ItemListener {
        private final int maxCheckBoxesSelectable;

        private int selectionCounter = 0;

        private final JCheckBox[] checkBoxes;
        private CheckBoxListener(int limit, JCheckBox[] checkBoxes){
            this.maxCheckBoxesSelectable = limit;
            this.checkBoxes = checkBoxes;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox source = (JCheckBox) e.getSource();

            if (source.isSelected()) {
                selectionCounter++;
                // check for max selections:
                if (selectionCounter == maxCheckBoxesSelectable)
                    for (JCheckBox box: checkBoxes)
                        if (!box.isSelected())
                            box.setEnabled(false);
            }
            else {
                selectionCounter--;
                // check for less than max selections:
                if (selectionCounter < maxCheckBoxesSelectable)
                    for (JCheckBox box: checkBoxes)
                        box.setEnabled(true);
            }
        }
    }
}
