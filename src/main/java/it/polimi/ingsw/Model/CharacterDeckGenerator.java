package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.Container.EmptyContainerException;
import it.polimi.ingsw.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CharacterDeckGenerator {
    public static List<CharacterCard> generateCardSet(Model context) {
        List<CharacterCardGenerator> deck = Arrays.asList(
                Card01::new,
                Card02::new,
                Card03::new,
                Card04::new,
                Card05::new,
                Card06::new,
                Card07::new,
                Card08::new,
                Card09::new,
                Card10::new,
                Card11::new,
                Card12::new);
        Collections.shuffle(deck, new Random(System.currentTimeMillis()));
        return deck.subList(0, 3).stream()
                .map(gen -> {
                    try {
                        return gen.build(context);
                    } catch (EmptyContainerException e) {
                        // should never happen
                        Logger.severe("student bag was found empty during game initialization. Critical, unrecoverable, error");
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

    private interface CharacterCardGenerator {
        CharacterCard build(Model ctx) throws EmptyContainerException;
    }
}
