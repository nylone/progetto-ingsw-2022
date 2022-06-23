import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Server.WelcomeServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Main {
    public static void main(String... args) throws IOException {
        if (args.length >= 1) {
            if (args.length > 1 && args[1].trim().equalsIgnoreCase("-d")) {
                Logger.enable(true);
            }
            switch (args[0].trim().toLowerCase()) {
                case "s" -> {
                    new Thread(new WelcomeServer()).start();
                    return;
                }
                case "c" -> {
                    CLI.main();
                    return;
                }
                case "g" -> {
                    GUI.main();
                    return;
                }
                case default -> Logger.info("Wrong input, redirecting to main menu...");
            }
        }
        System.out.println("""
            Welcome to the Eriantys startup tool!
            Please choose one of the following options:
            [S]\tstart server
            [c]\tstart CLI client
            [g]\tstart GUI client
            
            [d]\tenable the logging service for debugging purposes
                            
            [q]\tquit this tool
            
            Note: next time you run this tool, you can use the same [s/c/g] characters as command line parameters to skip this menu.
            Note: you can enable the logging service via cli parameters by appending a [-d] at the end of the arguments.
            Example: "java --enable-preview -jar <program-name>.jar s -d" will start the server in debug mode.
            """);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        boolean again = true;
        do {
            // read user input
            String input;
            input = reader.readLine().trim().toLowerCase();
            input = input.substring(0, input.length() > 0 ? 1 : 0);
            // handle input
            switch (input) {
                case "d" -> {
                    Logger.enable(true);
                    System.out.println("Logging enabled.");
                }
                case "s", "" -> {
                    System.out.println("Starting Server...");
                    new Thread(new WelcomeServer()).start();
                    again = false;
                }
                case "c" -> {
                    System.out.println("Starting CLI client...");
                    CLI.main();
                    again = false;
                }
                case "g" -> {
                    System.out.println("Starting GUI client...");
                    GUI.main();
                    again = false;
                }
                case "q" -> {
                    System.out.println("Quitting the startup tool...");
                    again = false;
                }
                case default -> System.out.println("Wrong input, please try again.");
            }
        } while (again);
    }
}
