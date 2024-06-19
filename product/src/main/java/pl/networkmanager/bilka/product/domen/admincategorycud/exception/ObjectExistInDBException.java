package pl.networkmanager.bilka.product.domen.admincategorycud.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import pl.networkmanager.bilka.errorhandler.entity.CustomError;

@Slf4j
public class ObjectExistInDBException extends CustomError {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    public ObjectExistInDBException(String message) {
        super(message, "BAD_REQUEST");
        log.info(message);
    }
}
