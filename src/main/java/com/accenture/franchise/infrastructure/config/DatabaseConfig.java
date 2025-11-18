package com.accenture.franchise.infrastructure.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.accenture.franchise.infrastructure.persistence.repository")
public class DatabaseConfig extends AbstractR2dbcConfiguration {
    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(DRIVER, "mysql")
                        .option(HOST, System.getenv("DB_HOST"))
                        .option(PORT, Integer.parseInt(System.getenv("DB_PORT")))
                        .option(USER, System.getenv("DB_USER"))
                        .option(PASSWORD, System.getenv("DB_PASSWORD"))
                        .option(DATABASE, System.getenv("DB_NAME"))
                        .build()
        );
    }
}
