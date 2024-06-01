package pl.networkmanager.bilka.auth.exceptions;

public class UserExistingWithMail extends RuntimeException {
    public UserExistingWithMail(String message) {
        super(message);
    }

    public UserExistingWithMail(Throwable cause) {
        super(cause);
    }

    public UserExistingWithMail(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
