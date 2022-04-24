package it.polimi.ingsw.Exceptions;

public class InvalidContainerIndexException extends ContainerException {
    public InvalidContainerIndexException(String containerName) {
        super(containerName, "provided index is out of bounds or no valid value could be retrieved.");
    }
}
