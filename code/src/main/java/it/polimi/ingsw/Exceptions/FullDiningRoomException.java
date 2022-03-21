package it.polimi.ingsw.Exceptions;

public class FullDiningRoomException extends Exception {
    public FullDiningRoomException() {
        super("No more space for that student in dining room");
    }
}
