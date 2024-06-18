package fr.uha.ensilan.concours.back.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "fr.uha.ensilan.concours.back.domain")
@Transactional
@EnableCaching
public class ApplicationConfig {
}
