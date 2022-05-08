package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.StatusCode;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.WelcomeServerAccept;

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
    private final ServerSocket socket;

    public WelcomeServer() throws IOException {
        this(8080, InetAddress.getLoopbackAddress());
    }

    public WelcomeServer(int port, InetAddress address) throws IOException {
        this.socket = new ServerSocket(port, 50, address);
    }

    public static void main(String... args) throws IOException {
        new Thread(new WelcomeServer()).start();
    }

    @Override
    public void run() {
        Logger log = Logger.getLogger(this.getClass().getName());
        log.info("Server initialized and listening for new connections");
        while (true) {
            try {
                SocketWrapper sw = new SocketWrapper(this.socket);
                sw.sendMessage(new WelcomeServerAccept(StatusCode.Success));
                log.info("Accepted a new connection from " + sw.getInetAddress());
                LobbyServer.spawn(sw);
                log.info("Spawned a new lobby server for: " + sw.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}