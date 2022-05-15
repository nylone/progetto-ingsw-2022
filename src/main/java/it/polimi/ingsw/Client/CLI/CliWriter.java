package it.polimi.ingsw.Client.CLI;

import com.google.gson.Gson;
import it.polimi.ingsw.Client.ClientView;
import it.polimi.ingsw.Exceptions.Container.InvalidContainerIndexException;
import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.GamePhase;
import it.polimi.ingsw.Model.PlayerBoard;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.ConnectLobbyRequest;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.CreateLobbyRequest;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.DeclarePlayerRequest;
import it.polimi.ingsw.RemoteView.Messages.Events.Requests.StartGameRequest;
import it.polimi.ingsw.RemoteView.SocketWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
            while(true) {
                System.out.println("Insert Username:");
                nickname = stdIn.readLine();
                System.out.println("Insert Password");
                password = stdIn.readLine();
                if(nickname.equals("") || password.equals("")){
                    System.out.println("Username or password not well formatted");
                }else {
                    DeclarePlayerRequest dp = new DeclarePlayerRequest(nickname, password);
                    socketWrapper.sendMessage(dp);
                    Thread.sleep(1000);
                    if (this.clientView.isLogged()) {
                        break;
                    }
                }
            }
            this.clientView.setNickname(nickname);
            String input;
            while(true){
                input = stdIn.readLine();
                elaborateInput(input);

            }
        } catch (IOException | InterruptedException | InvalidContainerIndexException e) {
            System.out.println("IO exception when reading from stdIn.");
        }
    }

    private void elaborateInput(String userInput) throws IOException, InvalidContainerIndexException {
        switch(userInput){
            case "showActions" -> printActions();
            case "createLobby" -> createLobby();
            case "joinLobby" -> joinLobby();
            case "startGame" -> startGame();
            case "playAssistantCard" -> playAssistantCard();
            case "moveStudent" -> moveStudent();
            case "moveMotherNature" -> moveMotherNature();
            default -> System.out.println("Command not valid");
        }
    }


    private void createLobby() throws IOException {
        if(!clientView.isInLobby()){
            CreateLobbyRequest createLobbyRequest;
            System.out.println("Do you want to create an open or private lobby?");
            System.out.println("O : open");
            System.out.println("P : private");
            boolean isPublic;
            String input;
            loop:
            while(true){
                input = stdIn.readLine().toUpperCase();
                switch (input){
                    case "O" ->{
                        isPublic = true;
                        break loop;
                    }
                    case "P" ->{
                        isPublic = false;
                        break loop;
                    }
                    case default -> System.out.println("input not correct, please try again");
                }
            }
            System.out.println("how many player will the lobby contain?");
            int players;
            while(true) {
                players = Integer.parseInt(stdIn.readLine());
                if(players>=2 && players<=4){
                    break;
                }
                System.out.println("Amount of players not valid");
            }
            createLobbyRequest = new CreateLobbyRequest(isPublic, players);
            socketWrapper.sendMessage(createLobbyRequest);
        }else {
            System.out.println("You are already in a lobby");
        }
    }

    private void joinLobby() throws IOException {
        if(!clientView.isInLobby()){
            ConnectLobbyRequest connectLobbyRequest;
            System.out.println("Insert lobby's UUID");
            UUID id = UUID.fromString(stdIn.readLine());
            connectLobbyRequest = new ConnectLobbyRequest(id);
            socketWrapper.sendMessage(connectLobbyRequest);

        }else {
            System.out.println("You are already in a lobby");
        }
    }

    private void startGame() throws IOException {
        System.out.println("Select the game mode:");
        System.out.println("S: simple");
        System.out.println("A: advanced");
        GameMode gameMode;
        String input;
        StartGameRequest startGameRequest;
        loop:
        while(true){
            input = stdIn.readLine().toUpperCase();
            switch (input){
                case "S" ->{
                    gameMode = GameMode.SIMPLE;
                    break loop;
                }
                case "A" ->{
                    gameMode = GameMode.ADVANCED;
                    break loop;
                }
                case default -> System.out.println("input not correct, please try again");
            }
        }
        startGameRequest = new StartGameRequest(gameMode);
        socketWrapper.sendMessage(startGameRequest);
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

    private void printGameActions(){
        if(!this.clientView.getNickname().equals(this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getNickname())){
            System.out.println("No actions are allowed out of turn");
            return;
        }
            GamePhase gamePhase = this.clientView.getGameBoard().getMutableTurnOrder().getGamePhase();
            System.out.println("during the "+gamePhase+" phase these are the available commands:");
            switch(gamePhase){
                case SETUP -> {
                    System.out.println("--playAssistantCard (play the assistant card to establish the turn order)");
                }
                case ACTION -> {
                    System.out.println("--moveStudent (move one student from entrance to dining room or one island)");
                    if(this.clientView.getGameBoard().getGameMode() == GameMode.ADVANCED){
                        System.out.println("--playCharacterCard (activate the powerful effect of the character card)");
                    }
                    System.out.println("--moveMotherNature (move mother nature and calculate the influence");
                }
                default -> System.out.println("Gamephase is not valid");
            }
        }

    private void playAssistantCard() throws InvalidContainerIndexException, IOException {
        System.out.println("select one of these available assistant card");
        //get the unused cards
        ArrayList<AssistantCard> availableAssistants = this.clientView.getGameBoard().getMutablePlayerBoardByNickname(this.clientView.getNickname()).getMutableAssistantCards()
                .stream().filter(assistantCard -> assistantCard.getUsed() == false)
                .collect(Collectors.toCollection(ArrayList::new));
        //from the unused cards, extract the card with a priority not selected before by other players
        for (PlayerBoard pb : this.clientView.getGameBoard().getMutableTurnOrder().getCurrentTurnOrder()) {
            if (this.clientView.getGameBoard().getMutableTurnOrder().getMutableSelectedCard(pb).isPresent()) {
                availableAssistants.removeIf(assistantCard -> assistantCard.getPriority() == this.clientView.getGameBoard().getMutableTurnOrder().getMutableSelectedCard(pb).get().getPriority());
            }
        }
        //get the priority of the available cards
        ArrayList<Integer> cardNumbers = availableAssistants.stream().map(assistantCard -> assistantCard.getPriority()).collect(Collectors.toCollection(ArrayList::new));
        availableAssistants.forEach(assistantCard -> System.out.println("priority:" + assistantCard.getPriority() + " maxMovement:" + assistantCard.getMaxMovement()));
        //get the selected card
        do {
            int selected = getInt();
            if(!cardNumbers.contains(selected)){
                System.out.println("Card not available");
            } else {
                break;
            }
        }while(true);

        //todo create message to send to the server
    }

    private void moveStudent() throws IOException {
        int entranceSize = this.clientView.getGameBoard().getMutableTurnOrder().getMutableCurrentPlayer().getEntranceSize() - 1;
        System.out.println("Insert the number of entrance's position between 0 and "+ entranceSize);
        int selected = getInt();

        System.out.println("Type the island id to move the selected student there or press 'Enter' to send it to the dining room");
        Optional<Integer> choice = getInt(stdIn.readLine());
        if(choice.isEmpty()){
            //todo moveDestination is diningroom
        }else{
            //todo moveDestination is an island
        }

        //todo create message to send to the server

    }

    private void moveMotherNature() throws IOException {
        System.out.println("How many steps do you want mother nature to take?");
        int steps = getInt();
        
    }

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
