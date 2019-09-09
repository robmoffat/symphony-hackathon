package com.db.symphonyp.tabs;

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
	
	@GetMapping(path="/app.html")
	public String appPage(Model m) {
		addParams(m);
		return "app.html";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LOG.info("afterPropertiesSet on MainController = setting up SymBotClient");
		SymConfig config = SymConfigLoader.loadFromFile("config.json");
		rsaAuth = new SymExtensionAppRSAAuth(config);
		LOG.info("set up rsaAuth");
	}
	
	private void addParams(Model m) {
		m.addAttribute("baseUrl", baseUrl);
		m.addAttribute("podAuthUrl", baseUrl+"/podAuth?dev=true&podId=");
		m.addAttribute("appAuthUrl", baseUrl+"/appAuth?dev=true&");
	}

	@GetMapping(path="/js/controller.js")
	public String getController(Model m) {
		addParams(m);
		return "controller.js";
	}
	
	@GetMapping("/podAuth")
	@ResponseBody
	public Map<String, Object> podAuth(@RequestParam("podId") String podId, @RequestParam(name = "dev",defaultValue="false" ) boolean dev) {
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
	public void appAuth(@RequestParam("appToken") String appToken, @RequestParam("podToken") String podToken, @RequestParam(name="dev", defaultValue="false") boolean dev) {
		LOG.info("Called appAuth with appToken {} and podToken {}", appToken, podToken);
		LOG.info("Done appAuth (always returns ok)");
	}
	
}
