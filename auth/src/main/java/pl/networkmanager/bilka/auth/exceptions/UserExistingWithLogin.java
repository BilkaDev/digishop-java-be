package pl.networkmanager.bilka.auth.exceptions;

import org.springframework.http.HttpStatus;
import pl.networkmanager.bilka.auth.entity.Code;
import pl.networkmanager.bilka.errorhandler.entity.CustomError;

public class UserExistingWithLogin extends CustomError {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    public UserExistingWithLogin(String message) {
        super(message, Code.A4.name());
    }
}
