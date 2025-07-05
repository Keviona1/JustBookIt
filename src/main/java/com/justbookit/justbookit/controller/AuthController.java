package com.justbookit.justbookit.controller;

import com.justbookit.justbookit.dto.AuthResponse;
import com.justbookit.justbookit.dto.LoginRequest;
import com.justbookit.justbookit.dto.UserRegistrationDTO;
import com.justbookit.justbookit.model.Role;
import com.justbookit.justbookit.model.User;
import com.justbookit.justbookit.repository.RoleRepository;
import com.justbookit.justbookit.repository.UserRepository;
import com.justbookit.justbookit.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil,
                          UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found after authentication"));
            String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(token, user));

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    } @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDTO dto) {
        logger.info("--- ENTERED /register ENDPOINT ---");
        logger.info("Received registration request for username: {}", dto.getUsername());

        try {
            logger.info("Step 1: Checking if username '{}' exists...", dto.getUsername());
            if (userRepository.existsByUsername(dto.getUsername())) {
                logger.warn("Validation FAILED: Username '{}' is already taken.", dto.getUsername());
                return ResponseEntity.badRequest().body("Error: Username is already taken!");
            }
            logger.info("SUCCESS: Username is available.");

            logger.info("Step 2: Checking if email '{}' exists...", dto.getEmail());
            if (userRepository.existsByEmail(dto.getEmail())) {
                logger.warn("Validation FAILED: Email '{}' is already in use.", dto.getEmail());
                return ResponseEntity.badRequest().body("Error: Email is already in use!");
            }
            logger.info("SUCCESS: Email is available.");

            logger.info("Step 3: Creating new User object in memory.");
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setEmail(dto.getEmail());
            user.setFullName(dto.getFullName());
            user.setPhone(dto.getPhone());
            user.setEnabled(true);
            logger.info("SUCCESS: User object created: {}", user);

            logger.info("Step 4: Attempting to find role 'USER' in the database...");
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> {
                        logger.error("FATAL: Role 'USER' could not be found in the database. This is the cause of the failure.");
                        return new RuntimeException("CRITICAL ERROR: Role 'USER' not found in the database.");
                    });
            logger.info("SUCCESS: Found role 'USER' with ID: {}", userRole.getId());

            logger.info("Step 5: Assigning the found role to the new user.");
            user.setRoles(Set.of(userRole));
            logger.info("SUCCESS: Role assigned.");

            logger.info("Step 6: Attempting to save the new user to the database...");
            userRepository.save(user);
            logger.info("--- SUCCESS: USER SAVED TO DATABASE! ---");

            return ResponseEntity.ok("User registered successfully");

        } catch (Exception e) {
            logger.error("--- REGISTRATION FAILED --- An unexpected exception occurred:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred during registration. Please check the server logs.");
        }
    }
}