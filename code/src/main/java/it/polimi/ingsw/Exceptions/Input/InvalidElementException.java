package it.polimi.ingsw.Exceptions.Input;

public class InvalidElementException extends InputValidationException {
    public InvalidElementException(String elementName) {
        super(elementName, "element " + elementName +
                " was found to be invalid (eg: null, out of bounds or otherwise incorrect).");
    }
}
