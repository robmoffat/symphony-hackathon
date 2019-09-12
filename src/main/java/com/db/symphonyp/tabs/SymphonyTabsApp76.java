package com.db.symphonyp.tabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.db.symphonyp.tabs.botClient76", "com.db.symphonyp.tabs.common"})
public class SymphonyTabsApp76 {
    public static void main(String[] args) {
        SpringApplication.run(SymphonyTabsApp76.class, args);
    }
}
