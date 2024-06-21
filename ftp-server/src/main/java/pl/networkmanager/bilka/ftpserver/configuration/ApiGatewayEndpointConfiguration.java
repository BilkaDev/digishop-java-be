package pl.networkmanager.bilka.ftpserver.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.networkmanader.bilka.IApiGatewayEndpointConfiguration;
import pl.networkmanader.bilka.entity.Endpoint;
import pl.networkmanader.bilka.entity.HttpMethod;
import pl.networkmanader.bilka.entity.Response;
import pl.networkmanader.bilka.entity.Role;

@Configuration
@Slf4j
public class ApiGatewayEndpointConfiguration implements IApiGatewayEndpointConfiguration {
    @Value("${api-gateway.url}")
    private String GATEWAY_URL;

    @PostConstruct
    public void init() {
        initMap();
        register();
    }

    @Override
    public void initMap() {
        log.info("--START initMap endpoints");
        endpoints.add(new Endpoint("/api/v1/image", HttpMethod.GET, Role.GUEST));
        endpoints.add(new Endpoint("/api/v1/image", HttpMethod.POST, Role.ADMIN));
        endpoints.add(new Endpoint("/api/v1/image", HttpMethod.PATCH, Role.ADMIN));
        endpoints.add(new Endpoint("/api/v1/image", HttpMethod.DELETE, Role.ADMIN));
        log.info("--END initMap endpoints");
    }

    @Override
    public void register() {
        log.info("--START register endpoints");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Response> response = restTemplate
                .postForEntity(GATEWAY_URL, endpoints, Response.class);
        if (response.getStatusCode().isError()) {
            log.error("Error while registering endpoints: {}", response.getStatusCode());
            throw new RuntimeException("Error while registering endpoints");
        }
        log.info("--END register endpoints");
    }
}
