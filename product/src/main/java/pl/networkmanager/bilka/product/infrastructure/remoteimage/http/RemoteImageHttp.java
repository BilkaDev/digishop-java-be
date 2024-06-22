package pl.networkmanager.bilka.product.infrastructure.remoteimage.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import pl.networkmanager.bilka.product.domen.productcrud.IImageClientFtp;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@RequiredArgsConstructor
public class RemoteImageHttp implements IImageClientFtp {
    private final RestTemplate restTemplate;

    @Value("${file-server.url}")
    private String FILE_SERVICE;

    @Override
    public void activeImage(String uuid) {
        log.info("Active image with uuid");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(FILE_SERVICE + "?uuid=" + uuid))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Image with uuid activated");
    }

    @Override
    public void deleteImage(String uuid) {
        log.info("Delete image with uuid");
        restTemplate.delete(FILE_SERVICE + "?uuid=" + uuid);
        log.info("Image with uuid deleted");
    }


}
