package pl.networkmanager.bilka.errorhandler.entity;

import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;


@Getter
public class ErrorResponse {
    private final String timestamp;
    private final List<String> messages;
    private final String code;

    public ErrorResponse(List<String> message) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.messages = message;
        this.code = null;
    }

    public ErrorResponse(List<String> message, String code) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.messages = message;
        this.code = code;
    }
}
