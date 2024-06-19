package pl.networkmanager.bilka.auth.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import pl.networkmanager.bilka.auth.entity.Code;
import pl.networkmanager.bilka.errorhandler.entity.CustomError;

@Slf4j
public class UserDontExistException extends CustomError {
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.UNAUTHORIZED;
    }

    public UserDontExistException(String message) {
        super(message, Code.A6.name());
        getInfoUserDontExistInDatabase();
    }


    public UserDontExistException(String message, Code code) {
        super(message, code.name());
        getInfoUserDontExistInDatabase();
    }

    public UserDontExistException(Code code) {
        super(code.getLabel(), code.name());
        getInfoUserDontExistInDatabase();
    }

    private static void getInfoUserDontExistInDatabase() {
        log.info("User dont exist in database");
    }
}
