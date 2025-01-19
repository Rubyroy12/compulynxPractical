package ibrahim.compulynxtest.Auntentication.service;


import ibrahim.compulynxtest.Auntentication.dao.request.SignUpRequest;
import ibrahim.compulynxtest.Auntentication.entities.User;
import ibrahim.compulynxtest.Utils.ApiResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();
    ApiResponse<SignUpRequest> fetchUser(String email);
    ApiResponse<List<User>> getUsers();
}
