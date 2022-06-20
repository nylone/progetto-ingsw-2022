package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Controller.Actions.*;
import it.polimi.ingsw.Controller.Enums.MoveDestination;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Misc.Optional;
import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.Events.Requests.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class takes Client's commands from terminal, provides support to the user and sends the command to the Server by using the SocketWrapper
 */
public class CliWriter implements Runnable {

    /**
     * socket wrapper to connect the Client to the Server
     */
    private final SocketWrapper socketWrapper;
    /**
     * used to store the client's game data
     */
    private final ClientView clientView;
    /**
     * used to acquire text from command line
     */
    private final BufferedReader stdIn;
    /**
     * used to synchronize CliWriter and CliReader, useful when the first one needs to wait the second one
     */
    CyclicBarrier cyclicBarrier;


    public CliWriter(SocketWrapper socketWrapper, ClientView clientView, BufferedReader bufferedReader, CyclicBarrier cyclicBarrier) {
        this.socketWrapper = socketWrapper;
        this.clientView = clientView;
        this.stdIn = bufferedReader;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            //wait for the CliReader to receive the confirmation of successful connection to the server
            cyclicBarrier.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        //exit the Thread whether the client was not able to connect to the server
        if (!this.clientView.isConnected()) {
            return;
        }
        try {
            System.out.println(
                    """
                            ERIANTYS
                            Welcome Player
                            """
            );
            String nickname;
            while (true) {
                System.out.println("Insert Username:");
                //acquire Username from stdIn
                nickname = stdIn.readLine();
                if (!this.clientView.isConnected()) return;
                //whether at least one between nickname and password is empty then repeat the login process
                if (nickname.equals("")) {
                    System.out.println("Username not well formatted");
                } else {
                    if (!this.clientView.isConnected()) return;
                    //create and initialize the DeclarePlayer request
                    DeclarePlayerRequest dp = new DeclarePlayerRequest(nickname);
                    //send the request to the server
                    socketWrapper.sendMessage(dp);
                    //wait for the CliReader to receive the confirmation of successful login
                    cyclicBarrier.await();
                    //check if this Client is connected
                    if (this.clientView.isLogged()) {
                        break;
                    }
                }
                //repeat until user has typed Username and password correctly (syntactically) or typed password is wrong
            }
            //set nickname in clientView object
            this.clientView.setNickname(nickname);
            String input;
            if (!this.clientView.isConnected()) Thread.currentThread().interrupt();
            while (true) {
                //acquire command from stdIn
                input = stdIn.readLine();
                if (!this.clientView.isConnected()) return;
                //execute command
                elaborateInput(input);
            }
        } catch (IOException | InvalidContainerIndexException | BrokenBarrierException e) {
            System.out.println("IO exception when reading from stdIn.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
            case "charactercardinfo" -> {
                if (checkActionRequest()) return;
                characterCardInfo();
            }
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

    /**
     * This method, after checking the gameMode, takes CharacterCard's number and extracts the characteCard; basing on CharacterCard type
     * the methods create dynamically the CharacterCardInput by requesting the user only the right input.
     *
     * @throws IOException input has gone wrong
     */
    private void playCharacterCard() throws IOException {
        //check for GameMode's validity
        if (this.clientView.getGameBoard().getGameMode() == GameMode.SIMPLE) {
            System.out.println("Command valid only in Advanced GameMode");
            return;
        }
        //Get selected characterCard's index
        int selected = getCharacterCardIndex();
        int finalSelected = selected;
        //get current player
        PlayerBoard currentPlayer = this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer();
        //extract CharacterCard from GameBoard
        CharacterCard characterCard = this.clientView.getGameBoard().getCharacterCards().stream().filter(characterCard1 -> characterCard1.getId() == finalSelected).findFirst().get();
        //get selected characterCard's index from gameBoard
        selected = IntStream.range(0, 3).filter(i -> this.clientView.getGameBoard().getCharacterCards().get(i).getId() == characterCard.getId()).findFirst().orElse(selected);
        PlayCharacterCard playCharacterCard;
        PlayerActionRequest playerActionRequest;
        //pattern matching switch responsible for redirecting user to right input creation
        switch (characterCard) {
            case Card01 card01 -> {
                PawnColour pawnToMove;
                System.out.println("Type student's position (from 0 to 3) to move");
                //Get selected pawn's index from 0 to 3 (Card01 contains 4 students)
                while (true) {
                    selected = getInt();
                    if (selected < 0 || selected > 3) {
                        System.out.println("Index not valid, try again");
                    } else {
                        break;
                    }
                }
                //get selected pawn having the index
                pawnToMove = (PawnColour) card01.getState().get(selected);
                System.out.println("Type target island's id");
                //get Island's ID
                Integer IslandId = getInt();
                //create playCharacterCard's controller action
                playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.of(IslandId), Optional.of(pawnToMove), Optional.empty());
                //create playerActionRequest message to send to the server
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card02 ignored -> {
                //create playCharacterCard's controller action
                playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.empty(), Optional.empty());
                //create playerActionRequest message to send to the server
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card03 ignored1 -> {
                System.out.println("Type target island's id");
                //get Island's ID
                Integer IslandId = getInt();
                //create playCharacterCard's controller action
                playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.of(IslandId), Optional.empty(), Optional.empty());
                //create playerActionRequest message to send to the server
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card04 ignored2 -> {
                //create playCharacterCard's controller action
                playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.empty(), Optional.empty());
                //create playerActionRequest message to send to the server
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card05 ignored3 -> {
                System.out.println("Type target island's id");
                //get Island's ID
                Integer IslandId = getInt();
                //create playCharacterCard's controller action
                playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.of(IslandId), Optional.empty(), Optional.empty());
                //create playerActionRequest message to send to the server
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card06 ignored4 -> {
                //create playCharacterCard's controller action
                playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.empty(), Optional.empty());
                //create playerActionRequest message to send to the server
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card07 card07 -> {
                System.out.println("You can now type up to 3 pairs to exchange, the first element coming from the entrance and the second one from the card\n" +
                        "Press 'enter' after a pair if you want to exchange less than 3 pairs");
                //create List of Pairs which will contain up to 3 pairs of pawns
                List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
                //index containing pawn's index in currentPlayers' entrance
                Optional<Integer> PawnPosition;
                //index containing pawn's index inside the card
                int cardIndex;
                outerWhile:
                do {
                    System.out.println("Insert entrance's position containing the pawn to move, or 'enter' to conclude the choice");
                    do {
                        //get pawn's index (that one coming from entrance)
                        PawnPosition = getOptionalInt();
                        //if user pressed 'enter' the input will interrupt (the user can choose less than 3 pairs)
                        if (PawnPosition.isEmpty()) break outerWhile;
                        //check whether user typed an invalid index
                        if (PawnPosition.get() < 0 || PawnPosition.get() >= currentPlayer.getEntranceStudents().size())
                            System.out.println("Target Entrance Position invalid");
                        else break;
                    } while (true);
                    System.out.println("Select pawn's index from card");
                    do {
                        //get pawn's index (that one coming from the card)
                        cardIndex = getInt();
                        //check whether user typed an invalid index
                        if (cardIndex < 0 || cardIndex >= card07.getState().size())
                            System.out.println("Target Card Position invalid");
                        else {
                            break;
                        }
                    } while (true);
                    //add to the list containing the pairs the one just chosen by the user
                    pairs.add(new Pair<>(currentPlayer.getEntranceStudents().get(PawnPosition.get()).get(), (PawnColour) card07.getState().get(cardIndex)));
                    //repeat the loop until the user has selected 3 pairs or pressed 'enter'
                } while (pairs.size() < 3);
                //create playCharacterCard's controller action
                playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.empty(), Optional.of(pairs));
                //create playerActionRequest message to send to the server
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card08 ignored5 -> {
                playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.empty(), Optional.empty());
                //create playerActionRequest message to send to the server
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card09 ignored6 -> {
                System.out.println("type the colour to make it irrelevant during this turn");
                //create playerActionRequest message to send to the server
                playerActionRequest = createPlayerActionByPawnColour(selected, currentPlayer);
            }
            case Card10 ignored7 -> {
                System.out.println("You can now type up to 2 pairs to exchange, the first element coming from the entrance and the second one from the dining room\n" +
                        "Press 'enter' after a pair if you want to exchange less than 2 pairs");
                //create List of Pairs which will contain up to 2 pairs of pawns
                List<Pair<PawnColour, PawnColour>> pairs = new ArrayList<>();
                //index containing pawn's index in currentPlayers' entrance
                Optional<Integer> PawnPosition;
                outerWhile:
                do {
                    System.out.println("Insert entrance's position containing the pawn to move, or 'enter' to conclude the choice");
                    do {
                        //get pawn's index (that one coming from entrance)
                        PawnPosition = getOptionalInt();
                        //if user pressed 'enter' the input will interrupt (the user can choose less than 3 pairs)
                        if (PawnPosition.isEmpty()) break outerWhile;
                        //check whether user typed an invalid index
                        if (PawnPosition.get() < 0 || PawnPosition.get() >= currentPlayer.getEntranceStudents().size())
                            System.out.println("Target Entrance Position invalid");
                        else {
                            break;
                        }
                    } while (true);
                    System.out.println("Select diningRoom's colour");
                    boolean repeat = true;
                    //get the selected PawnColour from stdIn and add the pair to the list
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
                        //repeat whether the user typed an invalid PawnColour
                    } while (repeat);
                    //repeat the loop until the user has selected 2 pairs or pressed 'enter'
                } while (pairs.size() < 2);
                //create playCharacterCard's controller action
                playCharacterCard = new PlayCharacterCard(currentPlayer.getId(),
                        selected,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.of(pairs));
                //create playerActionRequest message to send to the server
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card11 card11 -> {
                System.out.println("Select pawn's index from card");
                int cardIndex;
                do {
                    //get selected pawn's index from card
                    cardIndex = getInt();
                    //check whether user typed an invalid index
                    if (cardIndex < 0 || cardIndex >= card11.getState().size())
                        System.out.println("Target Card Position invalid");
                    else {
                        break;
                    }
                } while (true);
                //create playCharacterCard's controller action
                playCharacterCard = new PlayCharacterCard(currentPlayer.getId(), selected, Optional.empty(), Optional.of((PawnColour) card11.getState().get(cardIndex)), Optional.empty());
                //create playerActionRequest message to send to the server
                playerActionRequest = new PlayerActionRequest(playCharacterCard);
            }
            case Card12 ignored8 -> {
                System.out.println("Select Pawn's colour to remove from all Playerboards' diningrooms");
                //create playerActionRequest message to send to the server
                playerActionRequest = createPlayerActionByPawnColour(selected, currentPlayer);
            }
            case default -> {
                System.out.println("CharacterCard not valid");
                return;
            }
        }
        //send playerActionRequest to the server
        socketWrapper.sendMessage(playerActionRequest);
    }

    /**
     * This method prints selected characterCard to the command line to explain card's effect to the player
     *
     * @throws IOException input has gone wrong
     */
    private void characterCardInfo() throws IOException {
        //check for GameMode's validity
        if (this.clientView.getGameBoard().getGameMode() == GameMode.SIMPLE) {
            System.out.println("Command valid only in Advanced GameMode");
            return;
        }
        //get selected characterCard's index
        int selected = getCharacterCardIndex();
        //every switch's case print CharacterCard's info (every information is available on game's guide)
        switch (selected) {
            case 1 -> System.out.println("EFFECT: Take 1 Student from this card and place it on " +
                    "an Island of your choice. Then, draw a new Student from the Bag and place it on this card.");
            case 2 -> System.out.println("EFFECT: During this turn, you take control of any\n" +
                    " number of Professors even if you have the same number of Students as the player who currently controls them.");
            case 3 -> System.out.println("""
                    EFFECT: Choose an Island and resolve the Island as if
                     Mother Nature had ended her movement there. Mother
                     Nature will still move and the Island where she ends her movement will also be resolved.""");
            case 4 -> System.out.println("EFFECT: You may move Mother Nature up to 2\n" +
                    " additional Islands than is indicated by the Assistant card you've played.");
            case 5 -> System.out.println("""
                    EFFECT: Place a No Entrytile on an Island of your choice.
                     The first time Mother Nature ends her movement there, put the No Entry tile back onto this card
                     DO NOT calculate influence on that Island, or place any Towers.""");
            case 6 ->
                    System.out.println("EFFECT: When resolving a Conquering on an Island, Towers do not count towards influence.");
            case 7 ->
                    System.out.println("EFFECT: you may take up to 3 students from this card and replace them with the same number of Students\n" +
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
                     from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as many Students as they have.
                    """);
        }
        System.out.println("You may now continue with the game, remember the 'showActions' command, it's your best friend during the game");
    }

    /**
     * Support method responsible for checking whether the user has the rights to perform the requested action.
     * This method will only be executed to check actions available only when the match has started
     *
     * @return true if the check fails
     */
    private boolean checkActionRequest() {
        //check if the user is present inside a lobby
        if (!this.clientView.isInLobby()) {
            System.out.println("Action not valid, join or create a lobby");
            return true;
        }
        //check if the game has started
        if (!this.clientView.getGameStarted()) {
            if (this.clientView.getAdmin().equals(this.clientView.getNickname())) {
                System.out.println("Action not valid, you need to start the game");
            } else {
                System.out.println("Action not valid, you need to wait for the admin to start the game");
            }
            return true;
        }
        return false;
    }

    /**
     * Method responsible for print actions during all the game
     */
    private void printActions() {
        //actions available outside a lobby
        if (!this.clientView.isInLobby()) {
            System.out.println("Available commands:\n");
            System.out.println("-- createLobby (create a open or private lobby)");
            System.out.println("-- joinLobby (join a lobby with the UUID)");
            return;
        }
        if (!this.clientView.getGameStarted()) {
            //actions available when the game has not started
            if (this.clientView.getNickname().equals(this.clientView.getAdmin())) {
                //if the user is the admin
                System.out.println("Available commands:\n");
                System.out.println("-- startGame (start the game)");
            } else {
                //if the user is not the admin
                System.out.println("No actions available, wait for the admin to start the game");
            }
            return;
        }
        //print action-phase commands
        printGameActions();
    }

    /**
     * Executes the createLobby command; during the creation the user is able to choose lobby's visibility (private or public) and
     * lobby's dimension (this dimension will also be game's number of players).
     * <p>
     * Finally, it creates the createLobby request and then sends it to the server.
     *
     * @throws IOException if an I/O error occurs
     */
    private void createLobby() throws IOException {
        //check if the user is already in a lobby
        if (!clientView.isInLobby()) {
            //create CreateLobbyRequest object
            CreateLobbyRequest createLobbyRequest;
            //print visibility request
            System.out.println("Do you want to create an open or private lobby?");
            System.out.println("O : open");
            System.out.println("P : private");
            boolean isPublic;
            String input;
            loop:
            while (true) {
                //get visibility choice
                input = stdIn.readLine().toUpperCase();
                //assign user's choice to boolean variable 'isPublic'
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
            //print lobby's dimension request
            System.out.println("how many player will the lobby contain?");
            int players;
            while (true) {
                //get lobby's dimension from stdIn
                players = getInt();
                //check input's validity (lobby's dimension must be between 2 and 4)
                if (players >= 2 && players <= 4) {
                    break;
                }
                System.out.println("Amount of players not valid");
            }
            //initialize createLobbyRequest object to send to the server
            createLobbyRequest = new CreateLobbyRequest(isPublic, players);
            //send request to the server
            socketWrapper.sendMessage(createLobbyRequest);
        } else {
            System.out.println("You are already in a lobby");
        }
    }

    /**
     * Executes the joinLobby command by knowing lobby's UUID, it allows to access both open and private lobbies.
     * <p>
     * Finally, it creates the joinLobby request and then sends it to the Server.
     *
     * @throws IOException if an I/O error occurs
     */
    private void joinLobby() throws IOException {
        //check if the user is already in a lobby
        if (!clientView.isInLobby()) {
            //create ConnectLobbyRequest object
            ConnectLobbyRequest connectLobbyRequest;
            System.out.println("Insert lobby's UUID");
            UUID id = null;
            boolean repeat = false;
            do {
                try {
                    //get UUID from stdIn
                    id = UUID.fromString(stdIn.readLine());
                } catch (IllegalArgumentException e) {
                    System.out.println("UUID not valid, try again");
                    repeat = true;
                }
                //repeat until the user types a valid UUID (syntactically, the server will check if the UUID is associated with a valid lobby)
            } while (repeat);
            //initialize connectLobbyRequest object to send to the server
            connectLobbyRequest = new ConnectLobbyRequest(id);
            //send message to the server
            socketWrapper.sendMessage(connectLobbyRequest);

        } else {
            System.out.println("You are already in a lobby");
        }
    }

    /**
     * Executes the startGame command, it can be executed only by lobby's admin and whether the lobby is full.
     * The admin can choose the game mode between simple and advanced.
     * <p>
     * Finally, it creates the startGameRequest and then sends it to the Server.
     *
     * @throws IOException if an I/O error occurs
     */
    private void startGame() throws IOException {
        if (this.clientView.isInLobby()) {
            ////print gameMode request
            System.out.println("Select the game mode:");
            System.out.println("S: simple");
            System.out.println("A: advanced");
            //create GameMode object
            GameMode gameMode;
            String input;
            //create startGameRequest object
            StartGameRequest startGameRequest;
            loop:
            //repeat until the user types a valid gamemode
            while (true) {
                ////get gameMode  from stdIn
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
            //initialize startGameRequest object
            startGameRequest = new StartGameRequest(gameMode);
            //send message to the server
            socketWrapper.sendMessage(startGameRequest);
        } else {
            System.out.println("You first need to join or create a lobby");
        }
    }


    /**
     * Executes the playAssistantCard command
     * The method shows the available assistantCard at that moment (removing the used cards and cards picked by other players in the same turn to avoid
     * playing cards with same value).
     * <p>
     * Finally, it creates the playAssistantCard request and then sends it to the Server.
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
            //repeat until the user pic
        } while (true);
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), selected);
        PlayerActionRequest playerAction = new PlayerActionRequest(playAssistantCard);
        socketWrapper.sendMessage(playerAction);
    }

    /**
     * Executes the moveStudent command, the method requires from the user to type pawn's index inside entrance then it
     * allows the user to choose the destination:
     * type enter to move the pawn directly to the dining room
     * type a number that will be interpreted as the island's id on which to move the pawn
     * <p>
     * Finally, it creates the moveStudent request and then sends it to the Server.
     *
     * @throws IOException if an I/O error occurs
     */
    private void moveStudent() throws IOException {
        //get entrance size
        int entranceSize = this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getEntranceSize() - 1;
        System.out.println("Insert the number of entrance's position between 0 and " + entranceSize);
        //get selected pawn's index from entrance
        int selected = getInt();

        System.out.println("Type the island id to move the selected student there or press 'Enter' to send it to the dining room");
        //get destination ('enter' for dining room or island's ID)
        Optional<Integer> choice = getOptionalInt();
        //create MoveStudent object
        MoveStudent moveStudent;
        if (choice.isEmpty()) {
            //if the user has typed 'enter' then initialize the moveStudent action with dining room as MoveDestination
            moveStudent = new MoveStudent(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), selected, MoveDestination.toDiningRoom());
        } else {
            //if the user has typed a number then initialize the moveStudent with that number
            moveStudent = new MoveStudent(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), selected, MoveDestination.toIsland(choice.get()));
        }
        //create and initialize clientPlayerAction to send to the Server
        PlayerActionRequest clientPlayerAction = new PlayerActionRequest(moveStudent);
        //send the action request to the Server
        socketWrapper.sendMessage(clientPlayerAction);

    }

    /**
     * Executes the moveMotherNature command, this method asks the user to type the amount of steps motherNature should take
     * <p>
     * Finally, it creates the moveMotherNature request and then sends it to the Server.
     *
     * @throws IOException if an I/O error occurs
     */
    private void moveMotherNature() throws IOException {
        System.out.println("How many steps do you want mother nature to take?");
        //get the amount of steps to perform
        int steps = getInt();
        //create and initialize moveMotherNature action
        MoveMotherNature moveMotherNature = new MoveMotherNature(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), steps);
        //create and initialize PlayerActionRequest to send to the server
        PlayerActionRequest clientPlayerAction = new PlayerActionRequest(moveMotherNature);
        //send action request to the server
        socketWrapper.sendMessage(clientPlayerAction);
    }

    /**
     * Executes the chooseCloud command, this method asks the user to type a number corresponding to Island's id.
     * Finally, it creates the chooseCloudTile request and then sends it to the Server.
     *
     * @throws IOException if an I/O error occurs
     */
    private void chooseCloud() throws IOException {
        System.out.println("Select one cloud from 0 to " + (this.clientView.getGameBoard().getClouds().size() - 1));
        //get selected Cloud's ID
        int selectedCloud = getInt();
        //create and initialize ChooseCloudTile Object with the selected Island's ID
        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), selectedCloud);
        //create and initialize clientPlayerAction request to send to the Server
        PlayerActionRequest clientPlayerAction = new PlayerActionRequest(chooseCloudTile);
        //send clientPlayerAction request to the Server
        socketWrapper.sendMessage(clientPlayerAction);
    }

    /**
     * Executes the endTurn command to end currentPlayer's action turn and move on to the next player
     */
    private void endTurn() throws IOException {
        //create and initialize endTurnOfActionPhase action
        EndTurnOfActionPhase endTurnOfActionPhase = new EndTurnOfActionPhase(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId());
        //create and initialize PlayerActionRequest to send to the server
        PlayerActionRequest clientPlayerAction = new PlayerActionRequest(endTurnOfActionPhase);
        //send action to the server
        socketWrapper.sendMessage(clientPlayerAction);
    }

    /**
     * print all the available actions basing on current game phase
     */
    private void printGameActions() {
        //check whether the user is allowed to perform any action (i.e. if is the current player)
        if (!this.clientView.getNickname().equals(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getNickname())) {
            System.out.println("No actions are allowed out of turn");
            return;
        }
        //get current gamePhase
        GamePhase gamePhase = this.clientView.getGameBoard().getMutableTurnOrder().getGamePhase();
        System.out.println("during the " + gamePhase + " phase these are the available commands:");
        switch (gamePhase) {
            //during SETUP phase the user can only play the assistant card
            case SETUP ->
                    System.out.println("--playAssistantCard (play the assistant card to establish the turn order)");
            //print all actions available during ACTION phase
            case ACTION -> {
                System.out.println("--moveStudent (move one student from entrance to dining room or one island)");
                //only in advanced game mode the user can play a CharacterCard
                if (this.clientView.getGameBoard().getGameMode() == GameMode.ADVANCED) {
                    System.out.println("--playCharacterCard (activate the powerful effect of the character card)");
                }
                System.out.println("--moveMotherNature (move mother nature and calculate the influence");
                System.out.println("--chooseCloud (fill your entrance after moving three students)");
                //only in advanced game mode the user can get CharacterCard's info
                if (this.clientView.getGameBoard().getGameMode() == GameMode.ADVANCED)
                    System.out.println("--characterCardInfo (show the information about one characterCard)");
                System.out.println("--endTurn (end your turn)");
            }
            default -> System.out.println("Gamephase is not valid");
        }
    }

    /**
     * Support method used to acquire chosen characterCard from command line; it requires user to type the characterCard number
     * (not the index inside Game's list of 3 characterCards but the "id" o the card between 1 and 12). The methods also checks
     * if the chosen characterCard's number is present among the 3 CharacterCards available during the game.
     *
     * @return int representing characterCard's number
     * @throws IOException if an I/O error occurs
     */
    private int getCharacterCardIndex() throws IOException {
        System.out.println("Pick one character card from the list above (type the card number)");
        int selected;
        //ArrayList containing the 3 characterCards' numbers available during the game
        ArrayList<Integer> characterCardsNumbers =
                this.clientView.getGameBoard().getCharacterCards().stream().map(CharacterCard::getId).collect(Collectors.toCollection(ArrayList::new));
        do {
            //acquire number
            selected = getInt();
            //check if this number belongs to the list of available characterCards
            if (!characterCardsNumbers.contains(selected)) {
                System.out.println("CharacterCard not available");
            } else {
                break;
            }
            //repeat until the chosen number belongs to the list of available characterCards
        } while (true);
        //return the chosen number
        return selected;
    }

    /**
     * Support method to read an integer from command line, this method does not accept 'enter' as input
     *
     * @return the integer read from command line
     * @throws IOException if an I/O error occurs
     */
    private int getInt() throws IOException {
        int result;
        while (true) {
            try {
                //get text from stdIn
                String text = stdIn.readLine();
                if (text == null)
                    throw new IOException();
                //try to parse String from stdIn to Integer
                result = Integer.parseInt(text);
                break;
            } catch (NumberFormatException ex) {
                System.out.println("This string is not a number, retry.");
            }
            //repeat until the String is a valid number
        }
        return result;
    }

    /**
     * Support method to read an integer from command line, this method accepts 'enter' as input
     *
     * @return the integer read from command line or Optional.empty if the user pressed "Enter"
     * @throws IOException if an I/O error occurs
     */
    private Optional<Integer> getOptionalInt() throws IOException {
        int result;
        while (true) {
            try {
                //get text from stdIn
                String text = stdIn.readLine();
                if (text == null)
                    throw new IOException();
                //if user has typed 'enter' then return Optional.empty
                if (text.equals(""))
                    return Optional.empty();
                //try to parse String from stdIn to Integer
                result = Integer.parseInt(text);
                break;
            } catch (NumberFormatException ex) {
                System.out.println("This string is not a number, retry.");
            }
            //repeat until the String is a valid number
        }
        return Optional.of(result);
    }

    /**
     * Support method that create the PlayerActionRequest after that user type a PawnColour.
     *
     * @param selected      selected CharacterCard's index among the 3 available during the game
     * @param currentPlayer current player in turn, necessary to create PlayCharacterCard action
     * @return PlayerActionRequest with the selected PawnColour as parameter
     * @throws IOException if an I/O error occurs
     */
    private PlayerActionRequest createPlayerActionByPawnColour(int selected, PlayerBoard currentPlayer) throws IOException {
        //create PlayerActionRequest object
        PlayerActionRequest playerActionRequest;
        //create PlayCharacterCard object
        PlayCharacterCard playCharacterCard = null;
        boolean repeat = true;
        do {
            //get the input from stdIn, every switch's case create a playCharacterCard with selected pawnColour
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
            //repeat until the user types a valid pawnColour
        } while (repeat);
        //initialize playerActionRequest
        playerActionRequest = new PlayerActionRequest(playCharacterCard);
        //return the playerActionRequest
        return playerActionRequest;
    }
}
