package pl.networkmanager.bilka.auth.services;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            log.info("Can't login because token is empty");
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
        user.setEnabled(false);
        user.setLogin(userRegisterDTO.login());
        user.setPassword(userRegisterDTO.password());
        user.setEmail(userRegisterDTO.email());
        user.setRole(Role.USER);


        saveUser(user);
        emailService.sendActivationEmail(user);
    }

    public ResponseEntity<?> login(HttpServletResponse response, User authRequest) {
        log.info("--START loginService");
        User user = userRepository.findUserByLoginAndLockAndEnabled(authRequest.getUsername())
                .orElseThrow(() -> new UserDontExistException(Code.A2));
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (!authenticate.isAuthenticated()) {
            log.info("--STOP loginService");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(Code.A1));
        }

        Cookie refresh = cookieService.generateCookie(
                "refresh", generateToken(authRequest.getUsername(), REFRESH_EXP), REFRESH_EXP);
        Cookie cookie = cookieService.generateCookie(
                "Authorization", generateToken(authRequest.getUsername(), EXP), EXP);
        response.addCookie(refresh);
        response.addCookie(cookie);
        log.info("--STOP loginService");
        return ResponseEntity.ok(
                UserRegisterDTO
                        .builder()
                        .login(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build()
        );
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
            User user = userRepository.findUserByLoginAndLockAndEnabled(login)
                    .orElseThrow(() -> new UserDontExistException("User with the given login doesn't exist", Code.A1));
            return ResponseEntity.ok(
                    UserRegisterDTO
                            .builder()
                            .login(user.getUsername())
                            .email(user.getEmail())
                            .role(user.getRole())
                            .build()
            );
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            log.info("Can't login because token expired or is empty");
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
        User user = userRepository.findUserByUuid(uid)
                .orElseThrow(() -> new UserDontExistException("User with the given uuid doesn't exist"));
        user.setLock(false);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void recoverPassword(String email) throws UserDontExistException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserDontExistException("User with the given email doesn't exist"));
        ResetOperations resetOperation = resetOperationService.initResetOperation(user);
        emailService.sendResetPasswordRecovery(user, resetOperation.getUid());
    }

    @Transactional
    public void resetPassword(ChangePasswordData changePasswordData) throws UserDontExistException {
        ResetOperations resetOperations = resetOperationsRepository.findByUid(changePasswordData.getUid())
                .orElseThrow(() -> new UserDontExistException("User with the given uuid doesn't exist"));
        User user = userRepository.findUserByUuid(resetOperations.getUser().getUuid())
                .orElseThrow(() -> new UserDontExistException("User with the given uuid doesn't exist"));

        user.setPassword(changePasswordData.getPassword());
        saveUser(user);
        resetOperationService.endOperation(resetOperations.getUid());
    }

    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Logout: Delete all cookies");
        Cookie cookie = cookieService.removeCookie(request.getCookies(), "Authorization");
        if (cookie != null) {
            response.addCookie(cookie);
        }
        cookie = cookieService.removeCookie(request.getCookies(), "refresh");
        if (cookie != null) {
            response.addCookie(cookie);
        }
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }

}
