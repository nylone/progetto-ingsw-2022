package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Panels.StartPanel;

public class GUI {
    public static void init() {
        new Thread(GUI::start).start();
    }

    private static void start() {
        Context ctx = new Context();
        ctx.setWindow(new Window());
        new StartPanel(ctx);
    }
}
