package com.db.symphonyp.tabs.config;

import clients.SymBotClient;
import com.db.symphonyp.tabs.IMListenerImpl;
import com.db.symphonyp.tabs.RoomListenerImpl;
import listeners.DatafeedListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    @Bean
    public SymBotClient botClient() {
        return SymBotClient.initBotRsa("config.json");
    }

    @Bean
    public DatafeedListener imListenerImpl(SymBotClient botClient) {
        return new IMListenerImpl(botClient);
    }

    @Bean
    public DatafeedListener roomListenerImpl(SymBotClient botClient) {
        return new RoomListenerImpl(botClient);
    }

}
