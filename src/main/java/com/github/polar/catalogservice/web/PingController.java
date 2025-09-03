package com.github.polar.catalogservice.web;

import com.github.polar.catalogservice.config.PingConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("")
@RestController
public class PingController {

    private final PingConfig pingConfig;

    public PingController(PingConfig pingConfig) {
        this.pingConfig = pingConfig;
    }

    @GetMapping("/ping")
    String ping() {
        System.out.printf("isVirtual: %b%n", Thread.currentThread().isVirtual());
        System.out.printf("Thread name: %s%n", Thread.currentThread().getName());
        return pingConfig.getMessage();
    }
}
