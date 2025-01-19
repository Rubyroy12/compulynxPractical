package ibrahim.compulynxtest.Auntentication.controller;

import ibrahim.compulynxtest.Auntentication.dao.request.SigninRequest;
import ibrahim.compulynxtest.Auntentication.service.AuthenticationService;
import ibrahim.compulynxtest.Auntentication.service.JwtService;
import ibrahim.compulynxtest.Utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;


@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;


    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<?>> signin(@RequestBody SigninRequest request) {
        System.out.println("login request : Username :: "+ request.getUsername());
        ApiResponse<?> response = authenticationService.signin(request);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.info("Authorization Header is not null!");
            String jwtToken = authorizationHeader.substring(7);
            jwtService.invalidateToken(jwtToken);
            log.info("Token invalidated ! ");
            return ResponseEntity.ok("Logout successful");
        } else {
            // If the token is not present or in an unexpected format
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }
    }
}
