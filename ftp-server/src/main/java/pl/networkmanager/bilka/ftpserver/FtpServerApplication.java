package pl.networkmanager.bilka.ftpserver;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient
public class FtpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtpServerApplication.class, args);
    }

}
