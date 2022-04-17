package it.polimi.ingsw.Exceptions;

public class DuplicateAssistantCardException extends Exception{

    public DuplicateAssistantCardException(){
        super("This Assistant Card has already been used");
    }
}
