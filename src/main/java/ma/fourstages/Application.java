package ma.fourstages;

import ma.fourstages.entities.security.Role;

import ma.fourstages.entities.security.RoleName;
import ma.fourstages.entities.security.User;
import ma.fourstages.repositories.RoleRepository;
import ma.fourstages.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        UserRepository userRepository = ctx.getBean(UserRepository.class);
        RoleRepository roleRepository = ctx.getBean(RoleRepository.class);
        roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(Role.builder()
                .name(RoleName.ROLE_ADMIN)
                .build()));
        roleRepository.findByName(RoleName.ROLE_USER)
                .orElseGet(() -> roleRepository.save(Role.builder()
                .name(RoleName.ROLE_USER)
                .build()));
        if (!userRepository.existsByEmail("admin@4stages.ma")) {
            PasswordEncoder encoder = ctx.getBean(PasswordEncoder.class);
            roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .ifPresent(role -> {
                        User admin = User.builder()
                                .name("admin")
                                .email("admin@4stages.ma")
                                .password(encoder.encode("admin"))
                                .roles(new HashSet<>(Collections.singletonList(role)))
                                .build();
                        userRepository.save(admin);
                    });
        }
    }

}
