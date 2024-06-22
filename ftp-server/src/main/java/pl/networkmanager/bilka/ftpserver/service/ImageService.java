package pl.networkmanager.bilka.ftpserver.service;


import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.networkmanager.bilka.ftpserver.entity.Image;
import pl.networkmanager.bilka.ftpserver.repository.ImageRepository;

import java.io.IOException;

@Service
@EnableScheduling
@AllArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final FtpService ftpService;

    public Image save(Image imageEntity) {
        return imageRepository.saveAndFlush(imageEntity);
    }

    public Image findByUuid(String uuid) {
        return imageRepository.findByUuid(uuid).orElse(null);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanImages() {
        imageRepository.findDontUseImages().forEach(value -> {
            try {
                ftpService.deleteFile(value.getPath());
                imageRepository.delete(value);
            } catch (IOException e) {
                System.out.println("Cant delete " + value.getUuid());
            }
        });
    }

    public void delete(Image imageEntity) {
        imageRepository.delete(imageEntity);
    }
}
