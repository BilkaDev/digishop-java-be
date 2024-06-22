package pl.networkmanager.bilka.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableJpaRepositories
@EnableDiscoveryClient
@ComponentScan(basePackages = {"pl.networkmanager.bilka.auth", "pl.networkmanager.bilka.errorhandler"})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
