package com.db.symphonyp.tabs;

import clients.ISymClient;
import model.InboundMessage;

public interface BotBrain {
    void onRoomMessage(InboundMessage message);

    void onIMMessage(InboundMessage message);

    BotBrain with(ISymClient symClient);
}
