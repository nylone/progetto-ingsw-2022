package it.polimi.ingsw.Client;

import it.polimi.ingsw.RemoteView.Messages.Events.CreateLobby;
import it.polimi.ingsw.RemoteView.Messages.Events.DeclarePlayer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class ClientProber {
    public static void main(String... args) throws IOException {
        Logger log = Logger.getLogger(ClientProber.class.getName());
        Socket connection = new Socket("127.0.0.1", 8080);
        BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        log.info(inputStream.readLine());
        System.in.read();

        DeclarePlayer dp = new DeclarePlayer("pablo", "pablito");
        outputStream.write(dp.build().toJson().getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        System.out.println(inputStream.readLine());
        System.in.read();

        CreateLobby clFail = new CreateLobby(true, 5);
        outputStream.write(clFail.build().toJson().getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        System.out.println(inputStream.readLine());
        System.in.read();

        CreateLobby clSuccess = new CreateLobby(true, 4);
        outputStream.write(clSuccess.build().toJson().getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        System.out.println(inputStream.readLine());
        System.in.read();
    }
}
