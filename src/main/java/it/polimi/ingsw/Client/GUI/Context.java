package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Misc.SocketWrapper;
import it.polimi.ingsw.RemoteView.Messages.ServerResponses.LobbyInfo;

import java.util.List;

public class Context {
    private Window window;
    private SocketWrapper socketWrapper;
    private String nickname;
    private List<LobbyInfo> openLobbies;
    private List<LobbyInfo> reconnectToTheseLobbies;

    public List<LobbyInfo> getReconnectToTheseLobbies() {
        return reconnectToTheseLobbies;
    }

    public void setReconnectToTheseLobbies(List<LobbyInfo> reconnectToTheseLobbies) {
        this.reconnectToTheseLobbies = reconnectToTheseLobbies;
    }

    public List<LobbyInfo> getOpenLobbies() {
        return List.copyOf(this.openLobbies);
    }

    public void setOpenLobbies(List<LobbyInfo> openLobbies) {
        this.openLobbies = openLobbies;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public SocketWrapper getSocketWrapper() {
        return socketWrapper;
    }

    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
