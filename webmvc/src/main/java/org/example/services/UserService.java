package org.example.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.dtos.user.ForgotPasswordDTO;
import org.example.dtos.user.ResetPasswordDTO;
import org.example.dtos.user.UserRegisterDTO;
import org.example.entities.UserEntity;
import org.example.mappers.UserMapper;
import org.example.repositories.IUserRepository;
import org.example.smtp.EmailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserRegisterDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity user = userMapper.fromCreateDto(userDTO);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    public boolean forgotPassword(ForgotPasswordDTO dto, HttpServletRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isEmpty()) {
            return false;
        }

        UserEntity user = userOpt.get();

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        userRepository.save(user);

        String siteUrl = getSiteUrl(request);

        String resetLink = siteUrl + "/users/reset-password?token=" + token;

        String subject = "Відновлення паролю";
        String body = """
            <div style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 30px;">
                <div style="max-width: 600px; margin: auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                    <div style="background: #007bff; color: white; padding: 15px; text-align: center;">
                        <h2>Відновлення паролю</h2>
                    </div>
                    <div style="padding: 20px;">
                        <p>Вітаємо, <strong>%s</strong>!</p>
                        <p>Ми отримали запит на відновлення вашого паролю. Натисніть кнопку нижче, щоб задати новий пароль:</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" style="background-color: #007bff; color: white; padding: 12px 25px; border-radius: 5px; text-decoration: none; font-size: 16px;">Скинути пароль</a>
                        </div>
                        <p>Або скопіюйте це посилання у браузер:</p>
                        <p><a href="%s">%s</a></p>
                        <p style="color: #888;">Якщо ви не надсилали запит, просто ігноруйте цей лист.</p>
                    </div>
                    <div style="background: #f0f0f0; color: #555; padding: 10px; text-align: center; font-size: 12px;">
                        © %d Your Company. Усі права захищено.
                    </div>
                </div>
            </div>
            """.formatted(user.getUsername(), resetLink, resetLink, resetLink, Calendar.getInstance().get(Calendar.YEAR));

        EmailMessage email = new EmailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setBody(body);

        SmtpService smtpService = new SmtpService();
        boolean sent = smtpService.sendEmail(email);

        if (sent) {
            System.out.println("Reset password email sent to " + user.getEmail());
        } else {
            System.err.println("Failed to send email to " + user.getEmail());
        }

        return sent;
    }

    private String getSiteUrl(HttpServletRequest request) {
        String scheme = request.getScheme();             // http або https
        String serverName = request.getServerName();     // localhost або myapp.com
        int serverPort = request.getServerPort();        // 8080 або 443
        String contextPath = request.getContextPath();   // якщо додаток не в корені

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if ((scheme.equals("http") && serverPort != 80) ||
                (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath);
        return url.toString();
    }

    public boolean resetPassword(ResetPasswordDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return false;
        }

        Optional<UserEntity> userOpt = userRepository.findByResetPasswordToken(dto.getToken());
        if (userOpt.isEmpty()) {
            return false;
        }

        UserEntity user = userOpt.get();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setResetPasswordToken(null);
        userRepository.save(user);

        return true;
    }
}
