package fr.uha.ensilan.concours.back.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.api-key")
@Component
@Getter
public class ApiKeyProperties {
    private String header = "x-api-key";
    private String key;

    public void setHeader(String header) {
        this.header = header;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
