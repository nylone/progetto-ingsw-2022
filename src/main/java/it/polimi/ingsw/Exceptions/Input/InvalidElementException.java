package it.polimi.ingsw.Exceptions.Input;

/**
 * Signals there was error an while validating part of a contract. In this case the error is: an element was invalid in the
 * context of validation
 */
public class InvalidElementException extends InputValidationException {
    /**
     * Creates an exception reporting a specific error and an identifier for the part of the failed contract
     *
     * @param elementName the name of the field in the contract that failed validation
     */
    public InvalidElementException(String elementName) {
        super(elementName, "element " + elementName +
                " was found to be invalid (eg: null, out of bounds or otherwise incorrect).");
    }
}
