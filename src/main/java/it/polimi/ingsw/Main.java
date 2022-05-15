package it.polimi.ingsw;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.RemoteView.WelcomeServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("""
                Welcome to the eriantys startup tool!
                Please choose one of the following options:
                [S]\tstart server
                [c]\tstart CLI client
                [g]\tstart GUI client
                
                [q]\tquit this tool
                """);
        boolean again = true;
        do {
            // read user input
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            String input = reader.readLine();
            // handle input
            switch (input.trim().substring(0,1).toLowerCase()) {
                case "s", "" -> {
                    System.out.println("Starting Server...");
                    new Thread(new WelcomeServer()).start();
                    again = false;
                }
                case "c" -> {
                    System.out.println("Starting CLI client...");
                    new Thread(new CLI()).start();
                    again = false;
                }
                case "g" -> {
                    System.out.println("Starting GUI client...");
                    System.out.println("Sorry, we are still working on that :)");
                    //again = false;
                }
                case "q" -> {
                    System.out.println("Quitting Startup tool...");
                    again = false;
                }
                case default ->
                    System.out.println("Wrong input, please try again.");
            }
        } while (again);
    }
}
