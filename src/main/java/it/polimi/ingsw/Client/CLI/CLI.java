package it.polimi.ingsw.Client.CLI;


import it.polimi.ingsw.Client.ClientReader;
import it.polimi.ingsw.Client.ClientView;
import it.polimi.ingsw.RemoteView.SocketWrapper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;


public class CLI implements Runnable{

    public void run() {
        try {
            Logger log = Logger.getAnonymousLogger();
            Socket connection = new Socket("127.0.0.1", 8080);
            BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            log.info(inputStream.readLine());
            System.in.read();
            System.out.println("Successfully connected");

            SocketWrapper socketWrapper = new SocketWrapper(connection);

            OpenCLI(socketWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void OpenCLI(SocketWrapper socket) throws IOException {
        //todo add a countDownLatch?

        ClientView clientView = new ClientView();

        CliWriter cliWriter = new CliWriter(socket, clientView);

        Thread writerThread = new Thread(cliWriter);
        writerThread.start();

        ClientReader ClientReader = new ClientReader(socket, clientView, cliWriter);
        Thread readerThread = new Thread(ClientReader);
        readerThread.start();

    }
}
