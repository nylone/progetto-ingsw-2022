package it.polimi.ingsw.RemoteView.Messages.ClientEvents;

import it.polimi.ingsw.Model.Enums.GameMode;
import it.polimi.ingsw.RemoteView.Messages.MessageBuilder;
import it.polimi.ingsw.RemoteView.Messages.PayloadType;

public class StartGame extends ClientEvent implements MessageBuilder {
    private final GameMode gameMode;

    public StartGame(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.REQUEST_START_GAME;
    }
}