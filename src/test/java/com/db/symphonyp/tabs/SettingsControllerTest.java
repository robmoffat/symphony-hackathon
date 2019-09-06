package com.db.symphonyp.tabs;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.symphonyp.tabs.SettingsController;
import com.db.symphonyp.tabs.SettingsController.Setting;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { 
		"settings.file=file:target/test-settings.json"})
public class SettingsControllerTest {
	
	@Autowired
	SettingsController sc;
	
	@BeforeClass
	public static void removeOldSettings() {
		File s = new File("target/test-settings.json");
		s.delete();
	}

	@Test
	public void testLoadAndSave() throws Exception {
		
		// loading without a file should return the empty list
		List<Setting> emptyList = sc.loadSettings();
		Assert.assertEquals(0, emptyList.size());
		
		
		Setting s1 = new Setting("symphony","/symphony", 1000f, 50f);
		sc.saveSettings(Collections.singletonList(s1));
	
		// check load
		List<Setting> loaded = sc.loadSettings();
		Assert.assertEquals(1, loaded.size());
		
		Setting s2 = loaded.get(0);
		Assert.assertEquals(s1.getName(), s2.getName());
		Assert.assertEquals(s1.getPattern(), s2.getPattern());
		Assert.assertEquals((int) s1.getMax(), (int) s2.getMax());
		Assert.assertEquals((int) s1.getExpected(), (int) s2.getExpected());
	}
	
}
