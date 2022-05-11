package it.polimi.ingsw.Client.CLI;


import it.polimi.ingsw.Client.ClientProber;
import it.polimi.ingsw.Client.ClientReader;
import it.polimi.ingsw.Client.ClientView;
import it.polimi.ingsw.RemoteView.SocketWrapper;

import java.io.*;
;
import java.net.Socket;
import java.util.logging.Logger;


public class CLI {

    public static void main(String[] args) throws InterruptedException, IOException {
       Logger log = Logger.getLogger(ClientProber.class.getName());
       Socket connection = new Socket("127.0.0.1", 8080);
       BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
       BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
       log.info(inputStream.readLine());
       System.in.read();
       System.out.println("Successfully connected");

        SocketWrapper socketWrapper = new SocketWrapper(connection);

       OpenCLI(socketWrapper);

    }

    private static void OpenCLI(SocketWrapper socket) throws IOException {
        //todo add a countDownLatch?

        ClientView clientView = new ClientView();

        CliWriter cliWriter = new CliWriter(socket,clientView);

        Thread writerThread = new Thread(cliWriter);
        writerThread.start();

        ClientReader ClientReader = new ClientReader(socket, clientView, cliWriter);
        Thread readerThread = new Thread(ClientReader);
        readerThread.start();

    }
}
