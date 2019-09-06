package com.db.symphonyp.tabs;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class SettingsController {
	
	@Value("${settings.file:file:settings.json}")
	String resourceName;
	
	@Autowired
	ResourceLoader rl;
	
	@Autowired
	ObjectMapper mapper;

	public static class Setting {
		
		private String name;
		private String pattern;
		private float max, expected;
		
		public Setting() {
		}

		public Setting(String name, String pattern, float max, float expected) {
			super();
			this.name = name;
			this.pattern = pattern;
			this.max = max;
			this.expected = expected;
		}

		public String getName() {
			return name;
		}

		public String getPattern() {
			return pattern;
		}

		public float getMax() {
			return max;
		}

		public float getExpected() {
			return expected;
		}
	}
	
	private static final TypeReference<List<Setting>> SETTINGS_TYPE_REFERENCE = new TypeReference<List<Setting>>() {};
	
	@GetMapping(path="/settings")
	public List<Setting> loadSettings() throws Exception {
		Resource r = rl.getResource(resourceName);
		if (r.exists()) {
			List<Setting> settings = mapper.readValue(r.getInputStream(), SETTINGS_TYPE_REFERENCE);
			return settings;
		} else {
			return Collections.emptyList();
		}
	}
	
	@PostMapping(path="/settings")
	public void saveSettings(@RequestBody List<Setting> content) throws Exception {
		Resource r = rl.getResource(resourceName);
		String out = mapper.writeValueAsString(content);
		File f = r.getFile();
		FileWriter fw = new FileWriter(f);
		try (fw) {
			fw.write(out);
		}
	}
}
