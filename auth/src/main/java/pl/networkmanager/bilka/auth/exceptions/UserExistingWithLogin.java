package pl.networkmanager.bilka.auth.exceptions;

public class UserExistingWithLogin extends RuntimeException {
    public UserExistingWithLogin(String message) {
        super(message);
    }

    public UserExistingWithLogin(Throwable cause) {
        super(cause);
    }

    public UserExistingWithLogin(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
