import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Server.WelcomeServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * main class of the project, call the {@link #main} function to start the whole thing up
 */
public class Main {
    private static final String HELP_MESSAGE = """
            Welcome to the Eriantys startup tool!
                                
            This tool must be called with at least one of the following arguments:
                < s > will start the Server (default bound to 0.0.0.0:8080) [ max instances: 1 ]
                < g > will start a GUI [ max instances: unlimited ]
                < c > will start the CLI [ max instances: 1 ]
                < h > or < -h > will print this message [ max instances: 1 ]
                                
            Additionally, one or more of the following arguments may be used:
                < -d > will enable the logger, useful for debugging [ max instances: 1 ]
                < -local > will force the server to bind to the loopback address of the machine [ max instances: 1 ]
                < -port: > followed (without using spaces) by the port the server will be listening to [ max instances: 1 ]
                             
            Here are some examples of argument combinations:
                -d -local -port80 s         [starts the server binding it to 127.0.0.1:80 and enables the logger]
                -port:420 s                 [starts the server binding it to 0.0.0.0:420]
                -d g                        [starts the GUI enabling the logger]
                c                           [starts the CLI]
                -local s g g c              [starts a local server (127.0.0.1:8080) along side 2 GUIs and 1 CLI]
            """;

    /**
     * When fed the proper cli inputs, starts the appropriate elements of the project
     *
     * @param args the program's arguments
     */
    public static void main(String... args) throws UnknownHostException {
        InetAddress serverBinding = InetAddress.getByAddress(new byte[]{0, 0, 0, 0});
        int serverPort = 8080;
            // if the args are coherent
            if (args.length >= 1 &&
                    Arrays.stream(args).anyMatch(arg -> arg.equals("s") || arg.equals("g") || arg.equals("c") || arg.equals("h") || arg.equals("-h")) &&
                    Arrays.stream(args).filter(arg -> arg.equals("s")).count() <= 1 &&
                    Arrays.stream(args).filter(arg -> arg.equals("c")).count() <= 1 &&
                    Arrays.stream(args).filter(arg -> arg.equals("h") || arg.equals("-h")).count() <= 1 &&
                    Arrays.stream(args).filter(arg -> arg.equals("-d")).count() <= 1 &&
                    Arrays.stream(args).filter(arg -> arg.equals("-local")).count() <= 1 &&
                    Arrays.stream(args).filter(arg -> arg.startsWith("-port:")).count() <= 1
            ) { // parse arguments
                for (String arg : args) {
                    switch (arg.trim().toLowerCase()) {
                        case "-d" -> Logger.enable(true);
                        case "-local" -> serverBinding = InetAddress.getLoopbackAddress();
                        case String a && a.startsWith("-port:") -> serverPort = Integer.parseInt(a.substring(6));
                        case default -> {}
                    }
                }
                for (String arg : args) {
                    switch (arg.trim().toLowerCase()) {
                        case "h", "-h" -> System.out.println(HELP_MESSAGE);
                        case "c" -> new Thread(new CLI()).start();
                        case "g" -> new Thread(new GUI()).start();
                        case "s" -> new Thread(new WelcomeServer(serverPort, serverBinding)).start();
                        case default -> {}
                    }
                }
        } else {
            System.out.println(HELP_MESSAGE);
        }
    }
}
