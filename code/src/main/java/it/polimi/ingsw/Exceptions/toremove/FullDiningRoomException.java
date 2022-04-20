package it.polimi.ingsw.Exceptions.toremove;

public class FullDiningRoomException extends Exception {
    public FullDiningRoomException() {
        super("No more space for that student in dining room");
    }
}
