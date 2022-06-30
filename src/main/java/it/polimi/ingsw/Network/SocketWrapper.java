package it.polimi.ingsw.Network;

import it.polimi.ingsw.Logger;
import it.polimi.ingsw.Server.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

/**
 * Wrapper around a {@link Socket} transfering {@link Message}s, removes most of the overhead of handling the connection.
 */
public class SocketWrapper {
    private final Socket sock;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    /**
     * Create the SocketWrapper around a standard socket
     * @param socket the socket to wrap around
     * @throws IOException if the socket/wrapper has issues opening its streams
     */
    public SocketWrapper(Socket socket) throws IOException {
        this.sock = socket;
        // get output writer
        this.output = new ObjectOutputStream(this.sock.getOutputStream());
        // get the input reader
        this.input = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Blocks until a message is read
     * @return the read {@link Message}
     * @throws SocketException if there's an error while reading from the object stream, the socket will close and this exception
     * is thrown
     */
    public Message awaitMessage() throws IOException {
        try {
            return (Message) input.readObject();
        } catch (Exception e) {
            Logger.info("the object stream from socket generated an exception and the SocketWrapper will now be closed." +
                    "The exception was: " + e.getClass());
            this.close();
            Logger.info("closed SocketWrapper");
            throw new SocketException("SocketWrapper is closed");
        }
    }

    /**
     * Attempts to close the socket, if the socket is already closed then it does nothing
     * @throws IOException if any exception happens during closing of the socket
     */
    public void close() throws IOException {
        if (!this.sock.isClosed()) {
            this.input.close();
            this.output.flush();
            this.output.close();
            this.sock.close();
        }
    }

    /**
     * Check to see if the socket is closed
     * @return true if the socket is closed
     */
    public boolean isClosed() {
        return sock.isClosed();
    }

    /**
     * Sends a message to the socket endpoint
     * @param message the message to send, required not null
     * @throws IOException if any error happens during the sending of the message
     */
    public synchronized void sendMessage(Message message) throws IOException {
        Objects.requireNonNull(message);
        this.output.writeObject(message);
        this.output.flush();
    }

    /**
     * Get the address this socket is bound to
     * @return the address this socket is connected to.
     */
    public InetAddress getInetAddress() {
        return sock.getInetAddress();
    }
}
