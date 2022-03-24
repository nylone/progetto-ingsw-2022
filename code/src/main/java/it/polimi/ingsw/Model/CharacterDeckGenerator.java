package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CharacterDeckGenerator {
    private static ArrayList<CharacterCard> deck;

    public static List<CharacterCard> generateCardSet() {
        if (deck == null) {
            // todo generate cards for the singleton
        }
        Collections.shuffle(deck, new Random(System.currentTimeMillis()));
        return deck.subList(0, 2);
    }
}
