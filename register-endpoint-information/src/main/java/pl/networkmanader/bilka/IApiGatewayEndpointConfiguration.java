package pl.networkmanader.bilka;

import pl.networkmanader.bilka.entity.Endpoint;

import java.util.ArrayList;
import java.util.List;

public interface IApiGatewayEndpointConfiguration {
    List<Endpoint> endpoints = new ArrayList<>();

    void initMap();

    void register();
}
