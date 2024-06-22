package pl.networkmanager.bilka.basket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableDiscoveryClient
@ComponentScan(basePackages = {"pl.networkmanager.bilka.basket", "pl.networkmanager.bilka.errorhandler"})
public class BasketApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasketApplication.class, args);
    }

}
