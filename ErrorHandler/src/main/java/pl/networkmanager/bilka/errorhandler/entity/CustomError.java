package pl.networkmanager.bilka.errorhandler.entity;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomError extends RuntimeException {
    public abstract HttpStatus getStatusCode();

    private final String code;

    public CustomError(String message, String code) {
        super(message);
        this.code = code;
    }

    public CustomError(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}