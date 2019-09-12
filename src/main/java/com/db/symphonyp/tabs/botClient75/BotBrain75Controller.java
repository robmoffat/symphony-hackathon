package com.db.symphonyp.tabs.botClient75;

import clients.ISymClient;
import com.db.symphonyp.tabs.BotBrain;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import model.FormButtonType;
import model.InboundMessage;
import model.OutboundMessage;
import org.springframework.stereotype.Controller;
import utils.FormBuilder;

import java.util.Properties;

@Controller
public class BotBrain75Controller implements BotBrain {

    private ISymClient bot;


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


        if (message.getMessageText().toLowerCase().startsWith("create")) {
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
