package it.polimi.ingsw.Exceptions.toremove;

public class DuplicateAssistantCardException extends Exception{

    public DuplicateAssistantCardException(){
        super("This Assistant Card has already been used");
    }
}
