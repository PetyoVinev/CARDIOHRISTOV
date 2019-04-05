package org.vinevweb.cardiohristov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class CardiohristovApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CardiohristovApplication.class, args);
    }

}

