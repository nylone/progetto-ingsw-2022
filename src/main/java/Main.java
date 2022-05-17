import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.RemoteView.WelcomeServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("""
                Welcome to the Eriantys startup tool!
                Please choose one of the following options:
                [S]\tstart server
                [c]\tstart CLI client
                [g]\tstart GUI client
                                
                [q]\tquit this tool
                """);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        boolean again = true;
        do {
            // read user input
            String input = reader.readLine().trim().toLowerCase();
            // handle input
            switch (input.substring(0, input.length() > 0 ? 1 : 0)) {
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
                    GUI.init();
                    again = false;
                }
                case "q" -> {
                    System.out.println("Quitting Startup tool...");
                    again = false;
                }
                case default -> System.out.println("Wrong input, please try again.");
            }
        } while (again);
    }
}
