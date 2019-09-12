package com.db.symphonyp.tabs.botClient76;

import clients.ISymClient;
import clients.SymBotClient;
import com.db.symphonyp.tabs.BotBrain;
import com.db.symphonyp.tabs.common.IMListenerImpl;
import com.db.symphonyp.tabs.common.RoomListenerImpl;
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

    private final ISymClient botClient76;
    private final IMListenerImpl imListenerImpl;
    private final RoomListenerImpl roomListenerImpl;
    private final BotBrain botBrain76Controller;

    public StartupListener(IMListenerImpl imListenerImpl, RoomListenerImpl roomListenerImpl, BotBrain botBrain76Controller) {
        this.botClient76 = SymBotClient.initBotRsa("config76.json");
        this.imListenerImpl = imListenerImpl;
        this.roomListenerImpl = roomListenerImpl;
        this.botBrain76Controller = botBrain76Controller.with(botClient76);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        if (!StringHelper.isNullOrEmptyString(infoStreamId)) {
            OutboundMessage mes = new OutboundMessage("Started Bonds Bot");
            botClient76.getMessagesClient().sendMessage(infoStreamId, mes);
        }
        // Link datafeed listeners
        ((SymBotClient) botClient76).getDatafeedEventsService().addListeners(
                imListenerImpl.withBrain(botBrain76Controller), roomListenerImpl.withBrain(botBrain76Controller)
        );
    }
}
