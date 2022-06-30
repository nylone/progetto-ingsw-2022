package it.polimi.ingsw.Exceptions.Operation;

/**
 * Signals there was an error while executing of an operation.
 */
public class FailedOperationException extends OperationException {
    /**
     * Creates an exception reporting a specific error and an identifier for the failed operation
     *
     * @param operationName  the name of the operation in question
     * @param additionalInfo a short description of the error
     */
    public FailedOperationException(String operationName, String additionalInfo) {
        super(operationName, "could critically failed during execution." +
                "\nAdditional INFO: " + additionalInfo);
    }
}
