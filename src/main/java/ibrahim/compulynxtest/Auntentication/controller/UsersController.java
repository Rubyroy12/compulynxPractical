package ibrahim.compulynxtest.Auntentication.controller;

import ibrahim.compulynxtest.Auntentication.dao.request.SignUpRequest;
import ibrahim.compulynxtest.Auntentication.service.AuthenticationService;
import ibrahim.compulynxtest.Auntentication.service.impl.UserServiceImpl;
import ibrahim.compulynxtest.Utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final AuthenticationService authenticationService;

    @Autowired
    private UserServiceImpl userService;
    @GetMapping("hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Here is your resource");
    }
    @GetMapping("users")
    public ResponseEntity<ApiResponse<?>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody SignUpRequest request) {
        ApiResponse<?> response = authenticationService.signup(request);
        return ResponseEntity.ok().body(response);
    }


}
