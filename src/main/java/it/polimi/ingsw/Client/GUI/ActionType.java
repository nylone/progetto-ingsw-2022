package it.polimi.ingsw.Client.GUI;

/**
 * Enum used to represent islandFieldPanel's status
 * <p>MOVESTUDENT --> islandFieldPanel is waiting for user to click on an island to move the student from playerBoard</p>
 * <p>MOVEMOTHERNATURE --> islandFieldPanel is waiting for user to click on an island to move motherNature</p>
 * <p>CHARACTERCARD --> islandFieldPanel is waiting for user to click on an island to apply selected characterCard's effect</p>
 */
public enum ActionType {

    MOVESTUDENT, MOVEMOTHERNATURE, CHARACTERCARD
}
