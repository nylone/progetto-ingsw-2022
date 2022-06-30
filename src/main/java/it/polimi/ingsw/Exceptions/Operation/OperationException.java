package it.polimi.ingsw.Exceptions.Operation;

/**
 * Signals there was an error while executing or attempting to start execution of an operation.
 */
public abstract class OperationException extends Exception {
    /**
     * Creates an exception reporting a specific error and an identifier for the failed operation
     * @param operationName the name of the operation in question
     * @param errorReport a short description of the error
     */
    public OperationException(String operationName, String errorReport) {
        super("An error occurred while running the following operation: " + operationName +
                "\nThe error was: " + errorReport);
    }
}
