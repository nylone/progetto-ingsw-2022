import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Server.WelcomeServer;

import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.spi.InetAddressResolver;
import java.util.Arrays;

public class Main {
    public static void main(String... args) throws IOException {
        if (args.length >= 1) {
            for (String arg: args) {
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
