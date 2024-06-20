package pl.networkmanager.bilka.ftpserver.entity;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class ImageResponse {

    private final String timestamp;
    private final String message;

    public ImageResponse(String message) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.message = message;
    }
}
