package com.db.symphonyp.tabs;

import clients.ISymClient;
import clients.SymBotClient;
import listeners.IMListener;
import model.InboundMessage;
import model.OutboundMessage;
import model.Stream;

public class IMListenerImpl implements IMListener {
    private final ISymClient bot;

    public IMListenerImpl(SymBotClient botClient) {
        bot = botClient;
    }

    public void onIMMessage(InboundMessage message) {
        String streamId = message.getStream().getStreamId();
        String messageOut = String.format("Hello %s!", message.getUser().getDisplayName());
        bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(messageOut));
    }

    public void onIMCreated(Stream stream) {
    }
}
