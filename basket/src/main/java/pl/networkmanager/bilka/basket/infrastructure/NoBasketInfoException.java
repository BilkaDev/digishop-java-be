package pl.networkmanager.bilka.basket.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import pl.networkmanager.bilka.errorhandler.entity.CustomError;

@Slf4j
public class NoBasketInfoException extends CustomError {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    public NoBasketInfoException(String message) {
        super(message, "NoBasketInfoException");
        log.info(message);
    }
}
