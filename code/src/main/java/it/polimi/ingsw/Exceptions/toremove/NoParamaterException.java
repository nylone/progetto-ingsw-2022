package it.polimi.ingsw.Exceptions.toremove;

public class NoParamaterException extends Exception{
    public NoParamaterException() {
        super("Varargs parameter is empty");
    }
}
