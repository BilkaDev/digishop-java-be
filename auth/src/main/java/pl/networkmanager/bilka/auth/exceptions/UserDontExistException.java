package pl.networkmanager.bilka.auth.exceptions;

public class UserDontExistException extends RuntimeException {
    public UserDontExistException(String message) {
        super(message);
    }

    public UserDontExistException(Throwable cause) {
        super(cause);
    }

    public UserDontExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
