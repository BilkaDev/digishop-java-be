package pl.networkmanager.bilka.ftpserver.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ImageDto {
    private String uuid;
    private LocalDate createAt;
}

