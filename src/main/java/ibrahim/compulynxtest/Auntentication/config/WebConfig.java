package ibrahim.compulynxtest.Auntentication.config;

import ibrahim.compulynxtest.Auntentication.interceptor.CustomInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomInterceptor());
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**") // URL path
//                .addResourceLocations("file:/home/ibrahim/Pictures/uploads/"); // Absolute path-localhost
                .addResourceLocations("file:/root/drma/uploads/"); // Absolute path -server
    }
}
