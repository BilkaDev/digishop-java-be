package pl.networkmanager.bilka.product.infrastructure.remoteimage.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.networkmanager.bilka.product.domen.productcrud.IImageClientFtp;

@Configuration
public class RemoteImageConfiguration {

    @Bean
    public IImageClientFtp imageServerFtp() {
        return new RemoteImageHttp(new RestTemplate());
    }
}
