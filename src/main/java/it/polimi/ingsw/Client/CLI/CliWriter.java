package it.polimi.ingsw.Client.CLI;

import com.google.gson.Gson;
import it.polimi.ingsw.Client.ClientView;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.RemoteView.Messages.Events.*;
import it.polimi.ingsw.RemoteView.SocketWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

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
                    "ERIANTYS\n"+
                    "Welcome Player\n"
            );
            String nickname, password;
                System.out.println("Insert Username:");
                nickname = stdIn.readLine();
                System.out.println("Insert Password");
                password = stdIn.readLine();

            DeclarePlayer dp = new DeclarePlayer(nickname, password);
            socketWrapper.sendMessage(dp);

            String input;
            while(true){
                input = stdIn.readLine();
                elaborateInput(input);

            }
        } catch (IOException e) {
            System.out.println("IO exception when reading from stdIn.");
        }
    }

    private void elaborateInput(String userInput) throws IOException {
        switch(userInput){
            case "createLobby" -> createLobby();
            case "joinLobby" -> joinLobby();
            case "startGame" -> startGame();
            default -> System.out.println("Command not valid");
        }
    }
    private void createLobby() throws IOException {
        if(!clientView.isInLobby()){
            CreateLobby createLobby;
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
                    case default -> {
                        System.out.println("input not correct, please try again");
                        break;
                    }
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
            createLobby = new CreateLobby(isPublic, players);
            socketWrapper.sendMessage(createLobby);
        }else {
            System.out.println("You are already in a lobby");
        }
    }

    private void joinLobby() throws IOException {
        if(!clientView.isInLobby()){
            ConnectLobby connectLobby;
            System.out.println("Insert lobby's UUID");
            UUID id = UUID.fromString(stdIn.readLine());
            connectLobby = new ConnectLobby(id);
            socketWrapper.sendMessage(connectLobby);
            
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
        StartGame startGame;
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
                case default -> {
                    System.out.println("input not correct, please try again");
                    break;
                }
            }
        }
        startGame = new StartGame(gameMode);
        socketWrapper.sendMessage(startGame);
    }
}
