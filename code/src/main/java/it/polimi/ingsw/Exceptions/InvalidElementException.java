package it.polimi.ingsw.Exceptions;

public class InvalidElementException extends InputValidationException {
    public InvalidElementException(String elementName) {
        super(elementName, "element " + elementName + " was found to be invalid.");
    }
}
