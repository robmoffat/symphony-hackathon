package com.db.symphonyp.tabs.botClient75;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.db.symphonyp.tabs.botClient75", "com.db.symphonyp.tabs.common"})
public class SymphonyTabsApp75 {
    public static void main(String [] args) {
        SpringApplication.run(SymphonyTabsApp75.class, args);
    }

}
