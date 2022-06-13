package it.polimi.ingsw.Client.GUI.Panels;

import it.polimi.ingsw.Client.GUI.ActionType;
import it.polimi.ingsw.Client.GUI.Components.StudentButton;
import it.polimi.ingsw.Client.GUI.GUIReader;
import it.polimi.ingsw.Controller.Actions.MoveStudent;
import it.polimi.ingsw.Controller.Actions.PlayAssistantCard;
import it.polimi.ingsw.Controller.Enums.MoveDestination;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Model.*;
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
 * Panel class that contains one playerBoard and all not-used assistantCards
 */
public class PlayerBoardPanel extends JPanel {

    private final ArrayList<JButton> entranceStudentsButton;

    private final PlayerBoard player;

    //private boolean enableEntrance;

    private GUIReader guiReader;

    public PlayerBoardPanel(PlayerBoard pb, Model model, SocketWrapper socketWrapper, GUIReader guiReader) {
        this.player = pb;
        this.guiReader = guiReader;
        List<PawnColour> teachers = model.getOwnTeachers(this.player);
        TowerStorage towerStorage = model.getTeamMapper().getMutableTowerStorage(this.player);
        TurnOrder turnOrder = model.getMutableTurnOrder();
        ArrayList<JButton> assistantCardsLabels = new ArrayList<>(10);
        this.entranceStudentsButton = new ArrayList<>(this.player.getEntranceSize());
        ArrayList<JLabel> towersLabels = new ArrayList<>(towerStorage.getTowerCount());
        //Map that associates every pawnColour to an arrayList of JButton
        Map<PawnColour, ArrayList<JButton>> diningRoomButtons = new EnumMap<>(PawnColour.class);
        //Contains assistantCards' buttons
        JPanel assistantCardsPanel = new JPanel();
        assistantCardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
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
            entranceStudentsButton.get(i).addActionListener(e -> {
                String[] buttons = {"DiningRoom", "Island"};
                int returnValue = JOptionPane.showOptionDialog(null, "Where do you want to send this pawn?", "Destination ", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
                Container c = this.getParent();
                while (!(c instanceof JTabbedPane jTabbedPane)) {
                    c = c.getParent();
                }
                if (returnValue == 0) {
                    MoveStudent moveStudent = new MoveStudent(this.player.getId(), finalI, MoveDestination.toDiningRoom());
                    PlayerActionRequest playerAction = new PlayerActionRequest(moveStudent);
                    this.guiReader.savePlayerActionRequest(moveStudent);
                    try {
                        socketWrapper.sendMessage(playerAction);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (returnValue == 1) {
                    jTabbedPane.setSelectedIndex(0);
                    IslandFieldPanel islandFieldPanel = (IslandFieldPanel) jTabbedPane.getSelectedComponent();
                        /*for (int j = 1; j <= model.getMutablePlayerBoards().size(); j++) {
                            jTabbedPane.setSelectedIndex(j);
                            //PlayerBoardPanel playerBoardPanel = (PlayerBoardPanel) jTabbedPane.getSelectedComponent();
                            //playerBoardPanel.setDisabledEntrance(true);
                        }*/
                    jTabbedPane.setSelectedIndex(0);
                    islandFieldPanel.setActionType(ActionType.MOVESTUDENT, Optional.of(finalI));
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
            JButton assistantCardButton = new JButton(assistantCardsIcon.get(i));
            int finalI = i + 1;
            assistantCardButton.addActionListener(e -> {
                if (guiReader.getSuccessfulRequestsByType(PlayAssistantCard.class) == 0) {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(this, "Confirm to play assistant card with priority: " + finalI + "?", "PlayAssistant card confirmation", dialogButton);
                    if (dialogResult == 0) {
                        PlayAssistantCard playAssistantCard = new PlayAssistantCard(this.player.getId(), finalI);
                        PlayerActionRequest playerAction = new PlayerActionRequest(playAssistantCard);
                        this.guiReader.savePlayerActionRequest(playAssistantCard);
                        try {
                            socketWrapper.sendMessage(playerAction);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            });
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

    public PlayerBoard getPlayer() {
        return player;
    }


    /*protected void setDisabledEntrance(boolean enabled){
        this.enableEntrance = enabled;
    }*/


}
