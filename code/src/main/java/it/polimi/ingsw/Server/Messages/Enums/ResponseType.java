package it.polimi.ingsw.Server.Messages.Enums;

public enum ResponseType {
    /**
     * Sent on accept of the welcome server. <br>
     * Once sent, carries an empty body object.
     * This response only means the client is being handled by the lobby server, which is now waiting for a
     * {@link it.polimi.ingsw.Server.Messages.ClientMessages.DeclarePlayer} request.
     */
    WELCOME_SERVER_ACCEPT,
    /**
     * Sent on accept of the lobby server. <br>
     * Once sent, carries an empty body object.
     * This response means the client is now registered on the lobby server, and should select whether to
     * join a game or create one. <br>
     * To join a game use {@link it.polimi.ingsw.Server.Messages.ClientMessages.JoinGame} <br>
     * To create a game use {@link it.polimi.ingsw.Server.Messages.ClientMessages.CreateGame}
     */
    LOBBY_SERVER_ACCEPT,
    /**
     * Sent on redirect of the lobby server. <br>
     * Once sent, carries an ??? body object. //TODO
     * This response means the client has now entered a lobby. Depending on the {@link it.polimi.ingsw.Server.Messages.ServerMessages.ResponseBody}
     * the lobby may be on hold or the game may have already started. <br>
     * If the lobby was ever on hold, the client should wait for a
     * {@link it.polimi.ingsw.Server.Messages.ServerMessages.GameStart} response.
     */
    LOBBY_SERVER_REDIRECT,
}
