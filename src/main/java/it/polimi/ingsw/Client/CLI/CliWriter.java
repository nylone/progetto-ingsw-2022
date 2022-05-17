package it.polimi.ingsw.Client.CLI;

import com.google.gson.Gson;
import it.polimi.ingsw.Controller.Actions.*;
import it.polimi.ingsw.Controller.Enums.MoveDestination;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Misc.SocketWrapper;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class takes Client's commands from terminal, provide support to the user and send the command to the Server by using the SocketWrapper
 */
public class CliWriter implements Runnable {


    private final SocketWrapper socketWrapper;
    /**
     * used to store the client's game data
     */
    private final ClientView clientView;

    private final BufferedReader stdIn;
    private final Gson gson;


    public CliWriter(SocketWrapper socketWrapper, ClientView clientView, BufferedReader bufferedReader) {
        this.socketWrapper = socketWrapper;
        this.clientView = clientView;
        this.gson = new Gson();
        this.stdIn = bufferedReader;
    }

    @Override
    public void run() {
        try {
            System.out.println(
                    """
                            ERIANTYS
                            Welcome Player
                            """
            );
            String nickname, password;
            while (true) {
                System.out.println("Insert Username:");
                nickname = stdIn.readLine();
                System.out.println("Insert Password");
                password = stdIn.readLine();
                if (nickname.equals("") || password.equals("")) {
                    System.out.println("Username or password not well formatted");
                } else {
                    DeclarePlayerRequest dp = new DeclarePlayerRequest(nickname, password);
                    socketWrapper.sendMessage(dp);
                    Thread.sleep(1000); //wait for the server response
                    if (this.clientView.isLogged()) {
                        break;
                    }
                }
            }
            this.clientView.setNickname(nickname);
            String input;
            while (true) {
                input = stdIn.readLine();
                elaborateInput(input);

            }
        } catch (IOException | InterruptedException | InvalidContainerIndexException e) {
            System.out.println("IO exception when reading from stdIn.");
            e.printStackTrace();
        }
    }

    /**
     * This method, given a String, executes the proper method according to User's request
     *
     * @param userInput: text containing the command
     * @throws IOException the integer read from command line
     */
    private void elaborateInput(String userInput) throws IOException, InvalidContainerIndexException {
        switch (userInput.toLowerCase()) {
            case "showactions" -> printActions();
            case "createlobby" -> createLobby();
            case "joinlobby" -> joinLobby();
            case "startgame" -> startGame();
            case "charactercardinfo" -> characterCardInfo();
            case "playassistantcard" -> {
                if (checkActionRequest()) return;
                playAssistantCard();
            }
            case "playcharactercard" -> {
                if (checkActionRequest()) return;
                playCharacterCard();
            }
            case "movestudent" -> {
                if (checkActionRequest()) return;
                moveStudent();
            }
            case "movemothernature" -> {
                if (checkActionRequest()) return;
                moveMotherNature();
            }
            case "choosecloud" -> {
                if (checkActionRequest()) return;
                chooseCloud();
            }
            case "endturn" -> {
                if (checkActionRequest()) return;
                endTurn();
            }
            default -> System.out.println("Command not valid");
        }
    }


    private void playCharacterCard() throws IOException {
        if (this.clientView.getGameBoard().getGameMode() == GameMode.SIMPLE) {
            System.out.println("Command valid only in Advanced GameMode");
            return;
        }

        int selected = getCharacterCardIndex();
        int finalSelected = selected;
        PlayerBoard currentPlayer = this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer();

        CharacterCard characterCard = this.clientView.getGameBoard().getCharacterCards().stream().filter(characterCard1 -> characterCard1.getId() == finalSelected).findFirst().get();
        for (int i = 0; i < this.clientView.getGameBoard().getCharacterCards().size(); i++) {
            if (this.clientView.getGameBoard().getCharacterCards().get(i).getId() == characterCard.getId()) {
                selected = i;
                break;
            }
        }
        System.out.println("BALANCE:" + currentPlayer.getCoinBalance());
        System.out.println("SELECTED:" + selected);
        System.out.println(characterCard.getClass());
        PlayerActionRequest playerActionRequest;
        switch (characterCard) {
            case Card01 card01 -> {
                PawnColour pawnToMove;
                System.out.println("Type student's position (from 0 to 3) to move");
                do {
                    selected = getInt();
                    if (selected < 0 || selected > 3) {
                        System.out.println("Index not valid, try again");
                    } else {
                        break;
                    }
                } while (true);
                pawnToMove = (PawnColour) card01.getState().get(selected);
                System.out.println("Type target island's id");
                Integer IslandId = getInt();
                PlayCharacterCard playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.of(IslandId), Optional.of(pawnToMove), Optional.empty());
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card02 ignored -> {
                PlayCharacterCard playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.empty(), Optional.empty());
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card03 ignored1 -> {
                System.out.println("Type target island's id");
                Integer IslandId = getInt();
                PlayCharacterCard playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.of(IslandId), Optional.empty(), Optional.empty());
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card04 ignored2 -> {
                PlayCharacterCard playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.empty(), Optional.empty());
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card05 ignored3 -> {
                System.out.println("Type target island's id");
                Integer IslandId = getInt();
                PlayCharacterCard playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.of(IslandId), Optional.empty(), Optional.empty());
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card06 ignored4 -> {
                PlayCharacterCard playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.empty(), Optional.empty());
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card07 card07 -> {
                System.out.println("You can now type up to 3 pairs to exchange, the first element coming from the entrance and the second one from the card" +
                        "Press 'enter' after a pair if you want to exchange less than 3 pairs");
                List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
                Optional<Integer> PawnPosition;
                int cardIndex;
                do {
                    System.out.println("Insert entrance's position containing the pawn to move, or 'enter' to conclude the choice");
                    do {
                        PawnPosition = getInt(stdIn.readLine());
                        if (PawnPosition.isEmpty()) break;
                        if (PawnPosition.get() < 0 || PawnPosition.get() >= currentPlayer.getEntranceStudents().size()) {
                            System.out.println("Target Entrance Position invalid");
                        } else {
                            break;
                        }
                    } while (true);
                    if (PawnPosition.isEmpty()) break;
                    System.out.println("Select pawn's index from card");
                    do {
                        cardIndex = getInt();
                        if (cardIndex < 0 || cardIndex >= card07.getState().size()) {
                            System.out.println("Target Card Position invalid");
                        } else {
                            break;
                        }
                    } while (true);
                    pairs.add(new Pair<>(currentPlayer.getEntranceStudents().get(PawnPosition.get()).get(), (PawnColour) card07.getState().get(cardIndex)));
                } while (pairs.size() < 3);
                Pair<PawnColour, PawnColour>[] pairsArray = new Pair[pairs.size()];
                PlayCharacterCard playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.empty(), Optional.of(pairsArray));
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card08 ignored5 -> {
                PlayCharacterCard playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.empty(), Optional.empty());
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card09 ignored6 -> {
                System.out.println("type the colour to make it irrelevant during this turn");
                playerActionRequest = getPawnColourFromInput(selected, currentPlayer);
            }
            case Card10 ignored7 -> {
                System.out.println("You can now type up to 3 pairs to exchange, the first element coming from the entrance and the second one from the dining room" +
                        "Press 'enter' after a pair if you want to exchange less than 3 pairs");
                List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
                Optional<Integer> PawnPosition;
                do {
                    System.out.println("Insert entrance's position containing the pawn to move, or 'enter' to conclude the choice");
                    do {
                        PawnPosition = getInt(stdIn.readLine());
                        if (PawnPosition.isEmpty()) break;
                        if (PawnPosition.get() < 0 || PawnPosition.get() >= currentPlayer.getEntranceStudents().size()) {
                            System.out.println("Target Entrance Position invalid");
                        } else {
                            break;
                        }
                    } while (true);
                    if (PawnPosition.isEmpty()) break;
                    System.out.println("Select diningRoom's colour");
                    boolean repeat = true;
                    do {
                        switch (stdIn.readLine().toLowerCase()) {
                            case "red" -> {
                                pairs.add(new Pair<>(currentPlayer.getEntranceStudents().get(PawnPosition.get()).get(), PawnColour.RED));
                                repeat = false;
                            }
                            case "yellow" -> {
                                pairs.add(new Pair<>(currentPlayer.getEntranceStudents().get(PawnPosition.get()).get(), PawnColour.YELLOW));
                                repeat = false;
                            }
                            case "green" -> {
                                pairs.add(new Pair<>(currentPlayer.getEntranceStudents().get(PawnPosition.get()).get(), PawnColour.GREEN));
                                repeat = false;
                            }
                            case "pink" -> {
                                pairs.add(new Pair<>(currentPlayer.getEntranceStudents().get(PawnPosition.get()).get(), PawnColour.PINK));
                                repeat = false;
                            }
                            case "blue" -> {
                                pairs.add(new Pair<>(currentPlayer.getEntranceStudents().get(PawnPosition.get()).get(), PawnColour.BLUE));
                                repeat = false;
                            }
                            case default -> System.out.println("Colour not valid, try again");
                        }
                    } while (repeat);
                } while (pairs.size() < 2);
                Pair<PawnColour, PawnColour>[] pairsArray = new Pair[pairs.size()];
                PlayCharacterCard playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.empty(), Optional.of(pairsArray));
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card11 card11 -> {
                System.out.println("Select pawn's index from card");
                int cardIndex;
                do {
                    cardIndex = getInt();
                    if (cardIndex < 0 || cardIndex >= card11.getState().size()) {
                        System.out.println("Target Card Position invalid");
                    } else {
                        break;
                    }
                } while (true);
                PlayCharacterCard playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.of((PawnColour) card11.getState().get(cardIndex)), Optional.empty());
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card12 ignored8 -> {
                System.out.println("Select Pawn's colour to remove from all Playerboards' diningrooms");
                playerActionRequest = getPawnColourFromInput(selected, currentPlayer);
            }
            case default -> {
                System.out.println("CharacterCard not valid");
                return;
            }
        }
        socketWrapper.sendMessage(playerActionRequest);
    }

    private void characterCardInfo() throws IOException {
        if (this.clientView.getGameBoard().getGameMode() == GameMode.SIMPLE) {
            System.out.println("Command valid only in Advanced GameMode");
            return;
        }
        int selected = getCharacterCardIndex();
        switch (selected) {
            case 1 -> System.out.println("EFFECT: Take 1 Student from this card and place it on " +
                    "an Island of your choice. Then, draw a new Student from the Bag and place it on this card.");
            case 2 -> System.out.println("EFFECT: During this turn, you take control of any\n" +
                    " number of Professors even if you have the same number of Students as the player who currently controls them.");
            case 3 -> System.out.println("EFFECT: Choose an Island and resolve the Island as if\n" +
                    " Mother Nature had ended her movement there. Mother\n" +
                    " Nature will still move and the Island where she ends her movement will also be resolved.");
            case 4 -> System.out.println("EFFECT: You may move Mother Nature up to 2\n" +
                    " additional Islands than is indicated by the Assistant card you've played.");
            case 5 -> System.out.println("""
                    EFFECT: Place a No Entrytile on an Island of your choice.
                     The first time Mother Nature ends her movement there, put the No Entry tile back onto this card
                     DO NOT calculate influence on that Island, or place any Towers.""");
            case 6 ->
                    System.out.println("EFFECT: When resolving a Conquering on an Island, Towers do not count towards influence.");
            case 7 ->
                    System.out.println("you may take up to 3 students from this card and replace them with the same number of Students\n" +
                            " from your Entrance");
            case 8 ->
                    System.out.println("EFFECT: During the influence calculation this turn, you count as having 2 more influence");
            case 9 ->
                    System.out.println("EFFECT: Choose a color of Student: during the influence calculation this turn, that color adds no influence");
            case 10 ->
                    System.out.println("EFFECT: You may exchange up to 2 Students between your entrance and your Dining Room");
            case 11 ->
                    System.out.println("EFFECT: Take 1 Student from this card and place it in your Dining Room. Then, draw a new Student from the\n" +
                            "Bag and place it on this card.");
            case 12 -> System.out.println("""
                    EFFECT: Choose a type of Student: every player (including yourself) must return 3 Students of that type
                     * from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as many Students as they have.
                    """);
        }
        System.out.println("You may now continue with the game, remember the 'showActions' command, it's your best friend during the game");
    }

    private boolean checkActionRequest() {
        if (!this.clientView.isInLobby()) {
            System.out.println("Action not valid, join or create a lobby");
            return true;
        }
        if (!this.clientView.getGameStarted()) {
            if (this.clientView.getAdmin().equals(this.clientView.getNickname())) {
                System.out.println("Action not valid, you need to start the game");
                return true;
            } else {
                System.out.println("Action not valid, you need to wait for the admin to start the game");
                return true;
            }
        }
        return false;
    }

    private void printActions() {
        if (!this.clientView.isInLobby()) {
            System.out.println("Available commands:\n");
            System.out.println("-- createLobby (create a open or private lobby)");
            System.out.println("-- joinLobby (join a lobby with the UUID)");
            return;
        }
        if (!this.clientView.getGameStarted()) {
            if (this.clientView.getNickname().equals(this.clientView.getAdmin())) {
                System.out.println("Available commands:\n");
                System.out.println("-- startGame (start the game)");
            } else {
                System.out.println("No actions available, wait for the admin to start the game");
            }
            return;
        }
        printGameActions();
    }

    /**
     * Executes the createLobby command,
     *
     * @throws IOException if an I/O error occurs
     */
    private void createLobby() throws IOException {
        if (!clientView.isInLobby()) {
            CreateLobbyRequest createLobbyRequest;
            System.out.println("Do you want to create an open or private lobby?");
            System.out.println("O : open");
            System.out.println("P : private");
            boolean isPublic;
            String input;
            loop:
            while (true) {
                input = stdIn.readLine().toUpperCase();
                switch (input) {
                    case "O" -> {
                        isPublic = true;
                        break loop;
                    }
                    case "P" -> {
                        isPublic = false;
                        break loop;
                    }
                    case default -> System.out.println("input not correct, please try again");
                }
            }
            System.out.println("how many player will the lobby contain?");
            int players;
            while (true) {
                players = Integer.parseInt(stdIn.readLine());
                if (players >= 2 && players <= 4) {
                    break;
                }
                System.out.println("Amount of players not valid");
            }
            createLobbyRequest = new CreateLobbyRequest(isPublic, players);
            socketWrapper.sendMessage(createLobbyRequest);
        } else {
            System.out.println("You are already in a lobby");
        }
    }

    /**
     * Executes the joinLobby command by knowing lobby's UUID
     *
     * @throws IOException if an I/O error occurs
     */
    private void joinLobby() throws IOException {
        if (!clientView.isInLobby()) {
            ConnectLobbyRequest connectLobbyRequest;
            System.out.println("Insert lobby's UUID");
            UUID id = null;
            do {
                try {
                    id = UUID.fromString(stdIn.readLine());
                }catch (IllegalArgumentException e){
                    System.out.println("UUID not valid, try again");
                }
                break;
            }while (true);
            connectLobbyRequest = new ConnectLobbyRequest(id);
            socketWrapper.sendMessage(connectLobbyRequest);

        } else {
            System.out.println("You are already in a lobby");
        }
    }

    /**
     * Executes the startGame command, it can be executed only by lobby's admin and whether the lobby is full
     *
     * @throws IOException if an I/O error occurs
     */
    private void startGame() throws IOException {
        System.out.println("Select the game mode:");
        System.out.println("S: simple");
        System.out.println("A: advanced");
        GameMode gameMode;
        String input;
        StartGameRequest startGameRequest;
        loop:
        while (true) {
            input = stdIn.readLine().toUpperCase();
            switch (input) {
                case "S" -> {
                    gameMode = GameMode.SIMPLE;
                    break loop;
                }
                case "A" -> {
                    gameMode = GameMode.ADVANCED;
                    break loop;
                }
                case default -> System.out.println("input not correct, please try again");
            }
        }
        startGameRequest = new StartGameRequest(gameMode);
        socketWrapper.sendMessage(startGameRequest);
    }

    /**
     * Executes the playAssistantCard command
     * The method shows the available assistantCard at that moment
     *
     * @throws IOException if an I/O error occurs
     */
    private void playAssistantCard() throws IOException {
        System.out.println("select one of these available assistant card");
        //get the unused cards
        ArrayList<AssistantCard> availableAssistants = this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getMutableAssistantCards()
                .stream().filter(assistantCard -> !assistantCard.getUsed())
                .collect(Collectors.toCollection(ArrayList::new));
        //from the unused cards, extract the card with a priority not selected before by other players
        for (PlayerBoard pb : this.clientView.getGameBoard().getMutableTurnOrder().getCurrentTurnOrder()) {
            if (this.clientView.getGameBoard().getMutableTurnOrder().getMutableSelectedCard(pb).isPresent()) {
                availableAssistants.removeIf(assistantCard -> assistantCard.getPriority() == this.clientView.getGameBoard().getMutableTurnOrder().getMutableSelectedCard(pb).get().getPriority());
            }
        }
        //get the priority of the available cards
        ArrayList<Integer> cardNumbers = availableAssistants.stream().map(AssistantCard::getPriority).collect(Collectors.toCollection(ArrayList::new));
        availableAssistants.forEach(assistantCard -> System.out.println("priority:" + assistantCard.getPriority() + " maxMovement:" + assistantCard.getMaxMovement()));
        //get the selected card
        int selected;
        do {
            selected = getInt();
            if (!cardNumbers.contains(selected)) {
                System.out.println("Card not available");
            } else {
                break;
            }
        } while (true);
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), selected);
        PlayerActionRequest playerAction = new PlayerActionRequest(playAssistantCard);
        socketWrapper.sendMessage(playerAction);
    }

    /**
     * Executes the moveStudent command
     *
     * @throws IOException if an I/O error occurs
     */
    private void moveStudent() throws IOException {
        int entranceSize = this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getEntranceSize() - 1;
        System.out.println("Insert the number of entrance's position between 0 and " + entranceSize);
        int selected = getInt();

        System.out.println("Type the island id to move the selected student there or press 'Enter' to send it to the dining room");
        Optional<Integer> choice = getInt(stdIn.readLine());
        MoveStudent moveStudent;
        if (choice.isEmpty()) {
            moveStudent = new MoveStudent(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), selected, MoveDestination.toDiningRoom());
        } else {
            moveStudent = new MoveStudent(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), selected, MoveDestination.toIsland(choice.get()));
        }

        PlayerActionRequest clientPlayerAction = new PlayerActionRequest(moveStudent);
        socketWrapper.sendMessage(clientPlayerAction);

    }

    /**
     * Executes the moveMotherNature command
     *
     * @throws IOException if an I/O error occurs
     */
    private void moveMotherNature() throws IOException {
        System.out.println("How many steps do you want mother nature to take?");
        int steps = getInt();
        MoveMotherNature moveMotherNature = new MoveMotherNature(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), steps);
        PlayerActionRequest clientPlayerAction = new PlayerActionRequest(moveMotherNature);
        socketWrapper.sendMessage(clientPlayerAction);
    }

    /**
     * Executes the chooseCloud command
     *
     * @throws IOException if an I/O error occurs
     */
    private void chooseCloud() throws IOException {
        System.out.println("Select one cloud from 0 to " + (this.clientView.getGameBoard().getClouds().size() - 1));
        int selectedCloud = getInt();

        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), selectedCloud);
        PlayerActionRequest clientPlayerAction = new PlayerActionRequest(chooseCloudTile);
        socketWrapper.sendMessage(clientPlayerAction);
    }

    /**
     * Executes the endTurn command
     */
    private void endTurn() throws IOException {
        EndTurnOfActionPhase endTurnOfActionPhase = new EndTurnOfActionPhase(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId());
        PlayerActionRequest playerActionRequest = new PlayerActionRequest(endTurnOfActionPhase);
        socketWrapper.sendMessage(playerActionRequest);
    }

    /**
     * print all the available actions basing on current game phase
     */
    private void printGameActions() {
        if (!this.clientView.getNickname().equals(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getNickname())) {
            System.out.println("No actions are allowed out of turn");
            return;
        }
        GamePhase gamePhase = this.clientView.getGameBoard().getMutableTurnOrder().getGamePhase();
        System.out.println("during the " + gamePhase + " phase these are the available commands:");
        switch (gamePhase) {
            case SETUP ->
                    System.out.println("--playAssistantCard (play the assistant card to establish the turn order)");
            case ACTION -> {
                System.out.println("--moveStudent (move one student from entrance to dining room or one island)");
                if (this.clientView.getGameBoard().getGameMode() == GameMode.ADVANCED) {
                    System.out.println("--playCharacterCard (activate the powerful effect of the character card)");
                }
                System.out.println("--moveMotherNature (move mother nature and calculate the influence");
                System.out.println("--chooseCloud (fill your entrance after moving three students)");
                if (this.clientView.getGameBoard().getGameMode() == GameMode.ADVANCED)
                    System.out.println("--characterCardInfo (show the information about one characterCard)");
                System.out.println("--endTurn (end your turn)");
            }
            default -> System.out.println("Gamephase is not valid");
        }
    }

    private int getCharacterCardIndex() throws IOException {
        System.out.println("Pick one character card from the list above (type the card number)");
        int selected;
        ArrayList<Integer> characterCardsNumbers = this.clientView.getGameBoard().getCharacterCards().stream().map(CharacterCard::getId).collect(Collectors.toCollection(ArrayList::new));
        do {
            selected = getInt();
            if (!characterCardsNumbers.contains(selected)) {
                System.out.println("CharacterCard not available");
            } else {
                break;
            }
        } while (true);
        return selected;
    }

    /**
     * Support method to read an integer from command line
     *
     * @return the integer read from command line
     * @throws IOException if an I/O error occurs
     */
    private int getInt() throws IOException {
        int result;
        while (true) {
            try {
                String text = stdIn.readLine();
                if (text == null)
                    throw new IOException();
                result = Integer.parseInt(text);
                break;
            } catch (NumberFormatException ex) {
                System.out.println("This string is not a number, retry.");
            }
        }
        return result;
    }

    /**
     * @param input: text to convert in integer
     * @return the integer read from command line or Empty if the user pressed "Enter"
     * @throws IOException if an I/O error occurs
     */
    private Optional<Integer> getInt(String input) throws IOException {
        int result;
        while (true) {
            try {
                if (input == null)
                    throw new IOException();
                if (input.equals(""))
                    return Optional.empty();
                result = Integer.parseInt(input);
                break;
            } catch (NumberFormatException ex) {
                System.out.println("This string is not a number, retry.");
            }
            input = stdIn.readLine();
        }
        return Optional.of(result);
    }

    private PlayerActionRequest getPawnColourFromInput(int selected, PlayerBoard currentPlayer) throws IOException {
        PlayerActionRequest playerActionRequest;
        PlayCharacterCard playCharacterCard = null;
        boolean repeat = true;
        do {
            switch (stdIn.readLine().toLowerCase()) {
                case "red" -> {
                    playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.of(PawnColour.RED), Optional.empty());
                    repeat = false;
                }
                case "yellow" -> {
                    playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.of(PawnColour.YELLOW), Optional.empty());
                    repeat = false;
                }
                case "green" -> {
                    playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.of(PawnColour.GREEN), Optional.empty());
                    repeat = false;
                }
                case "pink" -> {
                    playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.of(PawnColour.PINK), Optional.empty());
                    repeat = false;
                }
                case "blue" -> {
                    playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.of(PawnColour.BLUE), Optional.empty());
                    repeat = false;
                }
                case default -> System.out.println("Colour not valid, try again");
            }
        } while (repeat);
        playerActionRequest = new PlayerActionRequest(playCharacterCard);
        return playerActionRequest;
    }
}
