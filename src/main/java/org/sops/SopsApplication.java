package org.sops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication()
public class SopsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SopsApplication.class, args);
    }

}
