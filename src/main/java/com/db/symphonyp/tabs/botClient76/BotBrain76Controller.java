package com.db.symphonyp.tabs.botClient76;

import clients.ISymClient;
import com.db.symphonyp.tabs.BotBrain;
import model.InboundMessage;
import model.OutboundMessage;
import org.springframework.stereotype.Controller;

@Controller
public class BotBrain76Controller implements BotBrain {

    private final ISymClient bot;

    public BotBrain76Controller(ISymClient botClient76) {
        this.bot = botClient76;
    }

    public void process(InboundMessage message) {
        String streamId = message.getStream().getStreamId();
        String firstName = message.getUser().getFirstName();
        String messageOut = String.format("Hello %s! from bot 76", firstName);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(messageOut));
    }

    public void onRoomMessage(InboundMessage message) {
        process(message);
    }

    @Override
    public void onIMMessage(InboundMessage message) {
        process(message);
    }
}
