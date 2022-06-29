package it.polimi.ingsw.Client.CLI;


import it.polimi.ingsw.Client.HeartBeatHandler;
import it.polimi.ingsw.Network.SocketWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;

/**
 * This class runs game's cli version and to do that it initializes client's view and runs 2 different threads:
 * One for writing elements on CLI and send message to Server (CliWriter class)
 * One for receiving responses from Server and update Client's view (ClientReader class)
 */
public class CLI implements Runnable {
    public static void main(String... args) {
        new Thread(new CLI()).start();
    }

    /**
     * Run Thread responsible for asking User which server wants to connect to
     */
    public void run() {
        //Create and Initialize BufferedReader
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        Socket connection;
        try {
            System.out.println("Type server's ip address");
            String ipAddress;
            boolean validate;
            do {
                ipAddress = stdIn.readLine(); //get input from stdIn
                validate = isIp(ipAddress); //check if is a valid ip address
                if (!validate) System.out.println("Ip address not valid");
                else break;
                //repeat until the user types a valid ip address
            } while (true);
            System.out.println("Type server's port");
            int port;
            do {
                while (true) {
                    try {
                        //get input from stdIN
                        String portString = stdIn.readLine();
                        if (portString == null)
                            throw new IOException();
                        //get int value from int
                        port = Integer.parseInt(portString);
                        break;
                    } catch (NumberFormatException ex) {
                        System.out.println("This string is not a number, retry.");
                    }
                    //repeat until the user types a number
                }
                if (port < 1024 || port > 65535) System.out.println("Port number not valid, try again");
                else break;
                //repeat until the user types a valid port number
            } while (true);

            try {
                //try to open connection with given parameters
                connection = new Socket(ipAddress, port);
            } catch (ConnectException connectException) {
                System.out.println("No server listening in this socket, quitting the game...");
                return;
            }
            //create and initialize SocketWrapper
            SocketWrapper socketWrapper = new SocketWrapper(connection);
            HeartBeatHandler.handle(socketWrapper);
            OpenCLI(socketWrapper, stdIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * used to verify that the string entered by the user is an ip address
     *
     * @param string String typed by user
     * @return true if the string is a valid ip address, false otherwise
     */
    private boolean isIp(String string) {
        //divided string in parts using '\\' as limiter
        String[] parts = string.split("\\.", -1);
        return parts.length == 4 // 4 parts
                && Arrays.stream(parts)
                .filter(this::isDecimal) // Only decimal numbers
                .map(Integer::parseInt)
                .filter(i -> i <= 255 && i >= 0) // Must be inside [0, 255]
                .count() == 4; // 4 numerical parts inside [0, 255]
    }

    /**
     * Support method to initialize CliWriter and ClientReader threads, it also creates and initialize Client's view
     *
     * @param socket         SocketWrapper used to wrap the socket used from Client and Server
     * @param bufferedReader BufferedReader used to acquire ip address and port number will be used to acquire commands during the game
     */
    private static void OpenCLI(SocketWrapper socket, BufferedReader bufferedReader) {
        //initialize cyclic barrier shared by CliWriter and CliReader
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        //initialize Client's view
        ClientView clientView = new ClientView();
        //initialize cliWriter
        CliWriter cliWriter = new CliWriter(socket, clientView, bufferedReader, cyclicBarrier);
        //start cliWriter's thread
        Thread writerThread = new Thread(cliWriter);
        writerThread.start();
        //initialize ClientReader
        ClientReader ClientReader = new ClientReader(socket, clientView, cyclicBarrier);
        //start ClientReader's thread
        Thread readerThread = new Thread(ClientReader);
        readerThread.start();

    }

    /**
     * Check that a string represents a decimal number
     *
     * @param string The string to check
     * @return true if string consists of only numbers without leading zeroes, false otherwise
     */
    private boolean isDecimal(String string) {
        // Check whether string has a leading zero but is not "0"
        if (string.startsWith("0")) {
            return string.length() == 1;
        }
        for (char c : string.toCharArray()) {
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
