package com.example.full;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.logging.Level;
import java.util.logging.Logger;

@EnableJpaAuditing
@SpringBootApplication
public class FullApplication {

    public final static Logger log = Logger.getGlobal();

    public static void main(String[] args) {
        log.setLevel(Level.INFO);
        SpringApplication.run(FullApplication.class, args);
    }

}
