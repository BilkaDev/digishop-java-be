package pl.networkmanager.bilka.auth.entity;

import lombok.Getter;

/**
 * Enum representing various operation codes.
 */
@Getter
public enum Code {
    SUCCESS("Operation end success"),
    PERMIT("Access granted"),
    A1("The specified user with the given name does not exist or hast not activated the account"),
    A2("The specified data is invalid"),
    A3("The indicated token is empty or not valid"),
    A4("The user with the specified name does not exist"),
    A5("The user with the specified email already exists"),
    A6("The user does not exist");
    private final String label;

    Code(final String label) {
        this.label = label;
    }
}
