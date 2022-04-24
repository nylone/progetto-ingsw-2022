package it.polimi.ingsw.Exceptions.Container;

public class InvalidContainerIndexException extends ContainerException {
    public InvalidContainerIndexException(String containerName) {
        super(containerName, "provided index is out of bounds or no valid value could be retrieved.");
    }
}
