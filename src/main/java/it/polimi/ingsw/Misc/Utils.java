package it.polimi.ingsw.Misc;

import it.polimi.ingsw.Exceptions.Input.InvalidElementException;

import java.util.*;

/**
 * This class contains various static functions useful for more than one purpose inside the code.
 * Check the docs of each function for more info.
 */
public class Utils {
    /**
     * Given a {@link List}, shuffles it in place with a random seed
     *
     * @param coll the list to shuffle
     * @param <T>  the type of the elements of the list
     */
    public static <T> void shuffle(List<T> coll) {
        Collections.shuffle(coll, new Random(System.currentTimeMillis()));
    }

    /**
     * Given a {@link List}, returns a random element from it
     *
     * @param list the list to fetch from
     * @param <T>  the type of the list's elements
     * @return a random element from the list
     */
    public static <T> T random(List<T> list) {
        Random random = new Random();
        int randomNumber = random.nextInt(list.size());
        return list.get(randomNumber);
    }

    /**
     * Selects an element in the list as if it was a circular array
     *
     * @param startingElement the element from which to start counting forwards
     * @param group           the list to fetch from
     * @param movement        the amount of steps forward from the starting element to take in order to reach the fetched element
     * @param <T>             the type of elements of the group
     * @return an element from the group
     * @throws InvalidElementException if startingElement is not present in the list
     */
    public static <T> T modularSelection(T startingElement, List<T> group, int movement) throws InvalidElementException {
        int index = group.indexOf(startingElement);
        if (index == -1) {
            throw new InvalidElementException("starting element");
        }
        return group.get((index + group.size() + movement) % group.size());
    }

    /**
     * Given two maps, evaluate whether the second map contains values that can fit the first map
     *
     * @param originalSet    the first map is the "original"
     * @param possibleSubset the second map is a "possible subset" of the original
     * @param <T>            the key of both maps
     * @param <C>            an element extending the Integer class as the value of both maps
     * @return true if the second map is a possible subset of the original, meaning the values for each key in the possible subset
     * are lower than the values for the same key in the original map. otherwise returns false
     */
    public static <T, C extends Integer> boolean canMapFit(Map<T, C> originalSet, Map<T, C> possibleSubset) {
        for (Map.Entry<T, C> entry : possibleSubset.entrySet()) {
            T key = entry.getKey();
            int count = entry.getValue();
            if (!originalSet.containsKey(key) || ((int) originalSet.get(key) - count < 0)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Given a list, find occurrences of elements of the same class within it.
     *
     * @param selected the class object to search for in the list
     * @param list     the list to search in
     * @param <T>      the elements of the list
     * @param <E>      an inheritor of the class of elements contained in the list
     * @return the count of how many elements of the list match the input class
     */
    public static <T, E extends T> int countSimilarClassOccurrences(Class<E> selected, List<T> list) {
        return (int) list.stream().filter(pa -> pa.getClass() == selected).count();
    }

    /**
     * Given a {@link List}, sorts its elements in order of frequency from lowest to highest
     *
     * @param array the list to sort
     * @param <T>   the type of the elements in the list
     * @return a new {@link List}, containing the elements of the previous one sorted by frequency
     */
    public static <T> List<T> sortByFrequency(List<T> array) {
        Map<T, ArrayList<T>> frequencyMap = new HashMap<>();
        for (T current : array) {
            ArrayList<T> list = frequencyMap.getOrDefault(current, new ArrayList<>());
            list.add(current);
            frequencyMap.put(current, list);
        }
        ArrayList<T> sorted = new ArrayList<>();
        frequencyMap.entrySet().stream()
                .sorted(Comparator.comparingInt(o -> o.getValue().size()))
                .map(Map.Entry::getValue)
                .forEach(sorted::addAll);
        Collections.reverse(sorted);
        return sorted;
    }
}