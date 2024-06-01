package pl.networkmanager.bilka.auth.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.networkmanager.bilka.auth.entity.*;
import pl.networkmanager.bilka.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;
    @Value("${jwt.exp}")
    private int EXP;
    @Value("${jwt.refreshExp}")
    private int REFRESH_EXP;

    private User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    public String generateToken(String username, int exp) {
        return jwtService.generateToken(username, exp);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public void register(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setLogin(userRegisterDTO.login());
        user.setPassword(userRegisterDTO.password());
        user.setEmail(userRegisterDTO.email());
        if (userRegisterDTO.role() != null) {
            user.setRole(userRegisterDTO.role());
        } else {
            user.setRole(Role.USER);
        }

        saveUser(user);
    }

    public ResponseEntity<?> login(HttpServletResponse response, User authRequest) {
        User user = userRepository.findUserByLogin(authRequest.getUsername()).orElse(null);
        if (user != null) {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                Cookie refresh = cookieService.generateCookie(
                        "refresh", generateToken(authRequest.getUsername(), REFRESH_EXP), REFRESH_EXP);
                Cookie cookie = cookieService.generateCookie(
                        "Authorization", generateToken(authRequest.getUsername(), EXP), EXP);
                response.addCookie(refresh);
                response.addCookie(cookie);
                return ResponseEntity.ok(
                        UserRegisterDTO
                                .builder()
                                .login(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build()
                );
            }
            return ResponseEntity.ok(new AuthResponse(Code.A1));
        }
        return ResponseEntity.ok(new AuthResponse(Code.A2));
    }
}
