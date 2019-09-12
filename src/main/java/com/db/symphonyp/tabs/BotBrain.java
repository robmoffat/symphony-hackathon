package com.db.symphonyp.tabs;

import clients.ISymClient;
import model.InboundMessage;
import model.User;
import model.events.SymphonyElementsAction;

public interface BotBrain {
    void onRoomMessage(InboundMessage message);

    void onIMMessage(InboundMessage message);

    BotBrain with(ISymClient symClient);

    void onElementsAction(User initiator, SymphonyElementsAction action);
}
