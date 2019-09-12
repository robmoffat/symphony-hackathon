package com.db.symphonyp.tabs.botClient77;

import clients.ISymClient;
import com.db.symphonyp.tabs.BotBrain;
import model.InboundMessage;
import model.OutboundMessage;
import org.springframework.stereotype.Controller;

@Controller
public class BotBrain77Controller implements BotBrain {

    private final ISymClient bot;

    public BotBrain77Controller(ISymClient botClient77) {
        this.bot = botClient77;
    }

    public void process(InboundMessage message) {
        String streamId = message.getStream().getStreamId();
        String firstName = message.getUser().getFirstName();
        String messageOut = String.format("Hello %s! from bot 77", firstName);
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
