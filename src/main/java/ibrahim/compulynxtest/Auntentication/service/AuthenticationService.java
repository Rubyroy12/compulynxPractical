package ibrahim.compulynxtest.Auntentication.service;


import ibrahim.compulynxtest.Auntentication.dao.request.SignUpRequest;
import ibrahim.compulynxtest.Auntentication.dao.request.SigninRequest;
import ibrahim.compulynxtest.Utils.ApiResponse;

public interface AuthenticationService {
    ApiResponse<?> signup(SignUpRequest request);

    ApiResponse<?> signin(SigninRequest request);

}
