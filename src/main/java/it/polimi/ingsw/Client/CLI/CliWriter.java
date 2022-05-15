package it.polimi.ingsw.Client.CLI;

import com.google.gson.Gson;
import it.polimi.ingsw.Client.ClientView;
import it.polimi.ingsw.Controller.Actions.*;
import it.polimi.ingsw.Controller.Enums.MoveDestination;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.*;
import it.polimi.ingsw.RemoteView.SocketWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class takes Client's commands from terminal, provide support to the user and send the command to the Server by using the SocketWrapper
 */
public class CliWriter implements Runnable{


    private final SocketWrapper socketWrapper;
    /**
     * used to store the client's game data
     */
    private final ClientView clientView;

    private final BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    private final Gson gson;


    public CliWriter(SocketWrapper socketWrapper, ClientView clientView) {
        this.socketWrapper = socketWrapper;
        this.clientView = clientView;
        this.gson = new Gson();
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
        } catch (IOException | InterruptedException e) {
            System.out.println("IO exception when reading from stdIn.");
            e.printStackTrace();
        }
    }

    /**
     * This method, given a String, executes the proper method according to User's request
     * @param userInput: text containing the command
     * @throws IOException the integer read from command line
     */
    private void elaborateInput(String userInput) throws IOException {
        switch (userInput) {
            case "showActions" -> printActions();
            case "createLobby" -> createLobby();
            case "joinLobby" -> joinLobby();
            case "startGame" -> startGame();
            case "playAssistantCard" -> playAssistantCard();
            case "moveStudent" -> moveStudent();
            case "moveMotherNature" -> moveMotherNature();
            case "chooseCloud" -> chooseCloud();
            case "endTurn" -> endTurn();
            default -> System.out.println("Command not valid");
        }
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
                System.out.println("-- StartGame (start the game)");
            } else {
                System.out.println("No actions available, wait for the admin to start the game");
            }
            return;
        }
        printGameActions();
    }

    /**
     * Executes the chooseCloud command
     * @throws IOException if an I/O error occurs
     */
    private void chooseCloud() throws IOException {
        System.out.println("Select one cloud from 0 to "+(this.clientView.getGameBoard().getClouds().size()-1));
        int selectedCloud = getInt();

        ChooseCloudTile chooseCloudTile = new ChooseCloudTile(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), selectedCloud);
        PlayerActionRequest clientPlayerAction = new PlayerActionRequest(chooseCloudTile);
        socketWrapper.sendMessage(clientPlayerAction);
    }

    /**
     *Executes the createLobby command,
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
     * Executes the endTurn command
     */
    private void endTurn() throws IOException {
        EndTurnOfActionPhase endTurnOfActionPhase = new EndTurnOfActionPhase(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId());
        PlayerActionRequest playerActionRequest = new PlayerActionRequest(endTurnOfActionPhase);
        socketWrapper.sendMessage(playerActionRequest);
    }

    /**
     * Executes the joinLobby command by knowing lobby's UUID
     * @throws IOException if an I/O error occurs
     */
    private void joinLobby() throws IOException {
        if (!clientView.isInLobby()) {
            ConnectLobbyRequest connectLobbyRequest;
            System.out.println("Insert lobby's UUID");
            UUID id = UUID.fromString(stdIn.readLine());
            connectLobbyRequest = new ConnectLobbyRequest(id);
            socketWrapper.sendMessage(connectLobbyRequest);

        } else {
            System.out.println("You are already in a lobby");
        }
    }

    /**
     * Executes the startGame command, it can be executed only by lobby's admin and whether the lobby is full
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
     * print all the available actions basing on current game phase
     */
    private void printGameActions(){
        if(!this.clientView.getNickname().equals(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getNickname())){
            System.out.println("No actions are allowed out of turn");
            return;
        }
            GamePhase gamePhase = this.clientView.getGameBoard().getMutableTurnOrder().getGamePhase();
            System.out.println("during the "+gamePhase+" phase these are the available commands:");
            switch(gamePhase){
                case SETUP -> System.out.println("--playAssistantCard (play the assistant card to establish the turn order)");
                case ACTION -> {
                    System.out.println("--moveStudent (move one student from entrance to dining room or one island)");
                    if(this.clientView.getGameBoard().getGameMode() == GameMode.ADVANCED){
                        System.out.println("--playCharacterCard (activate the powerful effect of the character card)");
                    }
                    System.out.println("--moveMotherNature (move mother nature and calculate the influence");
                    System.out.println("--chooseCloud (fill your entrance after moving three students)");
                    System.out.println("--endTurn (end your turn)");
                }
                default -> System.out.println("Gamephase is not valid");
            }
        }

    /**
     * Executes the playAssistantCard command
     *The method shows the available assistantCard at that moment
     *
     * @throws IOException if an I/O error occurs
     */
    private void playAssistantCard()throws IOException {
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
            if(!cardNumbers.contains(selected)){
                System.out.println("Card not available");
            } else {
                break;
            }
        }while(true);
        System.out.println("SELECTED:"+selected);
        System.out.println("ID:"+this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId());
        PlayAssistantCard playAssistantCard = new PlayAssistantCard(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), selected);
        PlayerActionRequest playerAction = new PlayerActionRequest(playAssistantCard);
        System.out.println(playerAction.getPayloadType());
        socketWrapper.sendMessage(playerAction);
        //todo create message to send to the server
    }

    /**
     * Executes the moveStudent command
     * @throws IOException if an I/O error occurs
     */
    private void moveStudent() throws IOException {
        int entranceSize = this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getEntranceSize() - 1;
        System.out.println("Insert the number of entrance's position between 0 and " + entranceSize);
        int selected = getInt();

        System.out.println("Type the island id to move the selected student there or press 'Enter' to send it to the dining room");
        Optional<Integer> choice = getInt(stdIn.readLine());
        MoveStudent moveStudent;
        if(choice.isEmpty()){
            moveStudent = new MoveStudent(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(),selected, MoveDestination.toDiningRoom());
        }else{
            moveStudent = new MoveStudent(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), selected, MoveDestination.toIsland(choice.get()));
        }

        PlayerActionRequest clientPlayerAction = new PlayerActionRequest(moveStudent);
        socketWrapper.sendMessage(clientPlayerAction);
        //todo create message to send to the server

    }

    /**
     * Executes the moveMotherNature command
     * @throws IOException if an I/O error occurs
     */
    private void moveMotherNature() throws IOException {
        System.out.println("How many steps do you want mother nature to take?");
        int steps = getInt();
        MoveMotherNature moveMotherNature = new MoveMotherNature(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getId(), steps);
        PlayerActionRequest clientPlayerAction = new PlayerActionRequest(moveMotherNature);
        socketWrapper.sendMessage(clientPlayerAction);
        //todo create message to send to the server
    }

    /**
     * Support method to read an integer from command line
     *
     * @return the integer read from command line
     * @throws IOException if an I/O error occurs
     */
    private int getInt() throws IOException{
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
     *
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
}
