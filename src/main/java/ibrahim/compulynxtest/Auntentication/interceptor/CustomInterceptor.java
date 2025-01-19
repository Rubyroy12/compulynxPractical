package ibrahim.compulynxtest.Auntentication.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CustomInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {


        return true;
    }

    public Map<String,Object> extractHeaders(HttpServletRequest request){
        return Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(
                headerName ->headerName,request::getHeader
        ));
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // This method is called after the handler method is invoked
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // This method is called after the response has been sent to the client
    }
}
