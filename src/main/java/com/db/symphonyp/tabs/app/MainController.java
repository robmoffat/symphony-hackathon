package com.db.symphonyp.tabs.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.symphonyp.tabs.common.Table;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import authentication.SymExtensionAppRSAAuth;
import configuration.SymConfig;
import configuration.SymConfigLoader;
import model.AppAuthResponse;

@Controller
public class MainController implements InitializingBean{
	
	private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
		
	@Value("${baseUrl:https://localhost:4000}")
	String baseUrl;
		
	SymExtensionAppRSAAuth rsaAuth;
	
	@Autowired
	ObjectMapper mapper;
	
	@Value("${pod.host:https://develop2.symphony.com}")
	String podHost;
	
	@Value("${dev}")
	boolean dev = false;
	
	@Autowired
	TableController tc;
	
	@GetMapping(path="/app.html")
	public String appPage(Model m, @RequestParam(name="id", required = false) String id, @RequestParam(name="stream", required = false) String stream) throws JsonParseException, JsonMappingException, IOException {
		addParams(m, dev);
		if (id != null) {
			Table t = tc.getTable(id);
			m.addAttribute("data", t);
		}
		m.addAttribute("stream", stream);
		return "app.html";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LOG.info("afterPropertiesSet on MainController = setting up SymBotClient");
		SymConfig config = SymConfigLoader.loadFromFile("config.json");
		rsaAuth = new SymExtensionAppRSAAuth(config);
		LOG.info("set up rsaAuth");
	}
	
	private void addParams(Model m, boolean dev) {
		m.addAttribute("baseUrl", baseUrl);
		m.addAttribute("podAuthUrl", baseUrl+"/podAuth?dev="+dev+"&podId=");
		m.addAttribute("appAuthUrl", baseUrl+"/appAuth?dev="+dev+"&");
		m.addAttribute("dev", dev);
	}

	@GetMapping(path="/js/controller.js")
	public String getControllerJs(Model m, @RequestParam(name = "dev", required= false, defaultValue="false" ) boolean dev) {
		addParams(m, dev);
		return "controller.js";
	}
	
	@GetMapping(path="/controller.html")
	public String getControllerHtml(Model m, @RequestParam(name = "dev", required= false, defaultValue="false" ) boolean dev) {
		addParams(m, dev);
		return "controller.html";
	}
	
	@GetMapping(path="/buttons.html")
	public String getButtonsHtml(Model m, @RequestParam(name="id", required = false) String id) {
		addParams(m, dev);
		m.addAttribute("id", id);
		return "buttons.html";
	}
	
	@GetMapping("/podAuth")
	@ResponseBody
	public Map<String, Object> podAuth(@RequestParam("podId") String podId, @RequestParam(name = "dev",required= false,defaultValue="false" ) boolean dev) {
		LOG.info("Called podAuth with podId {} dev= {}", podId, dev);
		// get pod token
		Map<String, Object> out = new HashMap<String, Object>();
		
		if (dev) {
			out.put("tokenA", "someAppToken");
			out.put("appId", "appId");
			out.put("tokenS", "someSymphonyToken");
			out.put("expireAt", System.currentTimeMillis());
		} else {
			AppAuthResponse response = rsaAuth.appAuthenticate();
			out.put("tokenA", response.getAppToken());
			out.put("appId", response.getAppId());
			out.put("tokenS", response.getSymphonyToken());
			out.put("expireAt", response.getExpireAt());
		} 
		
		out.put("dev", dev);
		LOG.info("Completed PodAuth {} with {} ", podId, out);
		return out;
	}
	
	@GetMapping("/appAuth")
	@ResponseBody
	public void appAuth(@RequestParam("appToken") String appToken, @RequestParam("podToken") String podToken, @RequestParam(name="dev", required= false, defaultValue="false") boolean dev) {
		LOG.info("Called appAuth with appToken {} and podToken {} and dev {}", appToken, podToken, dev);
		LOG.info("Done appAuth (always returns ok)");
	}

	
	@GetMapping(path="/js/app.js")
	public String getAppJs(Model m, @RequestParam(name = "dev",required= false,defaultValue="false" ) boolean dev) {
		addParams(m, dev);
		return "app.js";
	}

	
}
