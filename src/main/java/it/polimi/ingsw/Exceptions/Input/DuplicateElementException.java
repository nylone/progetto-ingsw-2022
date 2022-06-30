package it.polimi.ingsw.Exceptions.Input;
/**
 * Signals there was an error while validating part of a contract. In this case the error is: an element was not unique in the
 * context of validation
 */
public class DuplicateElementException extends InputValidationException {
    /**
     * Creates an exception reporting a specific error and an identifier for the part of the failed contract
     * @param elementName the name of the field in the contract that failed validation
     */
    public DuplicateElementException(String elementName) {
        super(elementName, "element " + elementName + " was found to be duplicate.");
    }
}
