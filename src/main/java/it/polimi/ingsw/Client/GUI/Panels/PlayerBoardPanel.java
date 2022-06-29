package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.ActionType;
import it.polimi.ingsw.Client.GUI.Listeners.CheckBoxListener;
import it.polimi.ingsw.Client.GUI.Components.StudentButton;
import it.polimi.ingsw.Client.GUI.Listeners.GUISocketListener;
import it.polimi.ingsw.Controller.Actions.MoveStudent;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Controller.Actions.PlayCharacterCard;
import it.polimi.ingsw.Controller.MoveDestination;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Misc.SerializableOptional;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.PlayerActionRequest;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Client.GUI.IconLoader.*;

/**
 * Class containing all the elements necessary to graphically represent both the player’s playerBoard and the assistant cards still usable. It also implements
 * some logic of characterCard's actions which require to interact with the playerboard in order to work properly.
 */
public class PlayerBoardPanel extends JPanel {

    /**
     * SocketWrapper necessary to send actions from GUI to server
     */
    private final SocketWrapper socketWrapper;

    /**
     * Contains player's PlayerBoard information
     */
    private final PlayerBoard player;

    /**
     * Contains game's information
     */
    private final Model model;

    /**
     * Contains GuiReader's information necessary to record user's requests during his turn
     */
    private final GUISocketListener guiSocketListener;

    /**
     * Create a new PlayerBoardPanel
     *
     * @param pb            Player's playerboard to represent
     * @param model         Game's model
     * @param socketWrapper socketWrapper to communicate with Server
     * @param guiSocketListener     guiReader from GameInProgressPanel
     */
    public PlayerBoardPanel(PlayerBoard pb, Model model, SocketWrapper socketWrapper, GUISocketListener guiSocketListener) {
        this.player = pb;
        this.guiSocketListener = guiSocketListener;
        this.socketWrapper = socketWrapper;
        this.model = model;
        //list containing teachers owned by the player
        List<PawnColour> teachers = model.getOwnTeachers(this.player);
        //towerStorage owned by the player
        TowerStorage towerStorage = model.getTeamMapper().getMutableTowerStorage(this.player);
        //Get current player
        TurnOrder turnOrder = model.getMutableTurnOrder();
        //create List that will contain assistantCards' buttons
        ArrayList<JButton> assistantCardsLabels = new ArrayList<>(10);
        //create List that will contain Entrance's students' buttons
        ArrayList<JButton> entranceStudentsButton = new ArrayList<>(this.player.getEntranceSize());
        //create List that will contain Towers' labels
        ArrayList<JLabel> towersLabels = new ArrayList<>(towerStorage.getTowerCount());
        //Map that associates every pawnColour to an arrayList of JButton
        Map<PawnColour, ArrayList<JButton>> diningRoomButtons = new EnumMap<>(PawnColour.class);
        //Contains assistantCards' buttons
        JPanel assistantCardsPanel = new JPanel();
        assistantCardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        //create label containing coin amount

        //initialize diningRoomButtons' map
        for (PawnColour p : PawnColour.values()) {
            diningRoomButtons.put(p, new ArrayList<>(this.player.getDiningRoomCount(p)));
        }
        //Basing on towerStorage's colour, labels contained by towersLabels have different images
        for (int i = 0; i < towerStorage.getTowerCount(); i++) {
            switch (towerStorage.getColour()) {
                case GRAY -> towersLabels.add(new JLabel(GrayTower));
                case BLACK -> towersLabels.add(new JLabel(BlackTower));
                case WHITE -> towersLabels.add(new JLabel(WhiteTower));
            }
        }
        //Draw students inside diningRoom
        for (PawnColour p : diningRoomButtons.keySet()) {
            for (int i = 0; i < this.player.getDiningRoomCount(p); i++) {
                //Basing on pawnColour, labels have different images
                switch (p) {
                    case RED -> diningRoomButtons.get(p).add(new StudentButton(PawnColour.RED, 1, false));
                    case GREEN -> diningRoomButtons.get(p).add(new StudentButton(PawnColour.GREEN, 1, false));
                    case YELLOW -> diningRoomButtons.get(p).add(new StudentButton(PawnColour.YELLOW, 1, false));
                    case BLUE -> diningRoomButtons.get(p).add(new StudentButton(PawnColour.BLUE, 1, false));
                    case PINK -> diningRoomButtons.get(p).add(new StudentButton(PawnColour.PINK, 1, false));
                }
                //Remove borders and filling from every button
                diningRoomButtons.get(p).get(diningRoomButtons.get(p).size() - 1).setBorderPainted(false);
                diningRoomButtons.get(p).get(diningRoomButtons.get(p).size() - 1).setContentAreaFilled(false);
                diningRoomButtons.get(p).get(diningRoomButtons.get(p).size() - 1).setFocusPainted(false);
                diningRoomButtons.get(p).get(diningRoomButtons.get(p).size() - 1).setOpaque(false);
            }
        }
        //Draw students inside entrance
        for (int i = 0; i < this.player.getEntranceSize(); i++) {
            if (this.player.getEntranceStudents().get(i).isEmpty()) {
                entranceStudentsButton.add(new JButton(""));
                entranceStudentsButton.get(i).setVisible(false);
                continue;
            }
            switch (this.player.getEntranceStudents().get(i).get()) {
                //Basing on pawnColour, labels have different images
                case RED -> entranceStudentsButton.add(new StudentButton(PawnColour.RED, 1, false));
                case GREEN -> entranceStudentsButton.add(new StudentButton(PawnColour.GREEN, 1, false));
                case YELLOW -> entranceStudentsButton.add(new StudentButton(PawnColour.YELLOW, 1, false));
                case BLUE -> entranceStudentsButton.add(new StudentButton(PawnColour.BLUE, 1, false));
                case PINK -> entranceStudentsButton.add(new StudentButton(PawnColour.PINK, 1, false));
            }
            //Remove borders and filling from every button
            entranceStudentsButton.get(i).setBorderPainted(false);
            entranceStudentsButton.get(i).setContentAreaFilled(false);
            entranceStudentsButton.get(i).setFocusPainted(false);
            entranceStudentsButton.get(i).setOpaque(false);
            int finalI = i;
            //add on-click action listener to Entrance's students' buttons
            entranceStudentsButton.get(i).addActionListener(e -> {
                //create options that will be displayed on JOptionPane
                String[] buttons = {"DiningRoom", "Island"};
                //create and show JoptionPane
                int returnValue = JOptionPane.showOptionDialog(this, "Where do you want to send this pawn?", "Destination ", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
                Container c = this.getParent();
                while (!(c instanceof JTabbedPane jTabbedPane)) {
                    //get JTabbedPane
                    c = c.getParent();
                }
                //if user clicked first button (DiningRoom)
                if (returnValue == 0) {
                    //create and send moveStudent action
                    MoveStudent moveStudent = new MoveStudent(this.player.getId(), finalI, MoveDestination.toDiningRoom());
                    PlayerActionRequest playerAction = new PlayerActionRequest(moveStudent);
                    this.guiSocketListener.savePlayerActionRequest(moveStudent);
                    try {
                        socketWrapper.sendMessage(playerAction);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (returnValue == 1) { //if the user clicked second button (Island)
                    //switch jTabbedPane to first tab
                    jTabbedPane.setSelectedIndex(0);
                    //get IslandFieldPanel
                    IslandFieldPanel islandFieldPanel = (IslandFieldPanel) jTabbedPane.getSelectedComponent();
                    //Set correct actionType inside islandFieldPanel
                    islandFieldPanel.setActionType(ActionType.MOVESTUDENT, SerializableOptional.of(finalI));
                }
            });
        }

        ArrayList<JButton> assistantCardsButtonsToShow;
        //label containing all others elements
        JLabel boardBackground = new JLabel(playerBoardBackground);
        //label containing PlayerBoard
        JLabel playerBoardLabel = new JLabel(playerBoard);
        //----labels containing assistantCards----
        ArrayList<ImageIcon> assistantCardsIcon = new ArrayList<>(Arrays.asList(assistantCard1, assistantCard2, assistantCard3, assistantCard4, assistantCard5, assistantCard6, assistantCard7, assistantCard8, assistantCard9, assistantCard10));
        ArrayList<JButton> assistantCardsButtons = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            //create assistantCard's button
            JButton assistantCardButton = new JButton(assistantCardsIcon.get(i));
            int finalI = i + 1;
            //add on-click actionListener to assistantCard's button
            assistantCardButton.addActionListener(e -> {
                //enable button only if a playAssistantCard action has not been played
                if (guiSocketListener.getSuccessfulRequestsByType(PlayAssistantCard.class) == 0) {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    //create optionPane for confirmation
                    int dialogResult = JOptionPane.showConfirmDialog(this, "Confirm to play assistant card with priority: " + finalI + "?", "PlayAssistant card confirmation", dialogButton);
                    if (dialogResult == 0) {
                        //if the player clicked first button (YES) then create and send the playAssistantCard action
                        PlayAssistantCard playAssistantCard = new PlayAssistantCard(this.player.getId(), finalI);
                        PlayerActionRequest playerAction = new PlayerActionRequest(playAssistantCard);
                        this.guiSocketListener.savePlayerActionRequest(playAssistantCard);
                        try {
                            socketWrapper.sendMessage(playerAction);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            });
            //add assistantCard's button to assistantCardsButtons arrayList
            assistantCardsButtons.add(assistantCardButton);
        }
        //----labels containing Teachers----
        JLabel redTeacherLabel = new JLabel(RedTeacher);
        JLabel blueTeacherLabel = new JLabel(BlueTeacher);
        JLabel pinkTeacherLabel = new JLabel(PinkTeacher);
        JLabel greenTeacherLabel = new JLabel(GreenTeacher);
        JLabel yellowTeacherLabel = new JLabel(YellowTeacher);
        //add all assistantcardLabels to support ArrayList
        assistantCardsLabels.addAll(assistantCardsButtons);
        //get unused assistantCards
        ArrayList<AssistantCard> availableAssistants = this.player.getMutableAssistantCards()
                .stream().filter(assistantCard -> !assistantCard.getUsed())
                .collect(Collectors.toCollection(ArrayList::new));
        //from the unused cards, extract the card with a priority not selected before by other players
        for (PlayerBoard p : turnOrder.getCurrentTurnOrder()) {
            if (turnOrder.getMutableSelectedCard(p).isPresent()) {
                availableAssistants.removeIf(assistantCard -> assistantCard.getPriority() == turnOrder.getMutableSelectedCard(p).get().getPriority());
            }
        }
        assistantCardsButtonsToShow = GetCardsToShow(assistantCardsLabels, availableAssistants);
        //Remove borders and filling from every button
        assistantCardsButtonsToShow.forEach(jButton -> {
                    jButton.setBorderPainted(false);
                    jButton.setContentAreaFilled(false);
                    jButton.setFocusPainted(false);
                    jButton.setOpaque(false);
                    //jButton.addActionListener(e -> playAssistantCard(e));
                }
        );

        assistantCardsPanel.setPreferredSize(new Dimension(assistantCardsButtonsToShow.size() * 205, 250));
        //add Panel containing assistantCards as parameter for JScrollPane's constructor
        JScrollPane assistantCardsScrollPane = new JScrollPane(assistantCardsPanel);
        //remove JScrollPane's borders
        assistantCardsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        assistantCardsScrollPane.setOpaque(false);
        assistantCardsScrollPane.getViewport().setOpaque(false);
        //remove assistantCardsPanel's background
        assistantCardsPanel.setOpaque(false);
        //hide Teachers' labels
        redTeacherLabel.setVisible(false);
        blueTeacherLabel.setVisible(false);
        greenTeacherLabel.setVisible(false);
        yellowTeacherLabel.setVisible(false);
        pinkTeacherLabel.setVisible(false);
        //show PlayerBoard's teachers
        for (PawnColour p : teachers) {
            switch (p) {
                case RED -> redTeacherLabel.setVisible(true);
                case GREEN -> greenTeacherLabel.setVisible(true);
                case YELLOW -> yellowTeacherLabel.setVisible(true);
                case BLUE -> blueTeacherLabel.setVisible(true);
                case PINK -> pinkTeacherLabel.setVisible(true);
            }
        }
        //---ABSOLUTE POSITIONING----
        boardBackground.setBounds(0, 0, 1080, 720);
        playerBoardLabel.setBounds(0, 0, 1080, 420);
        redTeacherLabel.setBounds(765, 120, 50, 45);
        greenTeacherLabel.setBounds(765, 50, 50, 45);
        yellowTeacherLabel.setBounds(765, 185, 50, 45);
        pinkTeacherLabel.setBounds(765, 257, 50, 45);
        blueTeacherLabel.setBounds(765, 325, 50, 45);
        //create and place a label containing player balance (only if the game is an advanced game)
        if (model.getGameMode() == GameMode.ADVANCED) {
            JLabel coinAmountLabel = new JLabel();
            String text;
            if (pb.getCoinBalance() > 1) {
                text = "available coins:" + pb.getCoinBalance();
            } else {
                text = "Available coin:" + pb.getCoinBalance();
            }
            coinAmountLabel.setText(text);
            coinAmountLabel.setOpaque(true);
            coinAmountLabel.setBackground(new Color(195, 193, 204));
            coinAmountLabel.setBounds(850, 10, 200, 25);
            coinAmountLabel.setFont(new Font("Monospaced", Font.BOLD, 17));
            playerBoardLabel.add(coinAmountLabel);
        }
        int count = 0;
        int secondCount = 0;
        for (int i = 0; i < this.player.getEntranceSize(); i++) {
            if (i % 2 == 0) {
                entranceStudentsButton.get(i).setBounds(90, 50 + 70 * count, 60, 45);
                count++;
            } else {
                entranceStudentsButton.get(i).setBounds(25, 123 + 70 * (secondCount), 60, 45);
                secondCount++;
            }
        }
        count = 0;
        secondCount = 0;
        for (int i = 0; i < towerStorage.getTowerCount(); i++) {
            if (i % 2 == 0) {
                towersLabels.get(i).setBounds(880, 50 + 70 * count, 50, 75);
                count++;
            } else {
                towersLabels.get(i).setBounds(965, 50 + 70 * secondCount, 50, 75);
                secondCount++;
            }
        }

        for (PawnColour p : diningRoomButtons.keySet()) {
            count = 0;
            for (int i = 0; i < this.player.getDiningRoomCount(p); i++) {
                switch (p) {
                    case RED -> diningRoomButtons.get(p).get(i).setBounds(200 + 50 * count, 120, 50, 45);
                    case GREEN -> diningRoomButtons.get(p).get(i).setBounds(200 + 50 * count, 50, 50, 45);
                    case YELLOW -> diningRoomButtons.get(p).get(i).setBounds(200 + 50 * count, 190, 50, 45);
                    case BLUE -> diningRoomButtons.get(p).get(i).setBounds(200 + 50 * count, 330, 50, 45);
                    case PINK -> diningRoomButtons.get(p).get(i).setBounds(200 + 50 * count, 260, 50, 45);
                }
                count++;
            }
        }

        assistantCardsScrollPane.setBounds(0, 430, 1080, 290);
        //add boardBackground's panel to window
        this.add(boardBackground);
        //add playerBoard's label to boardBackground's label
        boardBackground.add(playerBoardLabel);
        //add every assistant card to assistantCards' panel
        assistantCardsButtonsToShow.forEach(assistantCardsPanel::add);
        //add JScrollPane to boardBackground's label
        boardBackground.add(assistantCardsScrollPane);
        //add every entrance's student to PlayerBoard's label
        entranceStudentsButton.forEach(playerBoardLabel::add);
        //add every Tower's label to PlayerBoard's label
        towersLabels.forEach(playerBoardLabel::add);
        //add teachers' labels to PlayerBoard's label
        playerBoardLabel.add(redTeacherLabel);
        playerBoardLabel.add(blueTeacherLabel);
        playerBoardLabel.add(greenTeacherLabel);
        playerBoardLabel.add(yellowTeacherLabel);
        playerBoardLabel.add(pinkTeacherLabel);
        //add diningRoom's students' labels to PlayerBoard's label
        for (PawnColour p : diningRoomButtons.keySet()) {
            diningRoomButtons.get(p).forEach(playerBoardLabel::add);
        }
    }

    /**
     * Support method that, given an ArrayList of assistantCards, returns an arrayList containing the relative buttons
     * (example, given the assistantCard with priority 5, the method returns a JButton with the right assistantCard's image (image with number 5)
     *
     * @param assistantCardsLabels ArrayList containing assistantCard's JButton (containing assistantCards' images)
     * @param assistantCards       ArrayList containing AssistantCards of interest
     * @return an arrayList containing AssistantCards' JButtons
     */
    private ArrayList<JButton> GetCardsToShow(ArrayList<JButton> assistantCardsLabels, ArrayList<AssistantCard> assistantCards) {
        ArrayList<JButton> assistantsToShow = new ArrayList<>(assistantCards.size());
        for (AssistantCard assistantCard : assistantCards) {
            assistantsToShow.add(assistantCardsLabels.get(assistantCard.getPriority() - 1));
        }
        return assistantsToShow;
    }

    /**
     * get PlayerBoardNickname
     *
     * @return playerBoardPanel's playerBoard's nickname
     */
    public String getPlayerBoardNickname() {
        return this.player.getNickname();
    }

    /**
     * get PlayerBoard id
     *
     * @return playerBoardPanel's playerBoard's id
     */
    public int getPlayerBoardId() {
        return this.player.getId();
    }

    /**
     * Executes characterCards' effects that interact directly with the playerBoard
     *
     * @param cardIndex          card's priority
     * @param cardPositionInGame card's position inside the game (0 to 2)
     * @param fromCard           Optional list containing pawnColour picked from characterCard
     */
    public void PlayCharacterCardEffect(int cardIndex, int cardPositionInGame, SerializableOptional<ArrayList<PawnColour>> fromCard) {
        PlayCharacterCard playCharacterCard = null;
        PlayerActionRequest playerActionRequest = null;
        //create list that will contain chosen pawns from entrance
        List<PawnColour> pawnsFromEntrance = new ArrayList<>();
        //create checkboxes that will allow user to select pawns from entrance
        JCheckBox[] checkBoxes = new JCheckBox[player.getEntranceSize() - player.getEntranceSpaceLeft()];
        CheckBoxListener checkBoxListener;
        //initialize checkboxes' limit basing on characterCard that has been selected
        if (cardIndex == 7) {
            checkBoxListener = new CheckBoxListener(fromCard.get().size(), checkBoxes);
        } else {
            checkBoxListener = new CheckBoxListener(2, checkBoxes);
        }
        int countboxes = 0;
        //create JPanel that will be displayed by JoptionPane
        JPanel optionPanel = new JPanel();
        for (int j = 0; j < player.getEntranceSize(); j++) {
            //scan entrance and add a new CheckBox for every present pawn
            if (player.getEntranceStudents().get(j).isPresent()) {
                //create and add a new checkBox containing PawnColour's string
                checkBoxes[countboxes] = new JCheckBox(player.getEntranceStudents().get(j).get().toString());
                //add checkBoxListener to the new checkBox
                checkBoxes[countboxes].addItemListener(checkBoxListener);
                optionPanel.add(checkBoxes[countboxes]);
                countboxes++;
            }
        }
        //create and show JOptionPane
        int result = JOptionPane.showConfirmDialog(this, optionPanel,
                "Select pawns from entrance", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        //if user selected 'cancel' or closed JOptionPane
        if (result == -1 || result == 2) return;
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                //add selected checBoxes pawn to pawnsFromEntrance list
                pawnsFromEntrance.add(PawnColour.getPawnColourFromText(checkBox.getText()));
            }
        }
        switch (cardIndex) {
            case 7 -> {
                //list that will contain Pawns' pairs (from entrance and CharacterCard)
                ArrayList<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>(fromCard.get().size());
                for (int i = 0; i < pawnsFromEntrance.size(); i++) {
                    //create and add a new Pair
                    pairs.add(new Pair<>(pawnsFromEntrance.get(i), fromCard.get().get(i)));
                }
                //create a new PlayCharacterCard action and its playerActionRequest
                playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                        cardPositionInGame, SerializableOptional.empty(), SerializableOptional.empty(), SerializableOptional.of(pairs));
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case 10 -> {
                //Map containing playerBoard's diningRoom
                Map<PawnColour, Integer> diningRoomCount = player.getDiningRoom();
                optionPanel = new JPanel();
                optionPanel.setLayout(new FlowLayout());
                //list containing JSpinners
                ArrayList<JSpinner> jSpinners = new ArrayList<>(pawnsFromEntrance.size());
                for (PawnColour pawnColour : diningRoomCount.keySet()) {
                    //if diningRoom contains at least one pawn of that colour then create a JSpinner
                    if (diningRoomCount.get(pawnColour) > 0) {
                        optionPanel.add(new JLabel(pawnColour.toString()));
                        //create a new JSPinner that allows 0 as minimum, and as maximum the minimum between the number of pawns selected from entrance
                        // and the number of pawns of that color present in the diningroom
                        jSpinners.add(new JSpinner(new SpinnerNumberModel(0, 0, Math.min(pawnsFromEntrance.size(), diningRoomCount.get(pawnColour)), 1)));
                        //set JSpinnerName (useful to create pairs)
                        jSpinners.get(jSpinners.size() - 1).setName(pawnColour.toString());
                        //add Jspinner to JoptionPane's panel
                        optionPanel.add(jSpinners.get(jSpinners.size() - 1));
                    }
                }
                //create and show JOptionPane
                result = JOptionPane.showConfirmDialog(this, optionPanel,
                        "Select pawns from diningRoom", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == -1 || result == 2) return;
                //create list of pairs
                ArrayList<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>(pawnsFromEntrance.size());
                OuterLoop:
                for (JSpinner jSpinner : jSpinners) {
                    for (int i = 0; i < (Integer) jSpinner.getValue(); i++) {
                        //for every jSpinner create a new pair with firs element from entrance and the second one from diningRoom
                        pairs.add(new Pair<>(pawnsFromEntrance.get(pairs.size()), PawnColour.getPawnColourFromText(jSpinner.getName())));
                        if (pairs.size() == 2) break OuterLoop;
                    }
                }
                //create playCharacterCard action and its playerActionRequest
                playCharacterCard = new PlayCharacterCard(model.getMutableTurnOrder().getMutableCurrentPlayer().getId(),
                        cardPositionInGame, SerializableOptional.empty(), SerializableOptional.empty(), SerializableOptional.of(pairs));
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
        }
        //save action inside guiReader's history
        guiSocketListener.savePlayerActionRequest(playCharacterCard);
        //send playerActionRequest to Server
        try {
            socketWrapper.sendMessage(playerActionRequest);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


}
