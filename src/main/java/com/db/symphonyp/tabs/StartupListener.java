package com.db.symphonyp.tabs;

import clients.SymBotClient;
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

    private final SymBotClient botClient75;
    private final SymBotClient botClient76;
    private final IMListenerImpl imListenerImpl;
    private final RoomListenerImpl roomListenerImpl;
    private final BotBrain botBrain75Controller;
    private final BotBrain botBrain76Controller;

    public StartupListener(SymBotClient botClient75, SymBotClient botClient76, IMListenerImpl imListenerImpl, RoomListenerImpl roomListenerImpl, BotBrain botBrain75Controller, BotBrain botBrain76Controller) {
        this.botClient75 = botClient75;
        this.botClient76 = botClient76;
        this.imListenerImpl = imListenerImpl;
        this.roomListenerImpl = roomListenerImpl;
        this.botBrain75Controller = botBrain75Controller;
        this.botBrain76Controller = botBrain76Controller;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        if (!StringHelper.isNullOrEmptyString(infoStreamId)) {
            OutboundMessage mes = new OutboundMessage("Started Tabs Bot");
            botClient75.getMessagesClient().sendMessage(infoStreamId, mes);

            mes = new OutboundMessage("Started Bonds Bot");
            botClient76.getMessagesClient().sendMessage(infoStreamId, mes);
        }

        // Link datafeed listeners
        botClient75.getDatafeedEventsService().addListeners(
                imListenerImpl.withBrain(botBrain75Controller), roomListenerImpl.withBrain(botBrain75Controller)
        );

        // Link datafeed listeners
        botClient76.getDatafeedEventsService().addListeners(
                imListenerImpl.withBrain(botBrain76Controller), roomListenerImpl.withBrain(botBrain76Controller)
        );
    }
}
