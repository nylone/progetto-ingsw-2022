package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.RemoteView.Messages.ServerResponses.StatusCode;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.WelcomeServerAccept;

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
    private final ServerSocket socket;

    public WelcomeServer() throws IOException {
        this(8080, InetAddress.getLoopbackAddress());
    }

    public WelcomeServer(int port, InetAddress address) throws IOException {
        this.socket = new ServerSocket(port, 50, address);
    }

    public static void main(String... args) throws IOException {
        WelcomeServer ws = new WelcomeServer();
        new Thread(ws).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                SocketWrapper sw = new SocketWrapper(this.socket);
                sw.sendMessage(new WelcomeServerAccept(StatusCode.Success));
                LobbyServer.spawn(sw);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
