package it.polimi.ingsw.Exceptions;

public abstract class ContainerException extends Exception{
    public ContainerException(String containerName, String errorReport) {
        super("An error occurred on: " + containerName +
                "\nThe error was: " + errorReport);
    }
}

