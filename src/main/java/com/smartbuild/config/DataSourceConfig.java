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

    private static final String RENDER_DEFAULT_POSTGRES = 
            "postgresql://nirmman_user:95mkCKq8cumzivfZvnSHagDN0COBIvel@dpg-db5pvtl9fdbs73dngrlg-a/nirmman";

    @Value("${spring.datasource.url:}")
    private String rawUrl;

    @Value("${spring.datasource.username:root}")
    private String defaultUsername;

    @Value("${spring.datasource.password:san#mysql@22}")
    private String defaultPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        String envDbUrl = System.getenv("DATABASE_URL");
        if (envDbUrl == null || envDbUrl.isBlank()) {
            envDbUrl = System.getenv("DB_URL");
        }
        if (envDbUrl == null || envDbUrl.isBlank()) {
            envDbUrl = System.getenv("SPRING_DATASOURCE_URL");
        }

        // If running in cloud (Render) and no URL specified, use the user's Render PostgreSQL database
        boolean isCloud = System.getenv("RENDER") != null || System.getenv("PORT") != null;
        if ((envDbUrl == null || envDbUrl.isBlank()) && isCloud) {
            log.info("Running on Render: Defaulting to Render internal PostgreSQL database...");
            envDbUrl = RENDER_DEFAULT_POSTGRES;
        }

        String urlToUse = (envDbUrl != null && !envDbUrl.isBlank()) ? envDbUrl : rawUrl;

        HikariConfig config = new HikariConfig();

        if (urlToUse != null && (urlToUse.startsWith("postgresql://") || urlToUse.startsWith("postgres://"))) {
            log.info("Configuring PostgreSQL DataSource from URI...");
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

                log.info("Configured PostgreSQL connection to host: {}", host);
            } catch (Exception e) {
                log.error("Failed to parse PostgreSQL URI: {}", e.getMessage());
                config.setJdbcUrl(urlToUse.startsWith("jdbc:") ? urlToUse : "jdbc:" + urlToUse);
                config.setUsername(defaultUsername);
                config.setPassword(defaultPassword);
            }
        } else {
            // Local MySQL fallback
            config.setJdbcUrl(urlToUse);
            config.setUsername(defaultUsername);
            config.setPassword(defaultPassword);
            if (urlToUse != null && urlToUse.contains("mysql")) {
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            }
            log.info("Configured MySQL connection to: {}", urlToUse);
        }

        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(15000); // 15 seconds max connection timeout to prevent hanging

        return new HikariDataSource(config);
    }
}
