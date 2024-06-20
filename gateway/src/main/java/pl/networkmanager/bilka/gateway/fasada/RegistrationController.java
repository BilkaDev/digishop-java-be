package pl.networkmanager.bilka.gateway.fasada;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.networkmanader.bilka.entity.Endpoint;
import pl.networkmanader.bilka.entity.Response;
import pl.networkmanager.bilka.gateway.filter.RouteValidator;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gateway")
@RequiredArgsConstructor
public class RegistrationController {
    private final RouteValidator routeValidator;

    @PostMapping
    public ResponseEntity<Response> register(@RequestBody List<Endpoint> endpoints) {
        routeValidator.addEndpoints(endpoints);
        return ResponseEntity.ok(new Response("Endpoints registered successfully"));
    }
}
