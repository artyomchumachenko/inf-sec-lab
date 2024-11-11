package ru.mai.is.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.mail")
public record MailProperties(
        String host,
        String username,
        String password,
        int port,
        String protocol,
        String connectionTimeout,
        String timeout,
        String writeTimeout
) {}

