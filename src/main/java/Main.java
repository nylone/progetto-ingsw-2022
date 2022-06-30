import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Server.WelcomeServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * main class of the project, call the {@link #main} function to start the whole thing up
 */
public class Main {
    private static final String HELP_MESSAGE = """
            Welcome to the Eriantys startup tool!
                                
            This tool must be called with one of the following arguments:
            - < s > will start the Server (default bound to 0.0.0.0:8080)
            - < g > will start the GUI
            - < c > will start the CLI
            - < h > or < -h > will print this message
                                
            Additionally, one or more of the following arguments may be used (but not repeated):
            - < -d > will enable the logger, useful for debugging
            - < -local > will force the server to bind to the loopback address of the machine
            - < -port > followed (without using spaces) by the port the server will be listening to
                             
            Here are some examples of argument combinations:
                -d -local -port80 s         [starts the server binding it to 127.0.0.1:80]
                -port420 s                  [starts the server binding it to 0.0.0.0:420]
                -d g                        [starts the GUI enabling the logger]
                c                           [starts the CLI]
            """;

    /**
     * When fed the proper cli inputs, starts the appropriate elements of the project
     *
     * @param args the program's arguments
     */
    public static void main(String... args) throws UnknownHostException {
        InetAddress serverBinding = InetAddress.getByAddress(new byte[]{0, 0, 0, 0});
        int serverPort = 8080;
        if (args.length >= 1) {
            for (String arg : args) {
                switch (arg.trim().toLowerCase()) {
                    case "-d" -> Logger.enable(true);
                    case "-local" -> serverBinding = InetAddress.getLoopbackAddress();
                    case String a && a.startsWith("-port") -> serverPort = Integer.parseInt(a.substring(5));
                    case default -> {
                    }
                }
            }
            for (String arg : args) {
                switch (arg.trim().toLowerCase()) {
                    case "h", "-h" -> {
                        System.out.println(HELP_MESSAGE);
                        return;
                    }
                    case "c" -> {
                        new Thread(new CLI()).start();
                        return;
                    }
                    case "g" -> {
                        GUI.main();
                        return;
                    }
                    case "s" -> {
                        new Thread(new WelcomeServer(serverPort, serverBinding)).start();
                        return;
                    }
                    case default -> {
                    }
                }
            }
        } else {
            System.out.println(HELP_MESSAGE);
        }
    }
}
