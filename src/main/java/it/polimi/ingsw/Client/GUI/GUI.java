package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Panels.StartPanel;

import javax.swing.*;

public class GUI {
    public static void init() {
        SwingUtilities.invokeLater(GUI::start);
    }

    private static void start() {
        Context ctx = new Context();
        ctx.setWindow(new Window());
        new StartPanel(ctx);
    }
}
