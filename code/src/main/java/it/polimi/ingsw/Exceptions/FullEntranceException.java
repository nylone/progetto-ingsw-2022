package it.polimi.ingsw.Exceptions;

public class FullEntranceException extends Exception{
    public FullEntranceException(){
        super("No more space in entrance");
    }
}
