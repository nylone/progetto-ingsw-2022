package it.polimi.ingsw.Exceptions.Container;

/**
 * Signals there was an error in the container part or function of an object. The error has to do with the container
 * being accessed at an invalid index or key.
 */
public class InvalidContainerIndexException extends ContainerException {
    /**
     * Creates an exception with the name of the affected container and a default report
     * @param containerName a string identifying the container
     */
    public InvalidContainerIndexException(String containerName) {
        super(containerName, "provided index is out of bounds or no valid value could be retrieved.");
    }
}
