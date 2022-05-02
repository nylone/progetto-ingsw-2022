package it.polimi.ingsw.RemoteView.Messages;

import com.google.gson.Gson;

public class Message {
    private final PayloadType type;
    private final String data;

    private Message(PayloadType type, String data) {
        this.type = type;
        this.data = data;
    }

    public PayloadType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static <T extends MessageBuilder> Message build(T request) {
        return new Message(request.getPayloadType(), request.toJson());
    }
}