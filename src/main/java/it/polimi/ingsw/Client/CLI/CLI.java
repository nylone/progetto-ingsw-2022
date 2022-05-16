package it.polimi.ingsw.Client.CLI;


import it.polimi.ingsw.RemoteView.SocketWrapper;

import java.net.Socket;


public class CLI implements Runnable {

    public void run() {
        try {
            Socket connection = new Socket("127.0.0.1", 8080);
            SocketWrapper socketWrapper = new SocketWrapper(connection);
            OpenCLI(socketWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void OpenCLI(SocketWrapper socket) {
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
