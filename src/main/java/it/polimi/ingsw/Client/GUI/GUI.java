package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Panels.StartPanel;

public class GUI {
    public static void main(String ... args) {
        new Thread(() -> {
            Context ctx = new Context();
            ctx.setWindow(new Window());
            new StartPanel(ctx);
        }).start();
    }
}
