package it.polimi.ingsw.Exceptions;

public class FullContainerException extends ContainerException {
    public FullContainerException(String containerName) {
        super(containerName, containerName + " was found full.");
    }
}
