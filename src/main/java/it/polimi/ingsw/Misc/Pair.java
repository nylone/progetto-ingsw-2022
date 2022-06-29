package it.polimi.ingsw.Misc;

import java.io.Serializable;

/**
 * A Pair represents a set of 2 objects linked in some way
 *
 * @param first  the first element of the pair
 * @param second the second element of the pair
 * @param <T>    the first element's type
 * @param <U>    the second element's type
 */
public record Pair<T, U>(T first, U second) implements Serializable {

    /*//test-purpose only
    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
    //*/
}
