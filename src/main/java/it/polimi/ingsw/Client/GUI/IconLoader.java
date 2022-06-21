package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class IconLoader {
    public static final ImageIcon logo = loadIcon("IconEriantys.png", 600, 600);
    public static final ImageIcon motherNature = loadIcon("motherNature.png", 50, 75);
    public static final ImageIcon cloudIcon = loadIcon("cloud.png", 1300, 700);
    public static final ImageIcon sky = loadIcon("sky.png", 1080, 720);
    public static final ImageIcon coin = loadIcon("Coin.png", 150, 160);
    public static final ImageIcon card01 = loadIcon("CharacterCards/Card01.png", 205, 340);
    public static final ImageIcon card02 = loadIcon("CharacterCards/Card02.png", 205, 340);
    public static final ImageIcon card03 = loadIcon("CharacterCards/Card03.png", 205, 340);
    public static final ImageIcon card04 = loadIcon("CharacterCards/Card04.png", 205, 340);
    public static final ImageIcon card05 = loadIcon("CharacterCards/Card05.png", 205, 340);
    public static final ImageIcon card06 = loadIcon("CharacterCards/Card06.png", 205, 340);
    public static final ImageIcon card07 = loadIcon("CharacterCards/Card07.png", 205, 340);
    public static final ImageIcon card08 = loadIcon("CharacterCards/Card08.png", 205, 340);
    public static final ImageIcon card09 = loadIcon("CharacterCards/Card09.png", 205, 340);
    public static final ImageIcon card10 = loadIcon("CharacterCards/Card10.png", 205, 340);
    public static final ImageIcon card11 = loadIcon("CharacterCards/Card11.png", 205, 340);
    public static final ImageIcon card12 = loadIcon("CharacterCards/Card12.png", 205, 340);
    public static final ImageIcon playerBoard = loadIcon("player-board.png", 1080, 420);
    public static final ImageIcon playerBoardBackground = loadIcon("playerboardBG.png", 1080, 720);
    public static final ImageIcon assistantCard1 = loadIcon("AssistantCards/Assistant1.png", 165, 255);
    public static final ImageIcon assistantCard2 = loadIcon("AssistantCards/Assistant2.png", 165, 255);
    public static final ImageIcon assistantCard3 = loadIcon("AssistantCards/Assistant3.png", 165, 255);
    public static final ImageIcon assistantCard4 = loadIcon("AssistantCards/Assistant4.png", 165, 255);
    public static final ImageIcon assistantCard5 = loadIcon("AssistantCards/Assistant5.png", 165, 255);
    public static final ImageIcon assistantCard6 = loadIcon("AssistantCards/Assistant6.png", 165, 255);
    public static final ImageIcon assistantCard7 = loadIcon("AssistantCards/Assistant7.png", 165, 255);
    public static final ImageIcon assistantCard8 = loadIcon("AssistantCards/Assistant8.png", 165, 255);
    public static final ImageIcon assistantCard9 = loadIcon("AssistantCards/Assistant9.png", 165, 255);
    public static final ImageIcon assistantCard10 = loadIcon("AssistantCards/Assistant10.png", 165, 255);
    public static final ImageIcon RedStudent = loadIcon("Students/RedStudent.png", 50, 45);
    public static final ImageIcon BlueStudent = loadIcon("Students/BlueStudent.png", 50, 45);
    public static final ImageIcon GreenStudent = loadIcon("Students/GreenStudent.png", 50, 45);
    public static final ImageIcon YellowStudent = loadIcon("Students/YellowStudent.png", 50, 45);
    public static final ImageIcon PinkStudent = loadIcon("Students/PinkStudent.png", 50, 45);
    public static final ImageIcon RedTeacher = loadIcon("Teachers/RedTeacher.png", 50, 45);
    public static final ImageIcon BlueTeacher = loadIcon("Teachers/BlueTeacher.png", 50, 45);
    public static final ImageIcon YellowTeacher = loadIcon("Teachers/YellowTeacher.png", 50, 45);
    public static final ImageIcon PinkTeacher = loadIcon("Teachers/PinkTeacher.png", 50, 45);
    public static final ImageIcon GreenTeacher = loadIcon("Teachers/GreenTeacher.png", 50, 45);
    public static final ImageIcon GrayTower = loadIcon("Towers/grayTower.png", 50, 75);
    public static final ImageIcon BlackTower = loadIcon("Towers/blackTower.png", 50, 75);
    public static final ImageIcon WhiteTower = loadIcon("Towers/whiteTower.png", 50, 75);
    public static final ImageIcon Island1 = loadIcon("Islands/Island1.png", 300, 210);
    public static final ImageIcon Island2 = loadIcon("Islands/Island2.png", 300, 210);
    public static final ImageIcon Island3 = loadIcon("Islands/Island3.png", 300, 210);
    public static final ImageIcon noEntryTileIcon = loadIcon("noEntryTile.png", 52, 60);

    public static final ImageIcon winnerText = loadIcon("winner.png", 523, 120);

    private static ImageIcon loadIcon(String name, int width, int height) {
        Image img;
        try {
            img = ImageIO.read(Objects.requireNonNull(IconLoader.class.getResource("/icons/" + name)));
        } catch (IOException e) {
            Logger.severe("Could not load image " + name);
            e.printStackTrace();
            return null;
        }
        return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}
