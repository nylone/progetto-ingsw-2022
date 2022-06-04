package it.polimi.ingsw.Client.GUI;

import javax.swing.*;
import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.Logo;

/**
 * Window creates the top-level container (Java Swing component) onto which the game will be rendered
 */
public class Window {
    private final JFrame frame;

    public Window() {
        this.frame = new JFrame("Eriantys");
        frame.setMinimumSize(new Dimension(1080, 720));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public void changeView(JComponent newView) {
        frame.getContentPane().removeAll();
        frame.add(newView);
        frame.pack();
        frame.setIconImage(Logo.getImage());
    }
}
