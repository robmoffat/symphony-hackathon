package com.db.symphonyp.tabs;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import authentication.SymOBORSAAuth;
import authentication.SymOBOUserRSAAuth;
import clients.SymOBOClient;
import configuration.SymConfig;
import configuration.SymConfigLoader;
import model.OutboundMessage;

@Controller
public class TableController implements InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(TableController.class);
	
	@Value("${symphony.stream-id:}")
	String infoStreamId;
	
	SymConfig config;
	
	@Autowired
	ObjectMapper om;

	@Autowired
	TableConverter c;
	
	//@PostMapping(name="/table/{streamId}")
	public void postTable(@RequestBody Table table, @RequestHeader(name="Authorization") String jwt, @PathVariable("streamId") String streamId) throws JsonParseException, JsonMappingException, IOException {
		if (table == null) {
			table = getDefaultTable();
		}
		
		if (streamId == null) {
			streamId = infoStreamId;
		}
		
		SymOBORSAAuth auth = new SymOBORSAAuth(config);
		long userID = getIDFromJWT(jwt, om);
		SymOBOUserRSAAuth authToken = auth.getUserAuth(userID);
		SymOBOClient client = new SymOBOClient(config, authToken);
		OutboundMessage message = convertToMessage(table);
		
		client.getMessagesClient().sendTaggedMessage(streamId, message);
	}

	private OutboundMessage convertToMessage(Table table) {
		return new OutboundMessage(c.getMessageML(table), c.getJson(table));
	}

	public static Long getIDFromJWT(String jwt, ObjectMapper om) throws IOException, JsonParseException, JsonMappingException {
		String[] parts = jwt.split("\\.");
		byte[] data = Base64.getDecoder().decode(parts[1]);
		TypeReference<Map<String, Object>> tr = new TypeReference<Map<String,Object>>() {};
		Map<String, Object> claims = om.readValue(data, tr);
		
		Map<String, Object> user = (Map<String, Object>) claims.get("user");
		Long id = (Long) user.get("id");
		return id;
	}

	private Table getDefaultTable() {
		Map<String, Object> testRow = new LinkedHashMap<>();
		testRow.put("name", "some name");
		testRow.put("bool", true);
		testRow.put("date", new Date());
		testRow.put("int", 55);
		testRow.put("float", 453.3f);
		
		Map<String, String> approvers = new LinkedHashMap<>();
		approvers.put("robert.moffat@db.com", "none");
		return new Table(Collections.singletonList(testRow), approvers);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LOG.info("afterPropertiesSet on TableController = setting up SymBotClient");
		config = SymConfigLoader.loadFromFile("config.json");
		LOG.info("set up rsaAuth");
	}
	
}
