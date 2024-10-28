package ru.mai.is.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "keystore")
public class KeystoreProperties {
    private String path;
    private String password;
    private String alias;
}
