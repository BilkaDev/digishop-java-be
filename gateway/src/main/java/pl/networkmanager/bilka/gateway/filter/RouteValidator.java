package pl.networkmanager.bilka.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/validate",
            "/auth/reset-password",
            "/auth/activate"
    );

    public Predicate<ServerHttpRequest> isSecure =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI()
                            .getPath()
                            .contains(uri));
}
