package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Misc.Pair;
import it.polimi.ingsw.Misc.SocketWrapper;

import java.util.List;
import java.util.UUID;

public class Context {
    private Window window;
    private SocketWrapper socketWrapper;
    private String nickname;
    private List<Pair<UUID, String>> openLobbies;

    public List<Pair<UUID, String>> getOpenLobbies() {
        return List.copyOf(this.openLobbies);
    }

    public void setOpenLobbies(List<Pair<UUID, String>> openLobbies) {
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
