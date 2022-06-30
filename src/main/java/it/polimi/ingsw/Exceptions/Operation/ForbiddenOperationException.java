package it.polimi.ingsw.Exceptions.Operation;

/**
 * Signals there was an error while attempting to carry out an operation. This is different from any {@link it.polimi.ingsw.Exceptions.Input.InputValidationException}
 * in the sense that the operation is thought to be "generally allowed" under most normal circumstances, but some
 * form of verification was required to prevent potential unrecoverable errors in the code.
 */
public class ForbiddenOperationException extends OperationException {
    /**
     * Creates an exception reporting a specific error and an identifier for the failed operation
     * @param operationName the name of the operation in question
     * @param additionalInfo a short description of the error
     */
    public ForbiddenOperationException(String operationName, String additionalInfo) {
        super(operationName, "could not be executed as it was forbidden." +
                "\nAdditional INFO: " + additionalInfo);
    }
}
