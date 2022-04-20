package it.polimi.ingsw.Exceptions;

public abstract class OperationException extends Exception{
    public OperationException(String operationName, String errorReport) {
        super("An error occurred while running the following operation: " + operationName +
                "\nThe error was: " + errorReport);
    }
}
