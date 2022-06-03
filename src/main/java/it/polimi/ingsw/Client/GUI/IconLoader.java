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

    public static final ImageIcon coin = loadIcon("Coin", 150, 160);

    public static final ImageIcon card01 = loadIcon("CharacterCards/Card01", 205, 340);

    public static final ImageIcon card02 = loadIcon("CharacterCards/Card02", 205, 340);

    public static final ImageIcon card03 = loadIcon("CharacterCards/Card03", 205, 340);

    public static final ImageIcon card04 = loadIcon("CharacterCards/Card04", 205, 340);

    public static final ImageIcon card05 = loadIcon("CharacterCards/Card05", 205, 340);

    public static final ImageIcon card06 = loadIcon("CharacterCards/Card06", 205, 340);

    public static final ImageIcon card07 = loadIcon("CharacterCards/Card07", 205, 340);

    public static final ImageIcon card08 = loadIcon("CharacterCards/Card08", 205, 340);

    public static final ImageIcon card09 = loadIcon("CharacterCards/Card09", 205, 340);

    public static final ImageIcon card10 = loadIcon("CharacterCards/Card10", 205, 340);

    public static final ImageIcon card11 = loadIcon("CharacterCards/Card11", 205, 340);

    public static final ImageIcon card12 = loadIcon("CharacterCards/Card12", 205, 340);

    public static final ImageIcon playerBoard = loadIcon("player-board", 1080, 420);

    public static final ImageIcon playerBoardBackground = loadIcon("playerboardBG", 1080, 720);

    public static final ImageIcon assistantCard1 = loadIcon("AssistantCards/Assistant1", 165, 255);

    public static final ImageIcon assistantCard2 = loadIcon("AssistantCards/Assistant2", 165, 255);

    public static final ImageIcon assistantCard3 = loadIcon("AssistantCards/Assistant3", 165, 255);

    public static final ImageIcon assistantCard4 = loadIcon("AssistantCards/Assistant4", 165, 255);

    public static final ImageIcon assistantCard5 = loadIcon("AssistantCards/Assistant5", 165, 255);

    public static final ImageIcon assistantCard6 = loadIcon("AssistantCards/Assistant6", 165, 255);

    public static final ImageIcon assistantCard7 = loadIcon("AssistantCards/Assistant7", 165, 255);

    public static final ImageIcon assistantCard8 = loadIcon("AssistantCards/Assistant8", 165, 255);

    public static final ImageIcon assistantCard9 = loadIcon("AssistantCards/Assistant9", 165, 255);

    public static final ImageIcon assistantCard10 = loadIcon("AssistantCards/Assistant10", 165, 255);

    public static final ImageIcon RedStudent = loadIcon("Students/RedStudent", 50, 45);

    public static final ImageIcon BlueStudent = loadIcon("Students/BlueStudent", 50, 45);

    public static final ImageIcon GreenStudent = loadIcon("Students/GreenStudent", 50, 45);

    public static final ImageIcon YellowStudent = loadIcon("Students/YellowStudent", 50, 45);

    public static final ImageIcon PinkStudent = loadIcon("Students/PinkStudent", 50, 45);

    public static final ImageIcon RedTeacher = loadIcon("Teachers/RedTeacher", 50, 45);

    public static final ImageIcon BlueTeacher = loadIcon("Teachers/BlueTeacher", 50, 45);

    public static final ImageIcon YellowTeacher = loadIcon("Teachers/YellowTeacher", 50, 45);

    public static final ImageIcon PinkTeacher = loadIcon("Teachers/PinkTeacher", 50, 45);

    public static final ImageIcon GreenTeacher = loadIcon("Teachers/GreenTeacher", 50, 45);

    public static final ImageIcon GrayTower = loadIcon("Towers/grayTower", 50, 75);

    public static final ImageIcon BlackTower = loadIcon("Towers/blackTower", 50, 75);

    public static final ImageIcon WhiteTower = loadIcon("Towers/whiteTower", 50, 75);

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
