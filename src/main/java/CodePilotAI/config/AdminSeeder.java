package CodePilotAI.config;

import CodePilotAI.entity.StudentProgress;
import CodePilotAI.entity.User;
import CodePilotAI.repository.StudentProgressRepository;
import CodePilotAI.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

// Seeds one default admin account on first run so there's a way into the
// admin panel without manually editing the database. Change this password
// immediately in any real deployment.
@Configuration
public class AdminSeeder {

    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository,
                                        StudentProgressRepository progressRepository,
                                        PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@codepilot.ai";
            if (userRepository.existsByEmail(adminEmail)) {
                return;
            }

            User admin = new User();
            admin.setName("Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole("ADMIN");
            User saved = userRepository.save(admin);

            StudentProgress progress = new StudentProgress();
            progress.setUser(saved);
            progressRepository.save(progress);

            System.out.println("=================================================");
            System.out.println(" Default admin account created:");
            System.out.println("   email:    " + adminEmail);
            System.out.println("   password: Admin@123");
            System.out.println(" Change this password before any real deployment.");
            System.out.println("=================================================");
        };
    }
}
