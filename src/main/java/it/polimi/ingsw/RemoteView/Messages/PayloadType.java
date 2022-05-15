package it.polimi.ingsw.RemoteView.Messages;

import it.polimi.ingsw.RemoteView.Messages.Events.Requests.DeclarePlayerRequest;

public enum PayloadType {
    // RESPONSES
    /**
     * Sent on an invalid request. <br>
     * Once sent, carries an empty body object.
     * This response only means the client made an illegal request that cannot be processed at this time
     * by the server.
     */
    RESPONSE_INVALID_REQUEST,
    /**
     * Sent on accept of the welcome server. <br>
     * Once sent, carries an empty body object.
     * This response only means the client is being handled by the lobby server, which is now waiting for a
     * {@link DeclarePlayerRequest} request.
     */
    RESPONSE_WELCOME,
    /**
     * Sent on accept of the lobby server. <br>
     * Once sent, carries an empty body object.
     * This response means the client is now registered on the lobby server, and should select whether to
     * join a game or create one. <br>
     * To join a game use {@link it.polimi.ingsw.RemoteView.Messages.Events.Requests.ConnectLobbyRequest} <br>
     * To create a game use {@link it.polimi.ingsw.RemoteView.Messages.Events.Requests.CreateLobbyRequest}
     */
    RESPONSE_LOBBY_ACCEPT,
    /**
     * Sent on redirect of the lobby server. <br>
     * Once sent, carries an empty body object. // todo
     * This response means the client has now entered a lobby. Depending on the {@link it.polimi.ingsw.RemoteView.Messages.ServerResponses.Response}
     * the lobby may be on hold or the game may have already started. <br>
     * If the lobby was ever on hold, the client should wait for a
     * {@link it.polimi.ingsw.RemoteView.Messages.ServerResponses.GameStarted} response.
     */
    RESPONSE_LOBBY_REDIRECT,
    RESPONSE_GAME_INIT,
    RESPONSE_GAME_STARTED,
    RESPONSE_CLIENT_CONNECTED,
    RESPONSE_CLIENT_DISCONNECTED,
    RESPONSE_MODEL_UPDATED,
    RESPONSE_PLAYER_ACTION,

    RESPONSE_PLAYER_ACTION_FEEDBACK,


    // REQUESTS
    /**
     * Sent on accept of the welcome server. <br>
     * Once sent, carries an empty body object.
     * This response only means the client is being handled by the lobby server, which is now waiting for a
     * {@link DeclarePlayerRequest} request.
     */
    REQUEST_DECLARE_PLAYER,
    /**
     * Sent on accept of the lobby server. <br>
     * Once sent, carries an empty body object.
     * This response means the client is now registered on the lobby server, and should select whether to
     * join a game or create one. <br>
     * To join a game use {@link it.polimi.ingsw.RemoteView.Messages.Events.Requests.ConnectLobbyRequest} <br>
     * To create a game use {@link it.polimi.ingsw.RemoteView.Messages.Events.Requests.CreateLobbyRequest}
     */
    REQUEST_CREATE_LOBBY,
    /**
     * Sent on redirect of the lobby server. <br>
     * Once sent, carries an ??? body object. //TODO
     * This response means the client has now entered a lobby. Depending on the {@link it.polimi.ingsw.RemoteView.Messages.ServerResponses.Response}
     * the lobby may be on hold or the game may have already started. <br>
     * If the lobby was ever on hold, the client should wait for a
     * {@link it.polimi.ingsw.RemoteView.Messages.ServerResponses.GameStarted} response.
     */
    REQUEST_CONNECT_LOBBY,
    REQUEST_START_GAME,
    REQUEST_PLAYER_ACTION,
}
