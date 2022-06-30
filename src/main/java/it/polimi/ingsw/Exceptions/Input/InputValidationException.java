package it.polimi.ingsw.Exceptions.Input;

/**
 * Signals there was an error while validating part of a contract.
 */
public abstract class InputValidationException extends Exception {
    /**
     * Creates an exception reporting a specific error and an identifier for the part of the failed contract
     * @param failedValidationName the name of the field in the contract that failed validation
     * @param errorReport a short description of the error
     */
    public InputValidationException(String failedValidationName, String errorReport) {
        super("An error occurred while validating: " + failedValidationName +
                "\nThe error was: " + errorReport);
    }
}
