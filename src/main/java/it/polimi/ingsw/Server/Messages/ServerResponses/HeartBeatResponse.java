package it.polimi.ingsw.Server.Messages.ServerResponses;

import it.polimi.ingsw.Server.Messages.Events.Requests.ClientRequest;
import it.polimi.ingsw.Server.Messages.ServerResponses.SupportStructures.StatusCode;

import java.io.Serial;

public class HeartBeatResponse extends FixedStatusResponse {
    @Serial
    private static final long serialVersionUID = 357L;
}
