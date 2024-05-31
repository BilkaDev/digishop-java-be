package pl.networkmanager.bilka.auth.entity;

import lombok.Getter;

/**
 * Enum representing various operation codes.
 */
@Getter
public enum Code {
    SUCCESS("Operation end success");
    private final String label;

    Code(final String label) {
        this.label = label;
    }
}
