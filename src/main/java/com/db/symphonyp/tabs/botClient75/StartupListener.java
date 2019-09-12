package com.db.symphonyp.tabs.botClient75;

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

    private final ISymClient botClient75;
    private final IMListenerImpl imListenerImpl;
    private final RoomListenerImpl roomListenerImpl;
    private final BotBrain botBrain75Controller;
    private final ElementsListenerImpl elementsListenerImpl;

    public StartupListener(IMListenerImpl imListenerImpl, RoomListenerImpl roomListenerImpl, BotBrain botBrain75Controller, ElementsListenerImpl elementsListenerImpl) {
        this.elementsListenerImpl = elementsListenerImpl;
        this.botClient75 = SymBotClient.initBotRsa("config75.json");
        this.imListenerImpl = imListenerImpl;
        this.roomListenerImpl = roomListenerImpl;
        this.botBrain75Controller = botBrain75Controller.with(botClient75);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        if (!StringHelper.isNullOrEmptyString(infoStreamId)) {
            OutboundMessage mes = new OutboundMessage("Started Tabs Bot");
            botClient75.getMessagesClient().sendMessage(infoStreamId, mes);
        }

        // Link datafeed listeners
        ((SymBotClient) botClient75).getDatafeedEventsService().addListeners(
                imListenerImpl.withBrain(botBrain75Controller),
                roomListenerImpl.withBrain(botBrain75Controller),
                elementsListenerImpl.with(botBrain75Controller)
        );
    }
}
