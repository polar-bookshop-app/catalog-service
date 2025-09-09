package com.github.polar.catalogservice.web;

import com.github.polar.catalogservice.config.PingConfig;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("")
@RestController
public class PingController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PingConfig pingConfig;

    public PingController(PingConfig pingConfig) {
        this.pingConfig = pingConfig;
    }

    @GetMapping("/ping")
    String ping() {
        LOGGER.debug("isVirtual: {}", Thread.currentThread().isVirtual());
        LOGGER.debug("Thread name: {}", Thread.currentThread().getName());
        return pingConfig.getMessage();
    }
}
