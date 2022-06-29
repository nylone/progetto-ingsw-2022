import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Server.WelcomeServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * main class of the project, call the {@link #main} function to start the whole thing up
 */
public class Main {
    /**
     * When fed the proper cli inputs, starts the appropriate elements of the project
     * @param args
     */
    public static void main(String... args) {
        if (args.length >= 1) {
            for (String arg : args) {
                switch (arg.trim().toLowerCase()) {
                    case "-d" -> Logger.enable(true);
                    case "c" -> {
                        CLI.main();
                        return;
                    }
                    case "g" -> {
                        GUI.main();
                        return;
                    }
                    case "s" -> {
                        new Thread(new WelcomeServer()).start();
                        return;
                    }
                    case String a && a.startsWith("s:") -> {
                        int port = Integer.parseInt(a.substring(2));
                        new Thread(new WelcomeServer(port, new InetSocketAddress(port).getAddress())).start();
                        return;
                    }
                    case default -> {
                        Logger.info("Wrong input, quitting...");
                        return;
                    }
                }
            }
        }
    }
}
