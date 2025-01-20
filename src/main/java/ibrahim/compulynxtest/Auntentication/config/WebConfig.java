package ibrahim.compulynxtest.Auntentication.config;

import ibrahim.compulynxtest.Auntentication.interceptor.CustomInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomInterceptor());
    }


//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//                .allowedOrigins("http://localhost:4200") // Allow the frontend's origin
//                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow necessary methods
//                .allowedHeaders("Authorization", "Content-Type") // Allow specific headers
//                .allowCredentials(true); // Allow cookies if needed
//    }
}
