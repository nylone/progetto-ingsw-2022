package it.polimi.ingsw.Exceptions.Container;

public class EmptyContainerException extends ContainerException {
    public EmptyContainerException(String containerName) {
        super(containerName, containerName + " was found empty.");
    }
}
