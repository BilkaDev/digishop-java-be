package pl.networkmanager.bilka.basket.domain.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import pl.networkmanager.bilka.errorhandler.entity.CustomError;

@Slf4j
public class ProductDontExistsException extends CustomError {
    public ProductDontExistsException(String productNotFound) {
        super(productNotFound, "ProductDontExists");
        log.info(productNotFound);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
