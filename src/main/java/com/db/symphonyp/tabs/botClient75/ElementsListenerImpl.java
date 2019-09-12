package com.db.symphonyp.tabs.botClient75;

import com.db.symphonyp.tabs.BotBrain;
import listeners.DatafeedListener;
import listeners.ElementsListener;
import model.User;
import model.events.SymphonyElementsAction;
import org.springframework.stereotype.Component;

@Component
public class ElementsListenerImpl implements ElementsListener {
    private BotBrain botBrain75Controller;

    public void onElementsAction(User initiator, SymphonyElementsAction action) {
        botBrain75Controller.onElementsAction(initiator, action);
    }


    public DatafeedListener with(BotBrain botBrain75Controller) {
        this.botBrain75Controller = botBrain75Controller;
        return this;
    }
}
