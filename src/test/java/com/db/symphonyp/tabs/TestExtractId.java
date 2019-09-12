package com.db.symphonyp.tabs;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.symphonyp.tabs.app.TableController;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { 
		"settings.file=file:target/test-settings.json"})
public class TestExtractId {

	@Autowired
	TableController c;
	
	@Autowired
	ObjectMapper om;
	
	@Test
	public void doExtractJWT() throws JsonParseException, JsonMappingException, IOException {
		String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzUxMiJ9.eyJpc3MiOiJTeW1waG9ueSBDb21tdW5pY2F0aW9uIFNlcnZpY2VzIExMQy4iLCJzdWIiOjM0OTAyNjIyMjM0ODIxNywiYXVkIjoic3ltcGhvbnktdGFicyIsInVzZXIiOnsiaWQiOjM0OTAyNjIyMjM0ODIxNywiZW1haWxBZGRyZXNzIjoicm9iZXJ0Lm1vZmZhdEBkYi5jb20iLCJmaXJzdE5hbWUiOiJSb2JlcnQiLCJsYXN0TmFtZSI6Ik1vZmZhdHQiLCJkaXNwbGF5TmFtZSI6IlJvYmVydCBNb2ZmYXR0IiwiY29tcGFueSI6IlN5bXBob255IFBhcnRuZXIgRGV2ZWxvcG1lbnQiLCJjb21wYW55SWQiOjUwNzksInVzZXJuYW1lIjoicm9iZXJ0Lm1vZmZhdEBkYi5jb20iLCJsb2NhdGlvbiI6IkxvbmRvbiIsImF2YXRhclVybCI6Ii4uL2F2YXRhcnMvc3RhdGljLzE1MC9kZWZhdWx0LnBuZyIsImF2YXRhclNtYWxsVXJsIjoiLi4vYXZhdGFycy9zdGF0aWMvNTAvZGVmYXVsdC5wbmcifSwiZXhwIjoxNTY5NDAxMzc2fQ.OvoBn2gAGM6Mq5B9RhzdH8FAf1snRpVT-mQ15O9il-oeURlRfMl74GVTHEN_tS6qnCLwUXdk3f--aO9OVaaOD1DrHLgBmlK7W1w3oY8oxlUPcxdlj2qlaTK1Pz7e3OiNYFFp4ZophkEL11QrZVCu_E9Kv01HiIH5zHgI5FHJVzOGmgaEY7W2nUYRH9bKmcw2gwf5vlXOR88pBh5H9Px7FnsZBrMe_bZ5tjNTVVuSelbHKvIw0_NauB-lcdM8xe-yjpFpS_SxFbY9T3tigYyIr5r-fynBZOCofndm34z8dtT9TSbskHD4uNiWTHH2Txtg5nntwLKG4UXYa4yOJE5ZFg";
		long id = TableController.getIDFromJWT(jwt, om);
		Assert.assertEquals(349026222348217l, id);
	}
}
