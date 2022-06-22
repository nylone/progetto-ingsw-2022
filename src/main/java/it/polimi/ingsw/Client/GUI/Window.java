package it.polimi.ingsw.Client.GUI;

import javax.swing.*;
import java.awt.*;

import static it.polimi.ingsw.Client.GUI.IconLoader.logo;

/**
 * Window creates the top-level container (Java Swing component) onto which the game will be rendered
 */
public class Window {
    /**
     * Frame that will contain GUI's game version
     */
    private final JFrame frame;

    public Window() {
        //create frame and set its properties
        this.frame = new JFrame("Eriantys");
        frame.setMinimumSize(new Dimension(1080, 720));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /**
     * Remove all previous frame's contents and add a new JComponent
     *
     * @param newView Component that will be drawn inside the frame
     */
    public void changeView(JComponent newView) {
        frame.getContentPane().removeAll();
        frame.add(newView);
        frame.pack();
        frame.setIconImage(logo.getImage());
    }

    /**
     * @return GUI'S frame
     */
    public JFrame getFrame() {
        return frame;
    }
}
