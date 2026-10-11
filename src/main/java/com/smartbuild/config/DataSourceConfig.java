package com.smartbuild.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url:}")
    private String rawUrl;

    @Value("${spring.datasource.username:}")
    private String defaultUsername;

    @Value("${spring.datasource.password:}")
    private String defaultPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        // Check Render standard DATABASE_URL environment variable first, then spring.datasource.url
        String envDbUrl = System.getenv("DATABASE_URL");
        String urlToUse = (envDbUrl != null && !envDbUrl.isBlank()) ? envDbUrl : rawUrl;

        HikariConfig config = new HikariConfig();

        if (urlToUse != null && (urlToUse.startsWith("postgresql://") || urlToUse.startsWith("postgres://"))) {
            log.info("Detected PostgreSQL URL format from environment, parsing for JDBC...");
            try {
                URI uri = new URI(urlToUse);
                String host = uri.getHost();
                int port = uri.getPort() > 0 ? uri.getPort() : 5432;
                String path = uri.getPath();
                String userInfo = uri.getUserInfo();

                String username = defaultUsername;
                String password = defaultPassword;

                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    username = parts[0];
                    password = parts[1];
                }

                String jdbcUrl = "jdbc:postgresql://" + host + (port > 0 ? ":" + port : "") + path;
                config.setJdbcUrl(jdbcUrl);
                config.setUsername(username);
                config.setPassword(password);
                config.setDriverClassName("org.postgresql.Driver");

                log.info("Configured PostgreSQL DataSource for host: {}", host);
            } catch (Exception e) {
                log.error("Failed to parse PostgreSQL URI, falling back to raw URL: {}", e.getMessage());
                config.setJdbcUrl(urlToUse.startsWith("jdbc:") ? urlToUse : "jdbc:" + urlToUse);
                config.setUsername(defaultUsername);
                config.setPassword(defaultPassword);
            }
        } else {
            // Default MySQL or standard JDBC URL
            config.setJdbcUrl(urlToUse);
            config.setUsername(defaultUsername);
            config.setPassword(defaultPassword);
            if (urlToUse != null && urlToUse.contains("mysql")) {
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            }
            log.info("Configured standard JDBC DataSource: {}", urlToUse != null && urlToUse.contains("?") ? urlToUse.substring(0, urlToUse.indexOf("?")) : urlToUse);
        }

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(20000);

        return new HikariDataSource(config);
    }
}
