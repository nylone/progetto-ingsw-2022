package it.polimi.ingsw.Exceptions.Operation;

public class ForbiddenOperationException extends OperationException {
    public ForbiddenOperationException(String operationName) {
        this(operationName, "No further info.");
    }

    public ForbiddenOperationException(String operationName, String additionalInfo) {
        super(operationName, "could not be executed as it was forbidden." +
                "\nAdditional INFO: " + additionalInfo);
    }
}
