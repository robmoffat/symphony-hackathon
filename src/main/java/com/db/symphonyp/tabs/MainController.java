package com.db.symphonyp.tabs;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainController implements InitializingBean{
	
	@Value("${share.dir:shared}")
	String shareDirectory;
	
	@Value("${baseUrl:https://localhost:4000}")
	String baseUrl;
	
	@Autowired
	ObjectMapper mapper;

	@PostMapping(path="/share/{id}")
	@ResponseBody
	public void saveSettings(@RequestBody Object content, @PathVariable("id") String id) throws Exception {
		File out = new File(shareDirectory+"/"+id+".json");
		mapper.writeValue(out, content);
	}
	
	@GetMapping(path="/har")
	public String loadHARAnalyser(@RequestParam(name="id", required=false) String id, Model m) throws JsonParseException, JsonMappingException, IOException {
		m.addAttribute("data", Collections.emptyList());
		if (id != null) {
			File out = new File(shareDirectory+"/"+id+".json");
			if (out.exists()) {
				Object data = mapper.readValue(out, Object.class);
				m.addAttribute("data", data);
			}
		}
		return "har";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		new File(shareDirectory).mkdirs();
	}
	
	private void addParams(Model m) {
		m.addAttribute("baseUrl", baseUrl);
	}

	@GetMapping(path="/js/controller.js")
	public String getController(Model m) {
		addParams(m);
		return "controller.js";
	}
	
	@GetMapping(path="/js/app.js")
	public String getAppJs(Model m) {
		addParams(m);
		return "app.js";
	}
	
	
}
