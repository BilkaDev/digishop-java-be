package pl.networkmanager.bilka.ftpserver.fasada;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.networkmanager.bilka.ftpserver.entity.ImageResponse;
import pl.networkmanager.bilka.ftpserver.mediator.MediatorImage;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {
    private final MediatorImage mediatorImage;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveFile(@RequestParam MultipartFile multipartFile) {
        return mediatorImage.saveImage(multipartFile);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<ImageResponse> deleteFile(@RequestParam String uuid) {
        return mediatorImage.delete(uuid);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getFile(@RequestParam String uuid) throws IOException {
        return mediatorImage.getImage(uuid);
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<ImageResponse> activateImage(@RequestParam String uuid) {
        return mediatorImage.activateImage(uuid);
    }

}
