package it.polimi.ingsw.Server.Messages.ServerResponses;

import java.io.Serial;

/**
 * A {@link Response} used by the server to notify the client a {@link it.polimi.ingsw.Server.Messages.Events.Requests.ClientRequest}
 * was uninterpretable.
 */
public class InvalidRequest extends FixedStatusResponse {
    @Serial
    private static final long serialVersionUID = 307L;
}
