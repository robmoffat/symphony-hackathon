package com.db.symphonyp.tabs;

import clients.ISymClient;
import listeners.ElementsListener;
import model.OutboundMessage;
import model.User;
import model.events.SymphonyElementsAction;

import java.util.Map;

public class ElementsListenerImpl implements ElementsListener {
    private ISymClient bot;

    public void onElementsAction(User initiator, SymphonyElementsAction action) {
        Map<String, Object> formValues = action.getFormValues();
        String input = (String) formValues.get("input-name");

        String messageOut = String.format("Hi %s! You entered: %s", initiator.getFirstName(), input);
        bot.getMessagesClient().sendMessage(action.getStreamId(), new OutboundMessage(messageOut));
    }
}
