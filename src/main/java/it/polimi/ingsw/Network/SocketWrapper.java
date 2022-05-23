package it.polimi.ingsw.Network;

import it.polimi.ingsw.Logger;
import it.polimi.ingsw.RemoteView.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class SocketWrapper {
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
                Logger.severe("received invalid class as message: " + e.getMessage());
                if (this.input.read() == -1) {
                    Logger.info("closing SocketWrapper");
                    this.close();
                    Logger.info("closed SocketWrapper");
                    throw new SocketException("SocketWrapper is closed");
                }
            }
        }
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

    public synchronized void sendMessage(Message message) throws IOException {
        this.output.writeObject(message);
        this.output.flush();
    }

    public InetAddress getInetAddress() {
        return sock.getInetAddress();
    }
}
