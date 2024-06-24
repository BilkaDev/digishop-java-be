package pl.networkmanager.bilka.order.infrastructure.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.networkmanader.bilka.IApiGatewayEndpointConfiguration;
import pl.networkmanader.bilka.entity.Endpoint;
import pl.networkmanader.bilka.entity.Response;
import pl.networkmanader.bilka.entity.HttpMethod;
import pl.networkmanader.bilka.entity.Role;
import org.springframework.web.client.RestTemplate;

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
        endpoints.add(new Endpoint("/api/v1/order", HttpMethod.POST, Role.GUEST));
        endpoints.add(new Endpoint("/api/v1/order", HttpMethod.GET, Role.GUEST));
    }

    @Override
    public void register() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Response> response = restTemplate.postForEntity(GATEWAY_URL, endpoints, Response.class);
        if (response.getStatusCode().isError()) throw new RuntimeException();
    }
}

