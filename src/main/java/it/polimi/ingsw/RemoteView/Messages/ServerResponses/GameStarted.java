package it.polimi.ingsw.RemoteView.Messages.ServerResponses;

import it.polimi.ingsw.RemoteView.Messages.PayloadType;

import static it.polimi.ingsw.RemoteView.Messages.PayloadType.RESPONSE_GAME_STARTED;

public class GameStarted extends Response {
    public GameStarted() {
        super(StatusCode.Success);
    }

    @Override
    public PayloadType getPayloadType() {
        return RESPONSE_GAME_STARTED;
    }
}
