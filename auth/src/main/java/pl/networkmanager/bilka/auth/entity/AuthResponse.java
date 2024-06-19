package pl.networkmanager.bilka.auth.entity;


import pl.networkmanager.bilka.errorhandler.entity.ErrorResponse;

import java.util.List;

public class AuthResponse extends ErrorResponse {
    public AuthResponse(Code code) {
        super(List.of(code.getLabel()), code.name());
    }
}
