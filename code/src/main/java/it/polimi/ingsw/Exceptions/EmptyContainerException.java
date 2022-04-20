package it.polimi.ingsw.Exceptions;

public class EmptyContainerException extends ContainerException {
    public EmptyContainerException(String containerName) {
        super(containerName,  containerName + " was found empty.");
    }
}
