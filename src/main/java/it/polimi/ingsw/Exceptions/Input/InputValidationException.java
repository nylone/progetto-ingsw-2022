package it.polimi.ingsw.Exceptions.Input;

public abstract class InputValidationException extends Exception {
    public InputValidationException(String failedValidationName, String errorReport) {
        super("An error occurred while validating: " + failedValidationName +
                "\nThe error was: " + errorReport);
    }
}
