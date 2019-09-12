package com.db.symphonyp.tabs.botClient76;

import clients.ISymClient;

import com.db.symphonyp.tabs.BotBrain;
import com.db.symphonyp.tabs.common.Table;
import com.db.symphonyp.tabs.common.TableConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import model.InboundMessage;
import model.OutboundMessage;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class BotBrain76Controller implements BotBrain {

	@Autowired
	ObjectMapper om;
	
	@Autowired
	TableConverter tc;
	
	@Value("${cache:cache}")
	String cache;
	
	private static final Logger LOG = LoggerFactory.getLogger(BotBrain76Controller.class);
	
    private ISymClient bot;

    private Pattern patGiveMe;
    private Pattern patPriceBasket;
    
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
        
        if (messageText == null || messageText.isEmpty())
        	return;
        
        Matcher matGiveMe = patGiveMe.matcher(messageText);
        Matcher matPriceBasket = patPriceBasket.matcher(messageText);

        if (matPriceBasket.find())
        {
        	priceBasket(streamId, Double.parseDouble(matPriceBasket.group(1)));
        }
        else if (matGiveMe.find())
        {
        	giveBond(matGiveMe.group(1));
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

    private void priceBasket(String streamId, double price) {
    	LOG.info("76 priceBasket>" + price);
    	
    	Table basket = new Table();
    	
    	double total  = 0;
    	
        DecimalFormat df2 = new DecimalFormat("#.##");
    	
    	Random rand = new Random();
    	
    	List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
    	
    	while (total < price)
    	{
    		int selector = rand.nextInt(data.size());
    		
    		Bond bond = getInstance(selector);
    		
    		int number = rand.nextInt(50) + 1;
    		
    		double thisp = bond.price * number;
    		
    		LOG.info(bond.isin + " " + bond.price + "/" + number + "=" + thisp);
    		LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
    		
    		row.put("isin", bond.isin);
    		row.put("bond", bond.bond);
    		row.put("price", df2.format(bond.price));
    		row.put("volume", df2.format(number));

    		rows.add(row);
    		
    		total += thisp;
    	}
    	
    	LOG.info("priced basket " + rows.size() + " entries");
    	basket.setRows(rows);
    	
		long r = new Random().nextLong();
		basket.setTableId(""+r);
    	
		
    	String s = tc.getMessageML( basket);
    	
    	LOG.info(s);
    	try {
    		File cdir = new File(cache);
    		File out = new File(cdir, ""+r+".json");
    		om.writeValue(out, basket);
    		LOG.info("saved: "+out);
    		bot.getMessagesClient().sendTaggedMessage(streamId, new OutboundMessage( tc.getMessageML(basket), tc.getJson(basket) ));
    	}
    	catch (Exception e) {
    		LOG.error("Unable to post message, " + e.getMessage(), e);
    	}
    }
    
    private Bond getInstance(int selector) {
    	int count = 0;

    	Iterator it = data.entrySet().iterator();
    	
        while (it.hasNext()) {
            Map.Entry<String, Bond> pair = (Map.Entry)it.next();
            Bond bond = pair.getValue();
    		if (count++ == selector) return bond;
    	}
        
        return null;
    }
    
    public void giveBond(String isin) {
    	
    	LOG.info("76 giveBond>" + isin);
    	
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
    
    @Override
    public BotBrain with(ISymClient symClient) {
        this.bot = symClient;
        
        patGiveMe = Pattern.compile("^\\s*give \\s+ me \\s+ bond \\s+ (\\w+)", Pattern.CASE_INSENSITIVE | Pattern.COMMENTS);
        patPriceBasket = Pattern.compile("^\\s*price \\s+ basket \\s+ (\\d+)", Pattern.CASE_INSENSITIVE | Pattern.COMMENTS);
        
        loadBondData();
		
        return this;
    }
    
    private void loadBondData() {
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
    }
}
