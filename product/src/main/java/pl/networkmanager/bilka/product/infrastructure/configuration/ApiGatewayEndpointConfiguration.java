package pl.networkmanager.bilka.product.infrastructure.configuration;

import jakarta.annotation.PostConstruct;
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
public class ApiGatewayEndpointConfiguration implements IApiGatewayEndpointConfiguration {
    @Value("${api-gateway.url}")
    private String API_GATEWAY_URL;

    @PostConstruct
    public void init() {
        initMap();
        register();
    }

    @Override
    public void initMap() {
        endpoints.add(new Endpoint("/api/v1/product", HttpMethod.GET, Role.GUEST));
        endpoints.add(new Endpoint("/api/v1/product", HttpMethod.POST, Role.ADMIN));
        endpoints.add(new Endpoint("/api/v1/product", HttpMethod.DELETE, Role.ADMIN));
        endpoints.add(new Endpoint("/api/v1/category", HttpMethod.GET, Role.GUEST));
        endpoints.add(new Endpoint("/api/v1/category", HttpMethod.POST, Role.ADMIN));
    }

    @Override
    public void register() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Response> response = restTemplate.postForEntity(API_GATEWAY_URL, endpoints, Response.class);
        if (response.getStatusCode().isError()) throw new RuntimeException();
    }
}
