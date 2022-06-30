package it.polimi.ingsw.Exceptions.Input;

/**
 * Signals there was an error while validating part of a contract. In this case the error is defined by the thrower.
 */
public class GenericInputValidationException extends InputValidationException {
    /**
     * Creates an exception reporting a specific error and an identifier for the part of the failed contract
     * @param elementName the name of the field in the contract that failed validation
     * @param errorReport a short description of the error
     */
    public GenericInputValidationException(String elementName, String errorReport) {
        super(elementName, errorReport);
    }
}
