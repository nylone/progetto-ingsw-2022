package it.polimi.ingsw.Exceptions;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {
        super("Invalid input provided");
    }

    public InvalidInputException(String message) { super(message); }
}
