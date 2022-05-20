package it.polimi.ingsw.Network;

import it.polimi.ingsw.RemoteView.Messages.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

public class SocketWrapper {
    private static final Logger log = Logger.getAnonymousLogger();
    private final Socket sock;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public SocketWrapper(Socket socket) throws IOException {
        this.sock = socket;
        // get output writer
        this.output = new ObjectOutputStream(this.sock.getOutputStream());
        // get the input reader
        this.input = new ObjectInputStream(socket.getInputStream());
    }

    public Message awaitMessage() throws IOException {
        while (true) {
            try {
                return (Message) input.readObject();
            } catch (ClassNotFoundException e) {
                log.severe("received invalid class as message: " + e.getMessage());
                if (this.input.read() == -1) {
                    log.info("closing SocketWrapper");
                    this.close();
                    log.info("closed SocketWrapper");
                    throw new SocketException("SocketWrapper is closed");
                }
            }
        }
    }

    public synchronized void sendMessage(Message message) throws IOException {
        this.output.writeObject(message);
        this.output.flush();
    }

    public void close() {
        try {
            this.input.close();
            this.output.flush();
            this.output.close();
            this.sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getInetAddress() {
        return sock.getInetAddress();
    }
}
