package it.polimi.ingsw.Client.CLI;


import it.polimi.ingsw.Misc.SocketWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;


public class CLI implements Runnable {

    public void run() {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Type server's ip address");
            String ipAddress;
            boolean validate;
            do{
                ipAddress = stdIn.readLine();
                validate = isIp(ipAddress);
                if (!validate) System.out.println("Ip address not valid");
                else break;
            }while (true);
            System.out.println("Type server's port");
            int port;
            do {
                while (true) {
                    try {
                        String portString = stdIn.readLine();
                        if (portString == null)
                            throw new IOException();
                        port = Integer.parseInt(portString);
                        break;
                    } catch (NumberFormatException ex) {
                        System.out.println("This string is not a number, retry.");
                    }
                }
                if(port< 1024 || port > 65535) System.out.println("Port number not valid, try again");
                else break;
            }while(true);
            Socket connection = new Socket(ipAddress, port);
            SocketWrapper socketWrapper = new SocketWrapper(connection);
            OpenCLI(socketWrapper, stdIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void OpenCLI(SocketWrapper socket, BufferedReader bufferedReader) {
        //todo add a countDownLatch?

        ClientView clientView = new ClientView();

        CliWriter cliWriter = new CliWriter(socket, clientView, bufferedReader);

        Thread writerThread = new Thread(cliWriter);
        writerThread.start();

        ClientReader ClientReader = new ClientReader(socket, clientView, cliWriter);
        Thread readerThread = new Thread(ClientReader);
        readerThread.start();

    }

    /**
     * Check that a string represents a decimal number
     * @param string The string to check
     * @return true if string consists of only numbers without leading zeroes, false otherwise
     */
    private boolean isDecimal(String string) {
        // Check whether string has a leading zero but is not "0"
        if (string.startsWith("0")) {
            return string.length() == 1;
        }
        for(char c : string.toCharArray()) {
            if(c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private boolean isIp(String string) {
        String[] parts = string.split("\\.", -1);
        return parts.length == 4 // 4 parts
                && Arrays.stream(parts)
                .filter(this::isDecimal) // Only decimal numbers
                .map(Integer::parseInt)
                .filter(i -> i <= 255 && i >= 0) // Must be inside [0, 255]
                .count() == 4; // 4 numerical parts inside [0, 255]
    }
}
