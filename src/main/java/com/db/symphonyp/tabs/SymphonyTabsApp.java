package com.db.symphonyp.tabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.db.symphonyp.tabs.app", "com.db.symphonyp.tabs.common", "com.db.symphonyp.tabs.botClient76"})
public class SymphonyTabsApp {
    public static void main(String [] args) {
        SpringApplication.run(SymphonyTabsApp.class, args);
    }

}
