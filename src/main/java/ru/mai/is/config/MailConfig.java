package ru.mai.is.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import ru.mai.is.config.properties.MailProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
@RequiredArgsConstructor
public class MailConfig {

    private final MailProperties mailProperties;

    @Value("${mail.debug}")
    private String debug;

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailProperties.host());
        mailSender.setPort(mailProperties.port());
        mailSender.setUsername(mailProperties.username());
        mailSender.setPassword(mailProperties.password());

        Properties properties = mailSender.getJavaMailProperties();

        properties.setProperty("mail.transport.protocol", mailProperties.protocol());
        properties.setProperty("mail.debug", debug);
        properties.setProperty("mail.properties.smtp.connectiontimeout", mailProperties.connectionTimeout());
        properties.setProperty("mail.properties.smtp.timeout", mailProperties.timeout());
        properties.setProperty("mail.properties.smtp.writetimeout", mailProperties.writeTimeout());

        return mailSender;
    }
}
