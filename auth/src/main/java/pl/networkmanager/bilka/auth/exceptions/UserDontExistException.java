package pl.networkmanager.bilka.auth.exceptions;

import org.springframework.http.HttpStatus;
import pl.networkmanager.bilka.auth.entity.Code;
import pl.networkmanager.bilka.errorhandler.entity.CustomError;

public class UserDontExistException extends CustomError {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.UNAUTHORIZED;
    }

    public UserDontExistException(String message) {
        super(message, Code.A6.name());
    }
}
