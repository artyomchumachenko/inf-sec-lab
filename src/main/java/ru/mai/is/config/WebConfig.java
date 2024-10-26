package ru.mai.is.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Разрешаем запросы с фронтенда
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Включите OPTIONS для предварительных запросов CORS
                .allowedHeaders("*") // Разрешаем все заголовки, включая Authorization
                .allowCredentials(true); // Если используете cookies или сессионные данные
    }
}
