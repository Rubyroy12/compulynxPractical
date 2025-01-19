package ibrahim.compulynxtest.Auntentication.service.impl;


import ibrahim.compulynxtest.Auntentication.dao.request.SignUpRequest;
import ibrahim.compulynxtest.Auntentication.entities.User;
import ibrahim.compulynxtest.Auntentication.repository.UserRepository;
import ibrahim.compulynxtest.Auntentication.service.UserService;
import ibrahim.compulynxtest.Utils.ApiResponse;
import ibrahim.compulynxtest.Utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public ApiResponse<SignUpRequest> fetchUser(String username){
        if(userRepository.existsByUsername(username)){
            User user = userRepository.findByUsername(username).get();
           SignUpRequest _user= SignUpRequest.builder().phone(user.getPhone()).firstName(user.getFirstName()).lastName(user.getLastName()).username(user.getUsername()).build();
            return ResponseBuilder.success("User Found", _user);

        }else {
            return ResponseBuilder.success("User Not Found", null);

        }


    }
    @Override
    public ApiResponse<List<User>> getUsers(){
        if(!userRepository.findAll().isEmpty()){
            List<User> users = userRepository.findAll();
            return ResponseBuilder.success("User Found", users);
        }else {
            return ResponseBuilder.success("User Not Found", new ArrayList<>());
        }


    }




}
