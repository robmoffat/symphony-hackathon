package com.db.symphonyp.tabs.botClient75;

import clients.ISymClient;
import listeners.DatafeedListener;
import listeners.ElementsListener;
import model.OutboundMessage;
import model.User;
import model.events.SymphonyElementsAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ElementsListenerImpl implements ElementsListener {
    private ISymClient bot;

    public void onElementsAction(User initiator, SymphonyElementsAction action) {
        Map<String, Object> formValues = action.getFormValues();
        String input = (String) formValues.get("tableName");
        String messageOut;
        switch (action.getFormId()) {
            case "signoff-table-form":
                messageOut = String.format("Hi %s! You signed off table: %s", initiator.getFirstName(), input);
                bot.getMessagesClient().sendMessage(action.getStreamId(), new OutboundMessage(messageOut));
                break;
            case "create-table-form":
                messageOut = String.format("Hi %s! You create the table: %s", initiator.getFirstName(), input);
                bot.getMessagesClient().sendMessage(action.getStreamId(), new OutboundMessage(messageOut));
                break;
        }
//
//        Map<String, Object> formValues = action.getFormValues();
//        String input = (String) formValues.get("input-name");
//
//        String messageOut = String.format("Hi %s! You entered: %s", initiator.getFirstName(), input);
//        bot.getMessagesClient().sendMessage(action.getStreamId(), new OutboundMessage(messageOut));
    }

    public DatafeedListener with(ISymClient bot) {
        this.bot = bot;
        return this;
    }
}
