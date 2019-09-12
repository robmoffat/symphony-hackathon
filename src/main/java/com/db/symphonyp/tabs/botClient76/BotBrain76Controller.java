package com.db.symphonyp.tabs.botClient76;

import clients.ISymClient;
import com.db.symphonyp.tabs.BotBrain;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import model.InboundMessage;
import model.User;
import model.events.SymphonyElementsAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class BotBrain76Controller implements BotBrain {

	private static final Logger LOG = LoggerFactory.getLogger(BotBrain76Controller.class);

    private ISymClient bot;

    private Pattern patGiveMe;
    HashMap<String, Bond> data;

	public void process(InboundMessage message) {
        String streamId = message.getStream().getStreamId();
        String firstName = message.getUser().getFirstName();
        String messageOut = String.format("Hello %s! from bot 76", firstName);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

		String messageText = message.getMessageText();

		if (messageText == null | messageText.isEmpty())
        	return;

		Matcher matGiveMe = patGiveMe.matcher(messageText);

		if (matGiveMe.find())
        {
        	System.out.println("76 in>" + matGiveMe.group(1));

			String isin = matGiveMe.group(1);

			if (data.containsKey(isin))
        	{
        		Bond bond = data.get(isin);
        		LOG.info("bond " + isin + " exists; " + bond.price);
        	}
        	else
        	{
        		LOG.info("bond " + isin + " is unknown");
        	}
        }

		//this.bot.getMessagesClient().sendMessage(streamId, new OutboundMessage(messageOut));
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

		patGiveMe = Pattern.compile("^give \\s+ me \\s+ bond \\s+ (\\w+)", Pattern.CASE_INSENSITIVE | Pattern.COMMENTS);

		data = new HashMap<String, Bond>();

		CsvMapper csv = new CsvMapper();
		csv.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(',');

		Reader reader;
		MappingIterator<Bond> mi = null;

		try {
		    reader = new FileReader("src/main/resources/static/bonds/bonds.csv");

			ObjectReader oReader = csv.reader(Bond.class).with(schema);

			mi = oReader.readValues(reader);
		}
		catch (Exception e)
		{
			LOG.error("Problem loading bond data file; " + e.getMessage());
		}

		while (mi.hasNext()){
			  Bond row = mi.next();

			LOG.info("Adding bond " + row.isin);

			data.put(row.isin, row);
		}

		return this;
    }

	@Override
	public void onElementsAction(User initiator, SymphonyElementsAction action) {

	}
}
