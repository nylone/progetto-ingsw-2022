package it.polimi.ingsw.Server;

import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Network.KeepAliveSocketWrapper;
import it.polimi.ingsw.Network.SocketWrapper;
import it.polimi.ingsw.Server.Messages.ServerResponses.Welcome;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is the main server thread. <br>
 * Expected behaviour: a thread runs forever, accepting all connections and dispatching the
 * sockets to the {@link LobbyServer}.
 */
public class WelcomeServer implements Runnable {
    private final ServerSocket serverSocket;

    /**
     * Create a new Welcome server, once run the server binds to an address and listens for connections
     *
     * @param port    the port the server will bind to
     * @param address the address the server will bind to
     */
    public WelcomeServer(int port, InetAddress address) {
        Logger.info("Starting Welcome Server on: " + address + ":" + port);
        try {
            this.serverSocket = new ServerSocket(port, 1000, address);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Used when running the server in a {@link Thread}, will make the server listen for connections, dispatching a
     * {@link LobbyServer} for each new connection.
     */
    @Override
    public void run() {
        Logger.info("Server initialized and listening for new connections");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                SocketWrapper sw = new KeepAliveSocketWrapper(socket, 5000, false);
                Logger.info("New connection from: " + sw.getInetAddress());
                sw.sendMessage(new Welcome());
                LobbyServer.spawn(sw);
            } catch (IOException e) {
                Logger.severe("Caught an exception while awaiting new connections:\n" + e.getMessage());
                return;
            }
        }
    }
}
