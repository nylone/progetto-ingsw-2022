package it.polimi.ingsw.Client.GUI.Panels;


import it.polimi.ingsw.Client.GUI.Components.StudentButton;
import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.Model.Enums.PawnColour;
import it.polimi.ingsw.Model.Model;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class CloudPanelTest {

    public JFrame createScreen() {
        JFrame frame = new JFrame("Eriantys");
        frame.setMinimumSize(new Dimension(1080, 720));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        return frame;
    }

    public void changeView(JComponent newView, JFrame frame) {
        frame.getContentPane().removeAll();
        frame.add(newView);
        frame.pack();
    }

   @Test
   public void cloudGUITest() throws InterruptedException {
       Model ctx = new Model(GameMode.SIMPLE, "ale", "teo", "ari");
       CloudPanel clouds = new CloudPanel(ctx.getClouds());
       changeView(clouds, createScreen());
       Thread.sleep(10000);
   }
   @Test
   public void islandGUITest() throws InterruptedException {
       Model ctx = new Model(GameMode.SIMPLE, "ale", "teo", "ari");
       IslandPanel island = new IslandPanel(ctx.getMutableIslandField());
       changeView(island, createScreen());
       Thread.sleep(600000);
   }
   @Test
   public void studentGUITest() throws InterruptedException {
       Model ctx = new Model(GameMode.SIMPLE, "ale", "teo", "ari");
       StudentButton student = new StudentButton(PawnColour.BLUE, 5);
       changeView(student, createScreen());
       Thread.sleep(600000);
   }
}