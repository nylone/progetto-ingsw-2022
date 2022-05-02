package it.polimi.ingsw.RemoteView.Messages;

import com.google.gson.Gson;

public interface MessageBuilder {
    PayloadType getPayloadType();
    default String toJson() {
        return new Gson().toJson(this);
    }
    default Message build() {
        return Message.build(this);
    }
}
