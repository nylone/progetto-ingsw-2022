package it.polimi.ingsw.RemoteView.Messages;

import java.io.Serial;
import java.io.Serializable;

public abstract class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 300L; // convention: 3 for network, (01 -> 99) for objects
}