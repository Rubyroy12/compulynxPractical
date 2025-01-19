package ibrahim.compulynxtest.Utils;

public class ResponseBuilder {



    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>("SUCCESS",message,data);
    }
    public static <T> ApiResponse<T> error(String message, T data){
        return new ApiResponse<>("ERROR",message,data);
    }
}
