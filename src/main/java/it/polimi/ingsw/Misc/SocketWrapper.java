package it.polimi.ingsw.Misc;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class SocketWrapper {
    private static final Logger log = Logger.getAnonymousLogger();
    private final Socket sock;
    private final InputStreamReader input;
    private final JsonReader jsonReader;
    private final BufferedOutputStream output;

    public SocketWrapper(Socket socket) throws IOException {
        this.sock = socket;

        // get the input reader
        this.input = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
        this.jsonReader = new JsonReader(this.input);

        // get output writer
        this.output = new BufferedOutputStream(this.sock.getOutputStream());
    }

    public SocketWrapper(ServerSocket serverSocket) throws IOException {
        this.sock = serverSocket.accept();

        // get the input reader
        this.input = new InputStreamReader(this.sock.getInputStream(), StandardCharsets.UTF_8);
        this.jsonReader = new JsonReader(this.input);

        // get output writer
        this.output = new BufferedOutputStream(this.sock.getOutputStream());
    }

    public Message awaitMessage() throws IOException {
        Message read = null;
        do {
            try {
                read = new Gson().fromJson(this.jsonReader, Message.class);
            } catch (JsonSyntaxException e) {
                log.severe("received invalid syntax on: " + e.getMessage());
                if (this.input.read() == -1) {
                    log.info("closing SocketWrapper");
                    this.sock.close();
                    log.info("closed SocketWrapper");
                    throw new SocketException("SocketWrapper is closed");
                }
            }
        } while (read == null);
        return read;
    }

    public <T extends MessageBuilder> void sendMessage(T messageToBuild) throws IOException {
        this.sendMessage(messageToBuild.build());
    }

    public void sendMessage(Message message) throws IOException {
        String toSend = message.toJson() + "\n";
        this.output.write(toSend.getBytes(StandardCharsets.UTF_8));
        this.output.flush();
    }

    public void close() {
        if (!isClosed()) {
            try {
                this.sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isClosed() {
        return this.sock.isClosed();
    }

    public InetAddress getInetAddress() {
        return sock.getInetAddress();
    }
}
