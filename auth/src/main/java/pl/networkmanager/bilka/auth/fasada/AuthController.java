package pl.networkmanager.bilka.auth.fasada;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.networkmanager.bilka.auth.entity.AuthResponse;
import pl.networkmanager.bilka.auth.entity.Code;
import pl.networkmanager.bilka.auth.entity.User;
import pl.networkmanager.bilka.auth.entity.UserRegisterDTO;
import pl.networkmanager.bilka.auth.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> register(@RequestBody UserRegisterDTO user) {
        userService.register(user);
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        return userService.login(response, user);
    }

}
