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

    public static final ImageIcon sky = loadIcon("sky", 1080, 720);

    public static final ImageIcon card01 = loadIcon("CharacterCards/Card01", 198, 340);

    public static final ImageIcon card02 = loadIcon("CharacterCards/Card02", 198, 340);

    public static final ImageIcon card03 = loadIcon("CharacterCards/Card03", 198, 340);

    public static final ImageIcon card04 = loadIcon("CharacterCards/Card04", 198, 340);

    public static final ImageIcon card05 = loadIcon("CharacterCards/Card05", 198, 340);

    public static final ImageIcon card06 = loadIcon("CharacterCards/Card06", 198, 340);

    public static final ImageIcon card07 = loadIcon("CharacterCards/Card07", 198, 340);

    public static final ImageIcon card08 = loadIcon("CharacterCards/Card08", 198, 340);

    public static final ImageIcon card09 = loadIcon("CharacterCards/Card09", 198, 340);

    public static final ImageIcon card10 = loadIcon("CharacterCards/Card10", 198, 340);

    public static final ImageIcon card11 = loadIcon("CharacterCards/Card11", 198, 340);

    public static final ImageIcon card12 = loadIcon("CharacterCards/Card12", 198, 340);

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
