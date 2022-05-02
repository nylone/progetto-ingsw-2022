package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Server.Messages.ServerMessages.Response;

import java.io.*;
import java.net.Socket;

public class SocketWrapper {
    private final Socket sock;
    private final BufferedReader input;
    private final BufferedWriter output;
    private final Gson gson;

    public SocketWrapper(Socket socket) throws IOException {
        this.sock = socket;

        // get the input reader
        InputStream inputStream = socket.getInputStream();
        this.input = new BufferedReader(new InputStreamReader(inputStream));

        // get output writer
        OutputStream outputStream = this.sock.getOutputStream();
        this.output = new BufferedWriter(new OutputStreamWriter(outputStream));

        // get Gson object
        this.gson = new GsonBuilder().create();
    }

    public <T> T awaitRequest(Class<T> tClass) throws IOException {
        System.out.println("awaiting");
        return this.gson.fromJson(this.input.readLine(), tClass);
    }

    public void sendResponse(Response response) throws IOException {
        this.output.write(gson.toJson(response));
        this.output.flush();
    }

    public void close() throws IOException {
        this.sock.close();
    }
}
