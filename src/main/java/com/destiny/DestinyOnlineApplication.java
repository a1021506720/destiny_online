package com.destiny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DestinyOnlineApplication {

    // 应用入口（main函数）。在 Go 里类似于 package main 下的 main 函数
    public static void main(String[] args) {
        SpringApplication.run(DestinyOnlineApplication.class, args);
    }
}


