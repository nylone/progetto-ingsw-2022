package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Panels.StartPanel;

import javax.swing.*;

/**
 * Run game's gui version by creating a new {@link Context} and a new {@link Window}
 */
public class GUI {
    public static void main(String... args) {
        Context ctx = new Context();
        ctx.setWindow(new Window());
        new StartPanel(ctx);
    }
}
