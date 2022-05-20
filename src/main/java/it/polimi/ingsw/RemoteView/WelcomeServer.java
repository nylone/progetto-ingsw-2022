package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.SupportStructures.StatusCode;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.Welcome;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * This is the main server thread. </br>
 * Expected behaviour: a thread runs forever, accepting all connections and dispatching the
 * sockets to the LobbyServer.
 */
public class WelcomeServer implements Runnable {
    private static final Logger log = Logger.getLogger(WelcomeServer.class.getName());

    private final ServerSocket serverSocket;

    public WelcomeServer() {
        this(8080, InetAddress.getLoopbackAddress());
    }

    public WelcomeServer(int port, InetAddress address) {
        log.info("Starting Welcome Server on: " + address + ":" + port);
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
        log.info("Server initialized and listening for new connections");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                log.info("Detected a new connection from " + socket.getInetAddress());
                SocketWrapper sw = new SocketWrapper(socket);
                log.info("Wrapped new connection from " + sw.getInetAddress() + " into a " + SocketWrapper.class.getName());
                sw.sendMessage(new Welcome(StatusCode.Success));
                log.info("Accepted a new connection from " + sw.getInetAddress());
                LobbyServer.spawn(sw);
                log.info("Spawned a new lobby server for: " + sw.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
