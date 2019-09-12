package com.db.symphonyp.tabs.config;

import clients.SymBotClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    @Bean
    public SymBotClient botClient75() {
        return SymBotClient.initBotRsa("config75.json");
    }

    @Bean
    public SymBotClient botClient77() {
        return SymBotClient.initBotRsa("config77.json");
    }

}
