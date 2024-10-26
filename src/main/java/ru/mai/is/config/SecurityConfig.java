package ru.mai.is.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.mai.is.config.jwt.JwtAccessDeniedHandler;
import ru.mai.is.config.jwt.JwtAuthenticationEntryPoint;
import ru.mai.is.config.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors() // Включаем CORS
                .and()
                .csrf().disable() // Отключаем CSRF
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Устанавливаем политику сессии
                .and()
                .authorizeHttpRequests()
                .antMatchers("/user/login", "/user/registration").permitAll() // Разрешаем доступ без аутентификации
                .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Обработка 401 ошибок
                .accessDeniedHandler(jwtAccessDeniedHandler) // Обработка 403 ошибок
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
