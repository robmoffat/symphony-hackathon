package com.db.symphonyp.tabs.botClient75;

import clients.ISymClient;
import com.db.symphonyp.tabs.BotBrain;
import com.db.symphonyp.tabs.common.DataWrapper;
import com.db.symphonyp.tabs.common.Table;
import com.db.symphonyp.tabs.common.TableConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import model.*;
import model.events.SymphonyElementsAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import utils.FormBuilder;

import javax.ws.rs.core.NoContentException;
import java.util.*;

@Controller
public class BotBrain75Controller implements BotBrain {

    private ISymClient bot;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TableConverter tc;

    private Table lastTable;


    public void onElementsAction(User initiator, SymphonyElementsAction action) {
        Map<String, Object> formValues = action.getFormValues();
        String input = (String) formValues.get("tableName");
        String messageOut;
        switch (action.getFormId()) {
            case "approvals-table-form":
                Map<String, String> approvals = new LinkedHashMap<>();
                List<Long> assignedTo = (List) formValues.get("assignedTo");
                for (Long aLong : assignedTo) {
                    try {
                        UserInfo userFromId = bot.getUsersClient().getUserFromId(aLong, true);
                        String userIMStreamId = bot.getStreamsClient().getUserIMStreamId(aLong);
                        String username = userFromId.getUsername();
                        approvals.put(aLong.toString(), username);
                        bot.getMessagesClient().sendMessage(userIMStreamId, new OutboundMessage("You have a table pending to sign off"));
                    } catch (NoContentException e) {
                        e.printStackTrace();
                    }
                }

                lastTable.setApprovals(approvals);
                bot.getMessagesClient().sendTaggedMessage(action.getStreamId(), new OutboundMessage(tc.getMessageML(lastTable), tc.getJson(lastTable)));
                break;
            case "signoff-table-form":
                messageOut = String.format("Hi %s! You signed off table", initiator.getFirstName());
                bot.getMessagesClient().sendMessage(action.getStreamId(), new OutboundMessage(messageOut));
                break;
            case "create-table-form":
                messageOut = String.format("Hi %s! You created the table: %s", initiator.getFirstName(), input);
                bot.getMessagesClient().sendMessage(action.getStreamId(), new OutboundMessage(messageOut));
                renderTableForm(action.getStreamId(), formValues);
                break;
        }
//
//        Map<String, Object> formValues = action.getFormValues();
//        String input = (String) formValues.get("input-name");
//
//        String messageOut = String.format("Hi %s! You entered: %s", initiator.getFirstName(), input);
//        bot.getMessagesClient().sendMessage(action.getStreamId(), new OutboundMessage(messageOut));
    }

    private void renderTableForm(String streamId, Map<String, Object> formValues) {
        String formML = FormBuilder.builder("select-table-form")
                .addHeader(6, "Table Reference: " + formValues.get("tableName"))
                .addHeader(6, "Assigned To:")
                .addPersonSelector("assignedTo", "Assign to..", true)
                //.addTableSelect()
                .addButton("confirm", "Confirm", FormButtonType.ACTION)
                .addButton("reset", "Reset", FormButtonType.RESET)
                .formatElement();

        bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(formML));
    }

    public void process(InboundMessage message) {
//        String streamId = message.getStream().getStreamId();
//        String firstName = message.getUser().getFirstName();
//        String messageOut = String.format("Hello %s! from 75", firstName);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        this.bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(messageOut));

        String streamId = message.getStream().getStreamId();
        String data = message.getData();
        Table table = null;
        try {
            table = objectMapper.readValue(data, DataWrapper.class).getObject001().getId().get(0);
        } catch (Exception e) {
//            e.printStackTrace();
        }


        if (table != null && table.getApprovals() == null) {
            lastTable = table;
            renderApprovalsForm(message, table);
            //renderTable(message, table);
            //renderSelectTable(message, table);
        } else if (message.getMessageText().toLowerCase().startsWith("create")) {
            renderCreateTableForm(message);
        } else if (message.getMessageText().toLowerCase().startsWith("approve")) {
            renderConfirmationTableForm(message);
        } else {

            Properties props = new Properties();
            props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
            props.setProperty("coref.algorithm", "neural");
            props.setProperty("ner.model", "ner.model.ser.gz");
            props.setProperty("ner.useSUTime", "false");
            props.setProperty("ner.applyNumericClassifiers", "false");
            props.setProperty("ner.applyFineGrained", "false");
            Document doc = new Document(props, message.getMessageText().toLowerCase());

            for (Sentence sent : doc.sentences()) { // Will iterate over two sentences
                System.out.println("The words of the sentence '" + sent + "' is " + sent.words());
                System.out.println("The lemmas of the sentence '" + sent + "' is " + sent.lemmas());
                System.out.println("The posTags of the sentence '" + sent + "' is " + sent.posTags());
                System.out.println("The mentions of the sentence '" + sent + "' is " + sent.mentions());
                System.out.println("The nerTags of the sentence '" + sent + "' is " + sent.nerTags());
                System.out.println("The parse of the sentence '" + sent + "' is " + sent.parse());
                System.out.println();

            }
        }

    }

    private void renderApprovalsForm(InboundMessage message, Table table) {
        String streamId = message.getStream().getStreamId();
        String formML = FormBuilder.builder("approvals-table-form")
                .addHeader(6, "Choose Approvals")
                .addPersonSelector("assignedTo", "Assign to..", true)
                .addButton("confirm", "Confirm", FormButtonType.ACTION)
                .addButton("reset", "Reset", FormButtonType.RESET)
                .formatElement();

        bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(formML));
    }

    private void renderSelectTable(InboundMessage message, Table table) {
        String streamId = message.getStream().getStreamId();
        FormBuilder builder = FormBuilder.builder("select-table-form");
        List<List<String>> body = new ArrayList<>();
        for (Map<String, Object> row : table.getRows()) {
            ArrayList<String> list = new ArrayList<>();
            for (String s : row.keySet()) {
                list.add(row.get(s).toString());
            }

            body.add(list);
        }


        builder.addTableSelect(null,
                null,
                TableSelectPosition.RIGHT,
                TableSelectType.BUTTON,
                table.getColumns(),
                body,
                null);

        String formML = builder.formatElement();
        bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(formML));
    }

    private void renderTable(InboundMessage message, Table table) {
        String streamId = message.getStream().getStreamId();
        FormBuilder builder = FormBuilder.builder("edit-table-form");
//        for (String column : table.getColumns()) {
//            builder.addHeader(1,column);
//        }
        for (Map<String, Object> row : table.getRows()) {
            for (String key : row.keySet()) {
                builder.addTextField(key, row.get(key).toString(), row.get(key).toString(), false, false, 1, 10);
            }
        }

        String formML = builder.formatElement();
        bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(formML));
    }

    private void renderConfirmationTableForm(InboundMessage message) {
        String streamId = message.getStream().getStreamId();
        String formML = FormBuilder.builder("signoff-table-form")
                .addHeader(6, "Are you sure?")
                .addButton("confirm", "Confirm", FormButtonType.ACTION)
                .formatElement();

        bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(formML));
    }


    private void renderCreateTableForm(InboundMessage message) {
        String streamId = message.getStream().getStreamId();
        String formML = FormBuilder.builder("create-table-form")
                .addHeader(6, "Table Reference")
                .addTextField("tableName", "", "Enter a code..", true, false, 1, 15)
                .addHeader(6, "Assigned To:")
                .addPersonSelector("assignedTo", "Assign to..", true)
                //.addTableSelect()
                .addButton("confirm", "Confirm", FormButtonType.ACTION)
                .addButton("reset", "Reset", FormButtonType.RESET)
                .formatElement();

        bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(formML));
    }

    public void onRoomMessage(InboundMessage message) {
        process(message);
    }

    @Override
    public void onIMMessage(InboundMessage message) {
        process(message);
    }

    @Override
    public BotBrain with(ISymClient symClient) {
        this.bot = symClient;
        return this;
    }
}
