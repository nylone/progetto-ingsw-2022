package it.polimi.ingsw.Exceptions;

public class NoPawnInCloudException extends Exception {
    public NoPawnInCloudException() {
        super("Cloud is empty");
    }
}
