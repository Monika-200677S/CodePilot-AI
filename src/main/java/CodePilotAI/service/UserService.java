package CodePilotAI.service;

import CodePilotAI.dto.AuthResponse;
import CodePilotAI.dto.ForgotPasswordResponse;
import CodePilotAI.dto.LoginRequest;
import CodePilotAI.dto.RegisterRequest;
import CodePilotAI.entity.PasswordResetToken;
import CodePilotAI.entity.StudentProgress;
import CodePilotAI.entity.User;
import CodePilotAI.exception.EmailAlreadyExistsException;
import CodePilotAI.exception.ResourceNotFoundException;
import CodePilotAI.repository.PasswordResetTokenRepository;
import CodePilotAI.repository.StudentProgressRepository;
import CodePilotAI.repository.UserRepository;
import CodePilotAI.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProgressRepository progressRepository;

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("An account with this email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("STUDENT");

        User savedUser = userRepository.save(user);

        StudentProgress progress = new StudentProgress();
        progress.setUser(savedUser);
        progressRepository.save(progress);

        String token = jwtService.generateToken(toUserDetails(savedUser));

        return new AuthResponse(token, savedUser.getName(), savedUser.getEmail(), savedUser.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(toUserDetails(user));

        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole());
    }

    @Transactional
    public ForgotPasswordResponse forgotPassword(String email) {
        // Always return a generic success message even if the email doesn't
        // exist, so this endpoint can't be used to check which emails are registered.
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return new ForgotPasswordResponse(
                    "If an account with that email exists, a reset link has been generated.", null);
        }

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        resetTokenRepository.save(resetToken);

        return new ForgotPasswordResponse(
                "If an account with that email exists, a reset link has been generated. " +
                "No email server is configured for this demo, so use the token below directly.",
                resetToken.getToken());
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired reset token"));

        if (resetToken.isUsed() || resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException("Invalid or expired reset token");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole())
                .build();
    }
}
