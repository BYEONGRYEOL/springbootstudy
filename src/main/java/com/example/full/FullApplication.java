package com.example.full;

import com.example.full.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@EnableJpaAuditing
@SpringBootApplication
public class FullApplication {

    public final static Logger log = Logger.getGlobal();



    Properties properties = new Properties();
    public static void main(String[] args) {
        String getmactest = Config.GetMac();
        System.out.println(getmactest);
        log.setLevel(Level.INFO);
        SpringApplication.run(FullApplication.class, args);
    }

}
