package it.polimi.ingsw.Client.GUI;

import javax.swing.*;

public class PopupMessage {
    public PopupMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
