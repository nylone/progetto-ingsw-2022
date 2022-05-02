package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.Messages.Enums.StatusCode;
import it.polimi.ingsw.Server.Messages.ServerMessages.WelcomeServerAccept;

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

    private final LobbyServer lobbyServer;

    public WelcomeServer() throws IOException {
        this(8080, InetAddress.getLoopbackAddress());
    }

    public WelcomeServer(int port, InetAddress address) throws IOException {
        this.lobbyServer = new LobbyServer();
        this.socket = new ServerSocket(port, 50, address);
    }

    public static void main(String... args) throws IOException {
        WelcomeServer ws = new WelcomeServer();
        new Thread(ws).start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("Listening for connections...");
                Socket connection = this.socket.accept();
                System.out.println("New connection detected from: " +
                        connection.getInetAddress() + ":" + connection.getPort());
                SocketWrapper sw = new SocketWrapper(connection);
                sw.sendResponse(new WelcomeServerAccept(StatusCode.Success));
                this.lobbyServer.asyncHandle(sw);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
