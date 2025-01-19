package ibrahim.compulynxtest.Auntentication.service.impl;


import ibrahim.compulynxtest.Auntentication.dao.request.SignUpRequest;
import ibrahim.compulynxtest.Auntentication.dao.request.SigninRequest;
import ibrahim.compulynxtest.Auntentication.dao.response.JwtAuthenticationResponse;
import ibrahim.compulynxtest.Auntentication.entities.Role;
import ibrahim.compulynxtest.Auntentication.entities.Roles;
import ibrahim.compulynxtest.Auntentication.entities.User;
import ibrahim.compulynxtest.Auntentication.repository.RoleRepo;
import ibrahim.compulynxtest.Auntentication.repository.UserRepository;
import ibrahim.compulynxtest.Auntentication.service.AuthenticationService;
import ibrahim.compulynxtest.Auntentication.service.JwtService;
import ibrahim.compulynxtest.Utils.ApiResponse;
import ibrahim.compulynxtest.Utils.ResponseBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;


import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public ApiResponse<?> signup(SignUpRequest request) {


        for (String role : request.getRoles()) {
            boolean isValid = Arrays.stream(Role.values())
                    .anyMatch(r -> r.name().equals(role));
            if (isValid) {
                System.out.println(role + " is VALID");
            } else {
                return ResponseBuilder.error(role + " is INVALID", null);
            }
        }


        Set<Roles> userroles = new HashSet<>();
        for (String role : request.getRoles()) {
            Optional<Roles> userrole = roleRepo.findByName(String.valueOf(role));
            userrole.ifPresent(userroles::add);

        }

        try {
            String randomPassword = generateRandomPassword(8);

            String generatedPassword = passwordEncoder.encode(randomPassword);
            var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName()).dateRegistered(new Date())
                    .username(request.getUsername()).phone(request.getPhone()).password(generatedPassword)
                    .roles(userroles)
                    .build();
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                return ResponseBuilder.error("Email is required to create a user.", null);
            }
            if (userRepository.existsByUsername(user.getUsername())) {
                return ResponseBuilder.error("Email already exist!", user.getUsername());
            }
            if (user.getPhone().length() > 12) {
                return ResponseBuilder.error("Phone number is too long. Maximum is 12 digits", user.getPhone());
            }
            if (userRepository.existsByPhone(user.getPhone())) {
                return ResponseBuilder.error("Phone number is already used!", user.getPhone());
            }
            System.out.println("Generated Password " + generatedPassword);
            System.out.println("Saving user...");

            System.out.println(user);
            userRepository.save(user);
            System.out.println("Generated Password " + randomPassword);
            return ResponseBuilder.success("User Registered successfully!", request);

        } catch (Exception e) {
            return ResponseBuilder.error(e.getMessage(), request);
        }

    }

    @Override
    public ApiResponse<?> signin(SigninRequest request) {
        String message = "";

        // Validate if user exists
        boolean userExist = userRepository.existsByUsername(request.getUsername());
        if (!userExist) {
            message = "Username with " + request.getUsername() + " does not exist!";

            return ResponseBuilder.error(message, null);
        }


        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user == null) {
            message = "User with " + request.getUsername() + " does not exist!";
            return ResponseBuilder.error(message, null);
        }

        // Authenticate the user
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            if (!auth.isAuthenticated()) return ResponseBuilder.error("Login Failed", null);

            System.out.println("Login Successful!");
            message = "Login Successful!";
            var jwt = jwtService.generateToken(user);
            List<String> roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            JwtAuthenticationResponse response = JwtAuthenticationResponse.builder()
                    .token(jwt)
                    .username(user.getUsername())
                    .roles(roles)
                    .frstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phone(user.getPhone())
                    .id(user.getId())
                    .build();
            return ResponseBuilder.success(message, response);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getLocalizedMessage());
            return ResponseBuilder.error(e.getLocalizedMessage(), null);
        }
    }


    public String generateRandomPassword(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_+=<>?";
        String allChars = upper + lower + digits + specialChars;

        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Ensure at least one character from each category
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // Fill remaining characters randomly
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        return password.toString();
    }

//    public Mono<Roles> getAdminRole(){
//        return roleRepo.findByName(Role.ADMIN).switchIfEmpty(Mono.error(new RuntimeException("Error : Role Not found!")));
//    }


}
