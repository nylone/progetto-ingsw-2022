package it.polimi.ingsw.Server.Messages.ServerMessages;

import it.polimi.ingsw.Server.Messages.Enums.StatusCode;
import it.polimi.ingsw.Server.Messages.Message;

public class ConnectionResult implements Message {
    StatusCode status;

    public ConnectionResult(StatusCode status) {
        this.status = status;
    }

    @Override
    public StatusCode getStatusCode() {
        return status;
    }

    @Override
    public String getHumanMeaning() {
        return switch (this.status) {
            case Success -> "connection success";
            case Fail -> "connection failed";
            case Query -> "connection unknown";
        };
    }
}
