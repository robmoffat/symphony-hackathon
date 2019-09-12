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
    private final SymBotClient botClient77;
    private final IMListenerImpl imListenerImpl;
    private final RoomListenerImpl roomListenerImpl;
    private final BotBrain brain75;
    private final BotBrain brain77;

    public StartupListener(SymBotClient botClient75, SymBotClient botClient77, IMListenerImpl imListenerImpl, RoomListenerImpl roomListenerImpl, BotBrain brain75, BotBrain brain77) {
        this.botClient75 = botClient75;
        this.botClient77 = botClient77;
        this.imListenerImpl = imListenerImpl;
        this.roomListenerImpl = roomListenerImpl;
        this.brain75 = brain75;
        this.brain77 = brain77;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        if (!StringHelper.isNullOrEmptyString(infoStreamId)) {
            OutboundMessage mes = new OutboundMessage("Started Tabs Bot");
            botClient75.getMessagesClient().sendMessage(infoStreamId, mes);

            mes = new OutboundMessage("Started Bonds Bot");
            botClient77.getMessagesClient().sendMessage(infoStreamId, mes);
        }

        // Link datafeed listeners
        botClient75.getDatafeedEventsService().addListeners(
                imListenerImpl.withBrain(brain75), roomListenerImpl.withBrain(brain75)
        );

        // Link datafeed listeners
        botClient77.getDatafeedEventsService().addListeners(
                imListenerImpl.withBrain(brain77), roomListenerImpl.withBrain(brain77)
        );
    }
}
