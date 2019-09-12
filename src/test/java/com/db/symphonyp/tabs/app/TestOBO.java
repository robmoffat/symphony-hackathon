package com.db.symphonyp.tabs.app;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.symphonyp.tabs.SymphonyTabsApp;
import com.db.symphonyp.tabs.app.TableController;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={SymphonyTabsApp.class})
@TestPropertySource(properties = { 
		"settings.file=file:target/test-settings.json"})
public class TestOBO {

	@Autowired
	TableController c;
	
	@Autowired
	ObjectMapper om;
	
	@Test
	public void doExtractJWT() throws JsonParseException, JsonMappingException, IOException {
		String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzUxMiJ9.eyJpc3MiOiJTeW1waG9ueSBDb21tdW5pY2F0aW9uIFNlcnZpY2VzIExMQy4iLCJzdWIiOjM0OTAyNjIyMjM0ODIxNywiYXVkIjoic3ltcGhvbnktdGFicyIsInVzZXIiOnsiaWQiOjM0OTAyNjIyMjM0ODIxNywiZW1haWxBZGRyZXNzIjoicm9iZXJ0Lm1vZmZhdEBkYi5jb20iLCJmaXJzdE5hbWUiOiJSb2JlcnQiLCJsYXN0TmFtZSI6Ik1vZmZhdHQiLCJkaXNwbGF5TmFtZSI6IlJvYmVydCBNb2ZmYXR0IiwiY29tcGFueSI6IlN5bXBob255IFBhcnRuZXIgRGV2ZWxvcG1lbnQiLCJjb21wYW55SWQiOjUwNzksInVzZXJuYW1lIjoicm9iZXJ0Lm1vZmZhdEBkYi5jb20iLCJsb2NhdGlvbiI6IkxvbmRvbiIsImF2YXRhclVybCI6Ii4uL2F2YXRhcnMvc3RhdGljLzE1MC9kZWZhdWx0LnBuZyIsImF2YXRhclNtYWxsVXJsIjoiLi4vYXZhdGFycy9zdGF0aWMvNTAvZGVmYXVsdC5wbmcifSwiZXhwIjoxNTY5NDkwNjA3fQ.mIW3FgVK29CxBeGZzAoLF6LvdCQSjDPyaMAjFbzTEBpg_BWXstocWT0TYPeiY1erXBGEinyKRbh9vMreTfiXrH_12WaDW2rkQJgALR_ihwJi-Q3FkSJxJ_FysGbDOMlPa0GOqD6Q7_rODiHnk7nBn2ouas9A6Spz9hSMmKmq5JEFSmvOUTybfZWUopy7OnNLtnYZqMzCr6N-zO8lzfDVAkzDdoFdh83r9fomMY2g4ie9T3xKpTORngEFMp8OfH8Xmcg31MiyPbK-HU0VW3s5GpYnXAmwvDtsllrYYem1OrIE2KIq-eXzOPgUs_SzMdlQiKCiy4Svt-2w4NO8eLYJuQ";
		c.postTable(null, jwt, "o-ucd9iif0uV60PsIdWICn___pLkiSebdA==");
	}
}
