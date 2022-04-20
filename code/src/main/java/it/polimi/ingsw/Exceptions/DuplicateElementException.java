package it.polimi.ingsw.Exceptions;

public class DuplicateElementException extends InputValidationException{
    public DuplicateElementException(String elementName) {
        super(elementName, "element " + elementName + " was found to be duplicate.");
    }
}
