package ru.mai.is.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF (если не требуется)
                .csrf(AbstractHttpConfigurer::disable)
                // Настраиваем авторизацию запросов
                .authorizeHttpRequests(auth -> auth
                        // Разрешаем неаутентифицированный доступ ко всем URL
                        .anyRequest().permitAll()
                )
                // Отключаем форму логина
                .formLogin(Customizer.withDefaults())
                // Отключаем базовую авторизацию
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
