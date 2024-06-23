package pl.networkmanager.bilka.basket.domain.basketcrud.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import pl.networkmanager.bilka.errorhandler.entity.CustomError;

@Slf4j
public class BasketDontExistException extends CustomError {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    public BasketDontExistException(String message) {
        super(message, "BasketDontExistException");
        log.info(message);
    }
}
