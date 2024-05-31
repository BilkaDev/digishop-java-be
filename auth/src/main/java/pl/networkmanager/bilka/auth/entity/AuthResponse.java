package pl.networkmanager.bilka.auth.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class AuthResponse {
    private final String timestamp;
    private final String message;
    private Code code;

    public AuthResponse(Code code) {
        this.timestamp = new Timestamp(new Timestamp(System.currentTimeMillis()).getTime()).toString();
        this.message = code.getLabel();
        this.code = code;
    }
}
