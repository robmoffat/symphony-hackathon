package com.db.symphonyp.tabs;

import model.InboundMessage;

public interface BotBrain {
    void onRoomMessage(InboundMessage message);

    void onIMMessage(InboundMessage message);
}
