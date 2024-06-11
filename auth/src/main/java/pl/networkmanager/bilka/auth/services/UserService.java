package pl.networkmanager.bilka.auth.services;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.networkmanager.bilka.auth.entity.*;
import pl.networkmanager.bilka.auth.exceptions.UserDontExistException;
import pl.networkmanager.bilka.auth.exceptions.UserExistingWithLogin;
import pl.networkmanager.bilka.auth.exceptions.UserExistingWithMail;
import pl.networkmanager.bilka.auth.repository.ResetOperationsRepository;
import pl.networkmanager.bilka.auth.repository.UserRepository;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;
    private final EmailService emailService;
    private final ResetOperationService resetOperationService;
    private final ResetOperationsRepository resetOperationsRepository;
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

    public void validateToken(HttpServletRequest request, HttpServletResponse response) throws ExpiredJwtException, IllegalArgumentException {
        String token = null;
        String refresh = null;
        if (request.getCookies() != null) {
            for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
                if (value.getName().equals("Authorization")) {
                    token = value.getValue();
                } else if (value.getName().equals("refresh")) {
                    refresh = value.getValue();
                }
            }
        } else {
            throw new IllegalArgumentException("Token can't be null");
        }
        try {
            jwtService.validateToken(token);
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            jwtService.validateToken(refresh);
            Cookie refreshCokkie = cookieService.generateCookie("refresh", jwtService.refreshToken(refresh, REFRESH_EXP), REFRESH_EXP);
            Cookie cookie = cookieService.generateCookie("Authorization", jwtService.refreshToken(refresh, EXP), EXP);
            response.addCookie(cookie);
            response.addCookie(refreshCokkie);
        }

    }


    public void register(UserRegisterDTO userRegisterDTO) throws UserExistingWithLogin, UserExistingWithMail {
        userRepository.findUserByLogin(userRegisterDTO.login()).ifPresent(_ -> {
            throw new UserExistingWithLogin("user with the given name already exists");
        });

        userRepository.findUserByEmail(userRegisterDTO.email()).ifPresent(_ -> {
            throw new UserExistingWithMail("user with the given email already exists");
        });

        User user = new User();
        user.setLock(true);
        user.setLogin(userRegisterDTO.login());
        user.setPassword(userRegisterDTO.password());
        user.setEmail(userRegisterDTO.email());
        if (userRegisterDTO.role() != null) {
            user.setRole(userRegisterDTO.role());
        } else {
            user.setRole(Role.USER);
        }

        saveUser(user);
        emailService.sendActivationEmail(user);
    }

    public ResponseEntity<?> login(HttpServletResponse response, User authRequest) {
        User user = userRepository.findUserByLoginAndLockAndEnabled(authRequest.getUsername()).orElse(null);
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

    public ResponseEntity<?> loginByToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request, response);
            String refresh = null;
            for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
                if (value.getName().equals("refresh")) {
                    refresh = value.getValue();
                }
            }
            String login = jwtService.getSubject(refresh);
            User user = userRepository.findUserByLoginAndLockAndEnabled(login).orElse(null);
            if (user != null) {
                return ResponseEntity.ok(
                        UserRegisterDTO
                                .builder()
                                .login(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .build()
                );
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.A1));
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.A3));
        }
    }

    public ResponseEntity<LoginResponse> loggedIn(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateToken(request, response);
            return ResponseEntity.ok(new LoginResponse(true));
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            return ResponseEntity.ok(new LoginResponse(false));
        }
    }

    public void activateUser(String uid) throws UserDontExistException {
        User user = userRepository.findUserByUuid(uid).orElse(null);
        if (user != null) {
            user.setLock(false);
            userRepository.save(user);
            return;
        }
        throw new UserDontExistException("User with the given uuid doesn't exist");
    }

    public void recoverPassword(String email) throws UserDontExistException {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user != null) {
            ResetOperations resetOperation = resetOperationService.initResetOperation(user);
            emailService.sendResetPasswordRecovery(user, resetOperation.getUid());
            return;
        }
        throw new UserDontExistException("User with the given email doesn't exist");
    }

    @Transactional
    public void resetPassword(ChangePasswordData changePasswordData) throws UserDontExistException {
        ResetOperations resetOperations = resetOperationsRepository.findByUid(changePasswordData.getUid()).orElse(null);
        if (resetOperations != null) {
            User user = userRepository.findUserByUuid(
                    resetOperations.getUser().getUuid()).orElse(null);
            if (user != null) {
                user.setPassword(changePasswordData.getPassword());
                saveUser(user);
                resetOperationService.endOperation(resetOperations.getUid());
                return;
            }
        }
        throw new UserDontExistException("User with the given uuid doesn't exist");

    }
}
