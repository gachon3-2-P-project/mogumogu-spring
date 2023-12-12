package com.mogumogu.spring.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(List.of("*")); //
//        config.addAllowedHeader("*");
//        config.setAllowedMethods(List.of("GET","POST","PATCH","DELETE"));
//
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }

    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .maxAge(3000)
                .allowedHeaders("*")
                .exposedHeaders(HttpHeaders.LOCATION);
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoggerInterceptor())
//                .excludePathPatterns("/css/**", "/images/**", "/js/**");

//        registry.addInterceptor(new LoginCheckInterceptor())
//                //.addPathPatterns("/*")
//                .excludePathPatterns("/admin/log*");
    }

}
