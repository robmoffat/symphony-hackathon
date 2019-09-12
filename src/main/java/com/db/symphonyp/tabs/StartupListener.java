package com.db.symphonyp.tabs;

import clients.SymBotClient;
import listeners.DatafeedListener;
import model.OutboundMessage;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupListener {

    @Value("${symphony.stream-id:}")
    private String infoStreamId;

    private final SymBotClient botClient;
    private final DatafeedListener imListenerImpl;
    private final DatafeedListener roomListenerImpl;

    public StartupListener(SymBotClient botClient, DatafeedListener imListenerImpl, DatafeedListener roomListenerImpl) {
        this.botClient = botClient;
        this.imListenerImpl = imListenerImpl;
        this.roomListenerImpl = roomListenerImpl;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        if (!StringHelper.isNullOrEmptyString(infoStreamId)) {
            OutboundMessage mes = new OutboundMessage("Started Symphony Tabs");
            botClient.getMessagesClient().sendMessage(infoStreamId, mes);
        }

        // Link datafeed listeners
        botClient.getDatafeedEventsService().addListeners(
                imListenerImpl, roomListenerImpl
        );
    }
}
