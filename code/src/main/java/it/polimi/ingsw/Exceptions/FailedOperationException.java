package it.polimi.ingsw.Exceptions;

public class FailedOperationException extends OperationException{
    public FailedOperationException(String operationName) {
        this(operationName,  "No further info.");
    }

    public FailedOperationException(String operationName, String additionalInfo) {
        super(operationName,  "could critically failed during execution." +
                "\nAdditional INFO: " + additionalInfo);
    }
}
