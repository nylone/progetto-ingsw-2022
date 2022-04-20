package it.polimi.ingsw.Exceptions.toremove;

public class NoPawnInCloudException extends Exception {
    public NoPawnInCloudException() {
        super("Cloud is empty");
    }
}
