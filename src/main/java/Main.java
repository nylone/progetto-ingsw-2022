import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Server.WelcomeServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String... args) {
        if (args.length >= 1) {
            for (String arg : args) {
                switch (arg.trim().toLowerCase()) {
                    case "-d" -> Logger.enable(true);
                    case "c" -> {
                        CLI.main();
                    }
                    case "g" -> {
                        GUI.main();
                    }
                    case "s" -> {
                        new Thread(new WelcomeServer()).start();
                    }
                    case String a && a.startsWith("s:") -> {
                        int port = Integer.parseInt(a.substring(2));
                        new Thread(new WelcomeServer(port, new InetSocketAddress(port).getAddress())).start();
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
