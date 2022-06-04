package it.polimi.ingsw.Client.GUI.Listeners;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ClickListener extends MouseAdapter {

    JLabel component;
    ImageIcon image;
    Rectangle clickableArea;

    public ClickListener(JLabel component, Rectangle clickableArea) {
        this.component = component;
        this.image = (ImageIcon) component.getIcon();
        this.clickableArea = clickableArea;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        component.setIcon(image);

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (clickableArea.contains(e.getX(), e.getY())) {
            component.setIcon(new ImageIcon(GrayFilter.createDisabledImage(image.getImage())));
        }
    }
}
