package it.polimi.ingsw.RemoteView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.RemoteView.Messages.Message;
import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketWrapper {
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

    public Message awaitMessage() throws IOException {
        try {
            return new Gson().fromJson(this.jsonReader, Message.class);
        } catch (JsonSyntaxException e) {
            System.out.println("caught invalid syntax: " + e.getMessage());
            if (this.input.read() == -1) {
                System.out.println("client socket disconnected: closing socket");
                this.sock.close();
                System.out.println("closing SocketWrapper");
            }
            return null;
        }
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
}
