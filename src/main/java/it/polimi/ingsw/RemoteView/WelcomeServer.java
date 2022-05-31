package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.Welcome;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is the main server thread. </br>
 * Expected behaviour: a thread runs forever, accepting all connections and dispatching the
 * sockets to the LobbyServer.
 */
public class WelcomeServer implements Runnable {
    private final ServerSocket serverSocket;

    public WelcomeServer() {
        this(8080, InetAddress.getLoopbackAddress());
    }

    public WelcomeServer(int port, InetAddress address) {
        Logger.info("Starting Welcome Server on: " + address + ":" + port);
        try {
            this.serverSocket = new ServerSocket(port, 1000, address);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Thread(new WelcomeServer()).start();
    }

    @Override
    public void run() {
        Logger.info("Server initialized and listening for new connections");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                SocketWrapper sw = new SocketWrapper(socket);
                Logger.info("New connection from: " + sw.getInetAddress());
                sw.sendMessage(new Welcome(StatusCode.Success));
                LobbyServer.spawn(sw);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
