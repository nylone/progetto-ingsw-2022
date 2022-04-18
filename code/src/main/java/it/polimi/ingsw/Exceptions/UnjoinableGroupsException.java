package it.polimi.ingsw.Exceptions;

public class UnjoinableGroupsException extends Exception{
    public UnjoinableGroupsException(){
        super("Given IslandGroups cannot be joined");
    }
}
