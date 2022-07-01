package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Panels.StartPanel;

/**
 * Run game's gui version by creating a new {@link Context} and a new {@link Window}
 */
public class GUI implements Runnable {
    @Override
    public void run() {
        Context ctx = new Context();
        ctx.setWindow(new Window());
        ctx.getWindow().changeView(new StartPanel(ctx));
    }
}
