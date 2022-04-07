package it.polimi.ingsw.Exceptions;

public class NoParamaterException extends Exception{
    public NoParamaterException() {
        super("Varargs parameter is empty");
    }
}
