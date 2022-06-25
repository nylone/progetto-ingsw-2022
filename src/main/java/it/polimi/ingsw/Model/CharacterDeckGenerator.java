package it.polimi.ingsw.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Any {@link it.polimi.ingsw.Model.Enums.GameMode#ADVANCED} game must start with a selection of 3 different {@link CharacterCard}s
 * and this class is the generator of such cards. <br>
 */
public class CharacterDeckGenerator {
    /**
     * Generate a random list of 3 Character cards (ensured to be non repeating in the list)
     * @param context each generated {@link CharacterCard} needs a reference to the {@link Model} in order to work.
     * @return a list of 3 randomly generated cards, in no specific order.
     */
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
                .map(gen -> gen.build(context)).toList();
    }

    /**
     * in order to construct each card, the constructor must fall under an interface to be used in a lambda call.
     * This interface is just for that.
     */
    private interface CharacterCardGenerator {
        /**
         * the constructor of a {@link CharacterCard} has the same signature as this method
         * @param ctx the reference to {@link Model}
         * @return a {@link CharacterCard}
         */
        CharacterCard build(Model ctx);
    }
}
