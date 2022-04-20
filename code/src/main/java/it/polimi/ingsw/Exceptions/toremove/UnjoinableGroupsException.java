package it.polimi.ingsw.Exceptions.toremove;

public class UnjoinableGroupsException extends Exception{
    public UnjoinableGroupsException(){
        super("Given IslandGroups cannot be joined");
    }
}
