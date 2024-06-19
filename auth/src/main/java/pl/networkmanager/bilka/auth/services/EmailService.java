package pl.networkmanager.bilka.auth.services;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pl.networkmanager.bilka.auth.configuration.EmailConfiguration;
import pl.networkmanager.bilka.auth.entity.User;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final EmailConfiguration emailConfiguration;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("classpath:static/activate-mail.html")
    Resource activeTemplate;
    @Value("classpath:static/reset-password-mail.html")
    Resource recoverTemplate;

    public void sendActivationEmail(User user) {
        log.info("--START sendActivationEmail");
        try {
            String html = Files.toString(activeTemplate.getFile(), Charsets.UTF_8);
            html = html.replace("https://google.com", frontendUrl + "/activate/" + user.getUuid());
            emailConfiguration.sendMail(user.getEmail(), html, "Activate your account", true);

        } catch (IOException e) {
            log.info("Can't send mail");
            throw new RuntimeException(e);
        }
        log.info("--END sendActivationEmail");
    }

    public void sendResetPasswordRecovery(User user, String uid) {
        try {
            log.info("--START sendResetPasswordRecovery");
            String html = Files.toString(recoverTemplate.getFile(), Charsets.UTF_8);
            html = html.replace("https://google.com", frontendUrl + "/reset-password/" + uid);
            emailConfiguration.sendMail(user.getEmail(), html, "Reset your password", true);
        } catch (IOException e) {
            log.info("Can't send mail");
            throw new RuntimeException(e);
        }
        log.info("--END sendResetPasswordRecovery");
    }
}
