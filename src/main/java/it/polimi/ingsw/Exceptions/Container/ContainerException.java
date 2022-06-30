package it.polimi.ingsw.Exceptions.Container;

/**
 * Signals there was an error in the container part or function of an object
 */
public abstract class ContainerException extends Exception {
    /**
     * Creates an exception with the name of the affected container and a report
     * @param containerName a string identifying the container
     * @param errorReport a report of the error
     */
    public ContainerException(String containerName, String errorReport) {
        super("An error occurred on: " + containerName +
                "\nThe error was: " + errorReport);
    }
}

