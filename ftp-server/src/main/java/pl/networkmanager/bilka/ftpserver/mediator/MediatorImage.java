package pl.networkmanager.bilka.ftpserver.mediator;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.networkmanager.bilka.ftpserver.entity.Image;
import pl.networkmanager.bilka.ftpserver.entity.ImageDto;
import pl.networkmanager.bilka.ftpserver.entity.ImageResponse;
import pl.networkmanager.bilka.ftpserver.exceptions.FtpConnectionException;
import pl.networkmanager.bilka.ftpserver.service.FtpService;
import pl.networkmanager.bilka.ftpserver.service.ImageService;

import java.io.IOException;
import java.util.Objects;

@Component
@AllArgsConstructor
public class MediatorImage {
    private final FtpService ftpService;
    private final ImageService imageService;

    public ResponseEntity<?> saveImage(MultipartFile file) {
        try {
            if (Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1).equals("png")) {
                Image image = ftpService.uploadFileToFtp(file);
                image = imageService.save(image);
                return ResponseEntity.ok(
                        ImageDto.builder()
                                .uuid(image.getUuid())
                                .createAt(image.getCreatedAt()).build());
            }
            return ResponseEntity.status(400).body(new ImageResponse("MediaType not supported"));
        } catch (IOException e) {
            return ResponseEntity.status(400).body(new ImageResponse("File dont exist"));
        } catch (FtpConnectionException e1) {
            return ResponseEntity.status(400).body(new ImageResponse("Cannot save file"));
        }

    }

    public ResponseEntity<ImageResponse> delete(String uuid) {
        try {
            Image image = imageService.findByUuid(uuid);
            if (image != null) {
                imageService.delete(image);
                ftpService.deleteFile(image.getPath());
                return ResponseEntity.ok(new ImageResponse("File deleted"));
            }
            return ResponseEntity.ok(new ImageResponse("File dont exist"));
        } catch (IOException e) {
            return ResponseEntity.status(400).body(new ImageResponse("Cannot delete file"));
        }
    }

    public ResponseEntity<?> getImage(String uuid) throws IOException {
        Image image = imageService.findByUuid(uuid);
        if (image != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(ftpService.getFile(image).toByteArray(), headers, HttpStatus.OK);
        }
        return ResponseEntity.status(400).body(new ImageResponse("File dont exist"));
    }

    public ResponseEntity<ImageResponse> activateImage(String uuid) {
        Image image = imageService.findByUuid(uuid);
        if (image != null) {
            image.setUsed(true);
            imageService.save(image);
            return ResponseEntity.ok(new ImageResponse("Image successfully activated"));
        }
        return ResponseEntity.status(400).body(new ImageResponse("File dont exist"));
    }
}

