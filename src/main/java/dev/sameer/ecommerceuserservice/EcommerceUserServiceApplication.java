package dev.sameer.ecommerceuserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class EcommerceUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceUserServiceApplication.class, args);
    }

}
