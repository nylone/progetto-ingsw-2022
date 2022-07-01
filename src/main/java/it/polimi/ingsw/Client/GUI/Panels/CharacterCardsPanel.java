package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.ActionType;
import it.polimi.ingsw.Client.GUI.Components.NoEntryTileComponent;
import it.polimi.ingsw.Client.GUI.Components.StudentButton;
import it.polimi.ingsw.Client.GUI.Listeners.CheckBoxListener;
import it.polimi.ingsw.Client.GUI.Listeners.GUISocketListener;
import it.polimi.ingsw.Controller.Actions.PlayCharacterCard;
import it.polimi.ingsw.Misc.OptionalValue;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Enums.StateType;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.PlayerActionRequest;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

/**
 * Class used to draw the 3 characterCards and their eventual contents; it also handles all 12 characterCards actions.
 * This class will be initialized only in advanced Game
 */
public class CharacterCardsPanel extends JPanel {
    public CharacterCardsPanel(Model model, SocketWrapper socketWrapper, GUISocketListener guiSocketListener) {
        UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 14));
        //List containing game's characterCards
        ArrayList<CharacterCard> characterCards = new ArrayList<>(model.getCharacterCards());
        //Label that will contain all others components
        JLabel pageBackground = new JLabel(sky);
        pageBackground.setBounds(0, 0, 1080, 720);
        this.add(pageBackground);
        //list containing characterCards' buttons
        ArrayList<JButton> characterCardsButton = new ArrayList<>(characterCards.size());
        //list containing coins' labels
        ArrayList<JLabel> coinLabels = new ArrayList<>(characterCards.size());
        //list containing characterCardsStates' labels
        ArrayList<JLabel> characterCardsStatelabes = new ArrayList<>(characterCards.size());
        //repeat for all 3 characterCards
        for (int i = 0; i < characterCards.size(); i++) {
            //add a new coin's label to coinLabels list and make it invisible
            coinLabels.add(new JLabel(coin));
            coinLabels.get(i).setVisible(false);
            //add a new characterCardState's label to characterCardsStatelabes list and make it invisible
            characterCardsStatelabes.add(new JLabel());
            characterCardsStatelabes.get(i).setVisible(false);
            JButton button;
            //create a new characterCard's button with the proper image
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
                default -> button = new JButton();
            }
            //add characterCard's info with ToolTipManager CLASS
            button.setToolTipText(printCharacterCardInfo(characterCards.get(i)));
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
            int finalI = i;
            //add on-click actionListener to characterCard's button
            button.addActionListener(e -> {
                // skip execution of the action if a previous action still hasn't been processed by the server
                if (guiSocketListener.awaitingPlayerActionFeedback()) {
                    JOptionPane.showMessageDialog(null, "Please wait for the server to process your previous" +
                            "request before making a new one");
                    return;
                }
                PlayCharacterCard playCharacterCard = null;
                PlayerActionRequest playerActionRequest = null;
                //get JTabbedPane (necessary to switch to another JPanel)
                Container parent = this.getParent();
                while (!(parent instanceof GameInProgressPanel gameInProgressPanel)) {
                    parent = parent.getParent();
                }
                switch (characterCards.get(finalI)) {
                    case Card01 card01 -> {
                        PawnColour toMove;
                        //list containing CharacterCard's pawns
                        PawnColour[] options = card01.getState().toArray(new PawnColour[0]);
                        //create and show JOptionPane
                        int option = JOptionPane.showOptionDialog(null, "Select pawnColour to move to an island", "Select PawnColour",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, options, options[0]);
                        //get pawn to move
                        toMove = PawnColour.getPawnColourFromText(options[option].toString());
                        //switch to first JTabbedPane's tab
                        gameInProgressPanel.setSelectedIndex(0);
                        IslandFieldPanel islandFieldPanel = (IslandFieldPanel) gameInProgressPanel.getSelectedComponent();
                        JOptionPane.showMessageDialog(null, "click on the island on which you want to move the pawn");
                        //set IslandFieldPanel to play this characterCard
                        islandFieldPanel.setCharacterCardAction(ActionType.CHARACTERCARD, OptionalValue.of(finalI), OptionalValue.of(toMove));
                        return;
                    }
                    case Card02 ignored2 -> {
                        //create playCharacterCard and its playerActionReqeust
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    case Card03 ignored3 -> {
                        //switch to first JTabbedPane's pane
                        gameInProgressPanel.setSelectedIndex(0);
                        IslandFieldPanel islandFieldPanel = (IslandFieldPanel) gameInProgressPanel.getSelectedComponent();
                        JOptionPane.showMessageDialog(null, "click on the island on which you want to calculate the influence");
                        //set IslandFieldPanel to play this characterCard
                        islandFieldPanel.setCharacterCardAction(ActionType.CHARACTERCARD, OptionalValue.of(finalI), OptionalValue.empty());
                        return;
                    }
                    case Card04 ignored -> {
                        //create playCharacterCard and its playerActionReqeust
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    case Card05 ignored5 -> {
                        //switch to first JTabbedPane's pane
                        gameInProgressPanel.setSelectedIndex(0);
                        IslandFieldPanel islandFieldPanel = (IslandFieldPanel) gameInProgressPanel.getSelectedComponent();
                        JOptionPane.showMessageDialog(null, "click on the island on which you want to move NoEntry tile");
                        //set IslandFieldPanel to play this characterCard
                        islandFieldPanel.setCharacterCardAction(ActionType.CHARACTERCARD, OptionalValue.of(finalI), OptionalValue.empty());
                        return;
                    }
                    case Card06 ignored6 -> {
                        //create playCharacterCard and its playerActionRequest
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);

                    }
                    case Card07 card07 -> {
                        //create list of PawnColour from characterCard's state
                        ArrayList<PawnColour> pawnsFromCard = card07.getState().stream().map(o -> (PawnColour) o).collect(Collectors.toCollection(ArrayList::new));
                        //create array of JCheckBox having the same size as the previous list
                        JCheckBox[] checkBoxes = new JCheckBox[pawnsFromCard.size()];
                        //create a new CheckBoxListener
                        CheckBoxListener checkBoxListener = new CheckBoxListener(3, checkBoxes);
                        //panel used for showing components inside JOptionPane
                        JPanel optionPanel = new JPanel();
                        for (int j = 0; j < pawnsFromCard.size(); j++) {
                            //create a new JCheckBox and add it to checkBoxes array
                            checkBoxes[j] = new JCheckBox(pawnsFromCard.get(j).toString());
                            //add checkBoxListener to checkBox
                            checkBoxes[j].addItemListener(checkBoxListener);
                            //add checkBox to optionPanel
                            optionPanel.add(checkBoxes[j]);
                        }
                        //create and show JOptionPane
                        int result = JOptionPane.showConfirmDialog(this, optionPanel,
                                "Select pawns ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == -1 || result == 2) return;
                        //clear and reuse pawnsFromCard
                        pawnsFromCard.clear();
                        for (JCheckBox checkBox : checkBoxes) {
                            if (checkBox.isSelected()) {
                                //add a PawnColour of that colour only if the corresponding checkBox has been selected
                                pawnsFromCard.add(PawnColour.getPawnColourFromText(checkBox.getText()));
                            }
                        }
                        for (int h = 0; h < gameInProgressPanel.getTabCount(); h++) {
                            Component component = gameInProgressPanel.getComponentAt(h);
                            //search for CurrentPlayer's PlayerBoardPanel
                            if ((component instanceof PlayerBoardPanel playerBoardPanel) && playerBoardPanel.getPlayerBoardNickname().equals(model.getMutableTurnOrder().getMutableCurrentPlayer().getNickname())) {
                                //switch to that Panel
                                gameInProgressPanel.setSelectedIndex(h);
                                //delegate the remaining part of execution to PlayerBoardPanel
                                playerBoardPanel.PlayCharacterCardEffect(7, finalI, OptionalValue.of(pawnsFromCard));
                            }
                        }
                        return;
                    }
                    case Card08 ignored8 -> {
                        //create playCharacterCard and its playerActionRequest
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, OptionalValue.empty(), OptionalValue.empty(), OptionalValue.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    case Card09 ignored9 -> {
                        PawnColour toExclude;
                        //Array of strings containing all PawnColours
                        String[] options = new String[]{"RED", "PINK", "GREEN", "YELLOW", "BLUE"};
                        //create and show JOptionPane
                        int option = JOptionPane.showOptionDialog(null, "Select pawnColour to make it irrelevant", "Select PawnColour",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, options, options[0]);
                        //get selected pawn to exclude
                        toExclude = PawnColour.getPawnColourFromText(options[option]);
                        //create playCharacterCard action and its playerActionRequest
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, OptionalValue.empty(), OptionalValue.of(toExclude), OptionalValue.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    case Card10 ignored10 -> {
                        for (int h = 0; h < gameInProgressPanel.getTabCount(); h++) {
                            Component component = gameInProgressPanel.getComponentAt(h);
                            //search for CurrentPlayer's PlayerBoardPanel
                            if ((component instanceof PlayerBoardPanel playerBoardPanel) && playerBoardPanel.getPlayerBoardNickname().equals(model.getMutableTurnOrder().getMutableCurrentPlayer().getNickname())) {
                                //switch to that Panel
                                gameInProgressPanel.setSelectedIndex(h);
                                //delegate the remaining part of execution to PlayerBoardPanel
                                playerBoardPanel.PlayCharacterCardEffect(10, finalI, OptionalValue.empty());
                            }
                        }
                        return;
                    }
                    case Card11 card11 -> {
                        PawnColour toMove;
                        //get characterCard's state and convert its elements to String
                        String[] options = card11.getState().stream().map(Object::toString).toArray(String[]::new);
                        //create and show JOptionPane
                        int option = JOptionPane.showOptionDialog(null, "Select the pawn to move", "Select PawnColour",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, options, options[0]);
                        if (option == -1) return;
                        //get selected pawn to move
                        toMove = PawnColour.getPawnColourFromText(options[option]);
                        //create playCharacterCard action and its playerActionRequest
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, OptionalValue.empty(), OptionalValue.of(toMove), OptionalValue.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    case Card12 ignored12 -> {
                        PawnColour toRemove;
                        String[] options = new String[]{"RED", "PINK", "GREEN", "YELLOW", "BLUE"};
                        int option = JOptionPane.showOptionDialog(null, "Select the pawn to remove", "Select a colour",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, options, options[0]);
                        if (option == -1) return;
                        //get selected pawn to remove
                        toRemove = PawnColour.getPawnColourFromText(options[option]);
                        //create playCharacterCard action and its playerActionRequest
                        playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                                finalI, OptionalValue.empty(), OptionalValue.of(toRemove), OptionalValue.empty());
                        playerActionRequest = new PlayerActionRequest(playCharacterCard);
                    }
                    default -> {
                    }
                }
                //save action inside guiReader then send the request to Server
                guiSocketListener.savePlayerActionRequest(playCharacterCard);
                try {
                    socketWrapper.sendMessage(playerActionRequest);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            //print eventual characterCard's state
            checkStatefulCard(characterCards.get(i), characterCardsStatelabes.get(i));
            //draw Coin's image whether the card has been used at least once
            if (characterCards.get(i).getTimeUsed() > 0) coinLabels.get(i).setVisible(true);
        }
        //--ABSOLUTE POSITIONING--
        coinLabels.get(0).setBounds(30, 195, 150, 160);
        coinLabels.get(1).setBounds(371, 195, 150, 160);
        coinLabels.get(2).setBounds(712, 195, 150, 160);
        characterCardsButton.get(0).setBounds(100, 133, 205, 340);
        characterCardsButton.get(1).setBounds(441, 133, 205, 340);
        characterCardsButton.get(2).setBounds(782, 133, 205, 340);
        characterCardsStatelabes.get(0).setBounds(100, 485, 205, 200);
        characterCardsStatelabes.get(1).setBounds(441, 485, 205, 200);
        characterCardsStatelabes.get(2).setBounds(782, 485, 205, 200);
        //add all labels to container
        coinLabels.forEach(pageBackground::add);
        characterCardsButton.forEach(pageBackground::add);
        characterCardsButton.forEach(button -> button.setLayout(null));
        characterCardsStatelabes.forEach(pageBackground::add);
    }

    /**
     * Support method for printing characterCard's info
     *
     * @param characterCard CharacterCard desired by the player
     * @return String containing all card's information
     */
    private String printCharacterCardInfo(CharacterCard characterCard) {
        String info;
        switch (characterCard) {
            case Card01 ignored9 ->
                    info = "<html><p width = 300px>EFFECT: Take 1 Student from this card and place it on " +
                            "an Island of your choice. Then, draw a new Student from the Bag and place it on this card.</p></html>";
            case Card02 ignored -> info = "<html><p width = 300px>EFFECT: During this turn, you take control of any" +
                    " number of Professors even if you have the same number of Students as the player who currently controls them.</p></html>";
            case Card03 ignored1 ->
                    info = "<html><p width = 300px>EFFECT: Choose an Island and resolve the Island as if " +
                            "Mother Nature had ended her movement there. Mother " +
                            "Nature will still move and the Island where she ends her movement will also be resolved.</p></html>";
            case Card04 ignored2 -> info = "<html><p width = 300px>EFFECT: You may move Mother Nature up to 2" +
                    " additional Islands than is indicated by the Assistant card you've played.</p></html>";
            case Card05 ignored3 ->
                    info = "<html><p width = 300px>EFFECT: Place a No Entrytile on an Island of your choice. " +
                            "The first time Mother Nature ends her movement there, put the No Entry tile back onto this card " +
                            "DO NOT calculate influence on that Island, or place any Towers.</p></html>";
            case Card06 ignored4 ->
                    info = "<html><p width = 300px>EFFECT: When resolving a Conquering on an Island, Towers do not count towards influence.</p></html>";
            case Card07 ignored7 ->
                    info = "<html><p width = 300px>EFFECT: you may take up to 3 students from this card and replace them with " +
                            "the same number of Students from your Entrance</html>";
            case Card08 ignored5 ->
                    info = "<html><p width = 300px>EFFECT: During the influence calculation this turn, you count as having 2 more influence</p></html>";
            case Card09 ignored6 ->
                    info = "<html><p width = 300px>EFFECT: Choose a color of Student: during the influence calculation this turn, that color adds no influence</p></html>";
            case Card10 ignored7 ->
                    info = "<html><p width = 300px>EFFECT: You may exchange up to 2 Students between your entrance and your Dining Room</p></html>";
            case Card11 ignored11 ->
                    info = "<html><p width = 300px>EFFECT: Take 1 Student from this card and place it in your Dining Room. " +
                            "Then, draw a new Student from the Bag and place it on this card.</p></html>";
            case Card12 ignored8 ->
                    info = "<html><p width = 300px>EFFECT: Choose a type of Student: every player (including yourself) must return 3 Students of that type " +
                            "from their Dining Room to the bag. If any player has fewer than 3 Students of that type " +
                            "return as many Students as they have.</p></html>";
            case default -> info = "CharacterCard not recognized";
        }
        return info;
    }

    /**
     * Support method, responsible for checking whether the characterCard contains some elements (statefulEffect)
     *
     * @param characterCard CharacterCard to check
     * @param container     Container that will contain eventual characterCard's state
     */
    private void checkStatefulCard(CharacterCard characterCard, JLabel container) {
        //continue only if characterCard has a state
        if (!(characterCard instanceof StatefulEffect)) return;
        //make CharacterCard's state's container visible
        container.setVisible(true);
        ArrayList<StudentButton> studentButtons;
        // arranges student and tiles under card horizontally
        container.setLayout(new FlowLayout());
        //check for CharacterCard's state's type
        if (((StatefulEffect) characterCard).getStateType() == StateType.PAWNCOLOUR) {
            //get characterCard's buttons and add them to container
            studentButtons = getStudentButton(((StatefulEffect) characterCard).getState().stream().map(o -> (PawnColour) o).collect(Collectors.toList()));
            studentButtons.forEach(container::add);
        } else if (((StatefulEffect) characterCard).getStateType() == StateType.NOENTRY) {
            //get NoEntryTiles' labels and add them to container
            container.add(new NoEntryTileComponent(((StatefulEffect) characterCard).getState().size()));
        }
    }

    /**
     * Support method used for getting a list of StudentButtons starting from a list of PawnColours
     *
     * @param pawns List of pawnColours that will be used for creating the studentButtons
     * @return list of StudentButton
     */
    private ArrayList<StudentButton> getStudentButton(List<PawnColour> pawns) {
        EnumMap<PawnColour, Integer> colourIntegerEnumMap = new EnumMap<>(PawnColour.class);
        ArrayList<StudentButton> studentButtons = new ArrayList<>();
        //Count all pawnColours occurrences inside list received as input
        for (PawnColour p : pawns) {
            colourIntegerEnumMap.merge(p, 1, Integer::sum);
        }
        //create and add studentButtons
        for (PawnColour p : colourIntegerEnumMap.keySet()) {
            studentButtons.add(new StudentButton(p, colourIntegerEnumMap.get(p), false));
        }
        return studentButtons;
    }


}
