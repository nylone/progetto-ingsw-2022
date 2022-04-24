package it.polimi.ingsw.Exceptions.Input;

public class GenericInputValidationException extends InputValidationException {
    public GenericInputValidationException(String elementName, String errorReport) {
        super(elementName, errorReport);
    }
}
