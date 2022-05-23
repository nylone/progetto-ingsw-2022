package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Network.SocketWrapper;

public class Context {
    private Window window;
    private SocketWrapper socketWrapper;
    private String nickname;

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
