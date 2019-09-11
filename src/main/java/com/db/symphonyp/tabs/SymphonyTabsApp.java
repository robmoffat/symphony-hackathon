package com.db.symphonyp.tabs;

import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import clients.SymBotClient;
import model.OutboundMessage;

@SpringBootApplication
public class SymphonyTabsApp {
	
	@Value("${symphony.stream-id:}")
	String infoStreamId;
	
    public static void main(String [] args) {
        SpringApplication.run(SymphonyTabsApp.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/bundle-local.json").allowedOrigins("*");
                registry.addMapping("/bundle-linode.json").allowedOrigins("*");
                registry.addMapping("/bundle-admin.json").allowedOrigins("*");
            }
        };
    }
    
    @EventListener(ApplicationReadyEvent.class) 
    public void doSomethingAfterStartup() {
    	SymBotClient botClient = SymBotClient.initBotRsa("config.json");
    	if (!StringHelper.isNullOrEmptyString(infoStreamId)) {
    		OutboundMessage mes = new OutboundMessage("Started Symphony Tabs");
    		botClient.getMessagesClient().sendMessage(infoStreamId, mes);
    	}
    }
    
}
