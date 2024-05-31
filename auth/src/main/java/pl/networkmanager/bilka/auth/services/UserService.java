package pl.networkmanager.bilka.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.networkmanager.bilka.auth.entity.Role;
import pl.networkmanager.bilka.auth.entity.User;
import pl.networkmanager.bilka.auth.entity.UserRegisterDTO;
import pl.networkmanager.bilka.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
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
}
