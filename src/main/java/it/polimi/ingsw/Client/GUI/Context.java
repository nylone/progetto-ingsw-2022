package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Network.SocketWrapper;

/**
 * Context holds the information needed to relate the player, the server connection
 * and the player's personal graphical representation.
 */
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
