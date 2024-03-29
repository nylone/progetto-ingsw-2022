package it.polimi.ingsw.Exceptions.Container;

/**
 * Signals there was an error in the container part or function of an object. The error has to do with the container
 * being empty.
 */
public class EmptyContainerException extends ContainerException {
    /**
     * Creates an exception with the name of the affected container and a default report
     *
     * @param containerName a string identifying the container
     */
    public EmptyContainerException(String containerName) {
        super(containerName, containerName + " was found empty.");
    }
}
