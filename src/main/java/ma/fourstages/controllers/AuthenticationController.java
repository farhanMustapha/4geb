package ma.fourstages.controllers;

import lombok.RequiredArgsConstructor;
import ma.fourstages.config.security.jwt.JwtProvider;
import ma.fourstages.controllers.reponse.JwtResponse;
import ma.fourstages.controllers.reponse.ResponseMessage;
import ma.fourstages.controllers.request.LoginForm;
import ma.fourstages.controllers.request.SignUpForm;
import ma.fourstages.entities.security.Role;
import ma.fourstages.entities.security.RoleName;
import ma.fourstages.entities.security.User;
import ma.fourstages.repositories.RoleRepository;
import ma.fourstages.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginRequest) throws Exception {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new Exception("Unhandled error"));

        JwtResponse jwtResponse = JwtResponse.builder()
                .token(token)
                .type("Bearer")
                .username(user.getName())
                .email(user.getEmail())
                .authorities(userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody SignUpForm signUpRequest) throws Exception{
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Email déjà utilisé"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .name(signUpRequest.getName())
                .password(encoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .field(signUpRequest.getField())
                .studyLevel(signUpRequest.getStudyLevel())
                .build();

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: USER Role not find."));
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        return authenticateUser(LoginForm.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build());
    }

    @GetMapping("/user")
    public User getByUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Optional<User> users = this.userRepository.findByEmail(username);
        return users.orElse(null);

    }

}