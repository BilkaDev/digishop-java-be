package pl.networkmanager.bilka.auth.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import pl.networkmanager.bilka.auth.entity.Code;
import pl.networkmanager.bilka.errorhandler.entity.CustomError;

@Slf4j
public class UserExistingWithLogin extends CustomError {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    public UserExistingWithLogin(String message) {
        super(message, Code.A4.name());
        log.info(message);
    }
}
