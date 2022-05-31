package it.polimi.ingsw.Client.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class IconLoader {
    public static final ImageIcon showPassIcon = loadIcon("eye", 12, 12);
    public static final ImageIcon hidePassIcon = loadIcon("eye-barred", 12, 12);
    public static final ImageIcon playerBoardIcon = loadIcon("player-board", 1660, 720);

    private static ImageIcon loadIcon(String name, int width, int height) {
        Image img;
        try {
            img = ImageIO.read(Objects.requireNonNull(IconLoader.class.getResource("/icons/" + name + ".png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}
