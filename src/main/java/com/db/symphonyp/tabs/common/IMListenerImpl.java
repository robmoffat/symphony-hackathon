package com.db.symphonyp.tabs.common;

import com.db.symphonyp.tabs.BotBrain;
import com.db.symphonyp.tabs.WithBrain;
import listeners.IMListener;
import model.InboundMessage;
import model.Stream;
import org.springframework.stereotype.Component;

@Component
public class IMListenerImpl implements WithBrain, IMListener {

    private BotBrain brain;

    public IMListenerImpl withBrain(BotBrain brain) {
        this.brain = brain;
        return this;
    }

    public void onIMMessage(InboundMessage message) {
        brain.onIMMessage(message);
    }

    public void onIMCreated(Stream stream) {
    }

}
