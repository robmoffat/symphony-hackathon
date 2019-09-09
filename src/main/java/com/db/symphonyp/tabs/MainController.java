package com.db.symphonyp.tabs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainController implements InitializingBean{
		
	@Value("${baseUrl:https://localhost:4000}")
	String baseUrl;
	
	@Autowired
	ObjectMapper mapper;
	
	@GetMapping(path="/app.html")
	public String appPage(Model m) {
		addParams(m);
		return "app.html";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
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
	public Map<String, Object> podAuth(@RequestParam("podId") String podId) {
		Map<String, Object> out = new HashMap<String, Object>();
		out.put("tokenA", "token1");
		out.put("appId", "symphony-tabs");
		out.put("expireAt", System.currentTimeMillis()+1000000l);
		out.put("dev", true);
		return out;
	}
	
	@GetMapping("/appAuth")
	@ResponseBody
	public void podAuth(@RequestParam("appToken") String appToken, @RequestParam("podToken") String podToken) {
		// just works.
	}
	
}
