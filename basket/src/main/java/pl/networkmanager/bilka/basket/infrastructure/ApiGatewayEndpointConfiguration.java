package pl.networkmanager.bilka.basket.infrastructure;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.networkmanader.bilka.IApiGatewayEndpointConfiguration;
import pl.networkmanader.bilka.entity.Endpoint;
import pl.networkmanader.bilka.entity.HttpMethod;
import pl.networkmanader.bilka.entity.Role;

@Component
public class ApiGatewayEndpointConfiguration implements IApiGatewayEndpointConfiguration {

    @Value("${api-gateway.url}")
    private String GATEWAY_URL;

    @PostConstruct
    public void startOperation() {
        initMap();
        register();
    }

    @Override
    public void initMap() {
        endpoints.add(new Endpoint("/api/v1/basket", HttpMethod.GET, Role.GUEST));
        endpoints.add(new Endpoint("/api/v1/basket", HttpMethod.POST, Role.GUEST));
        endpoints.add(new Endpoint("/api/v1/basket", HttpMethod.DELETE, Role.GUEST));
    }

    @Override
    public void register() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Response> response = restTemplate.postForEntity(GATEWAY_URL, endpoints, Response.class);
        if (response.getStatusCode().isError()) throw new RuntimeException();
    }
}
