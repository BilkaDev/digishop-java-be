package pl.networkmanager.bilka.auth.entity;


import java.sql.Timestamp;

import lombok.Getter;

@Getter
public class AuthResponse {
    private final String timestamp;
    private final String message;
    private final Code code;

    public AuthResponse(Code code) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.message = code.getLabel();
        this.code = code;
    }
}
