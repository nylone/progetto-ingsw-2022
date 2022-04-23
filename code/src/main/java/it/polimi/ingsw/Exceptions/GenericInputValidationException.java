package it.polimi.ingsw.Exceptions;

public class GenericInputValidationException extends InputValidationException{
    public GenericInputValidationException(String elementName, String errorReport) {
        super(elementName, errorReport);
    }
}
