package ru.itis.some.project.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import freemarker.template.Configuration;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.sql.DataSource;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
@AllArgsConstructor
@ComponentScan("ru.itis.some.project")
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {

    private final Environment env;

    @Bean
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(env.getRequiredProperty("db.url"));
        config.setUsername(env.getRequiredProperty("db.username"));
        config.setPassword(env.getRequiredProperty("db.password"));
        config.setDriverClassName(env.getRequiredProperty("db.driver"));

        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(hikariDataSource());
    }

    @Bean
    public Configuration freemarkerConfig() {
        Configuration config = new Configuration(Configuration.VERSION_2_3_27);

        config.setClassForTemplateLoading(getClass(), "/templates/email");
        config.setDefaultEncoding("UTF-8");

        return config;
    }

    @Bean
    public JavaMailSender mailSender() {
        Properties mailProperties = new Properties();

        mailProperties.setProperty("mail.smtp.auth", env.getRequiredProperty("mail.smtp.auth"));
        mailProperties.setProperty("mail.smtp.starttls.enable", env.getRequiredProperty("mail.smtp.starttls.enable"));

        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setJavaMailProperties(mailProperties);
        sender.setHost(env.getRequiredProperty("mail.host"));
        sender.setPort(Integer.parseInt(env.getRequiredProperty("mail.port")));
        sender.setUsername(env.getRequiredProperty("mail.username"));
        sender.setPassword(env.getRequiredProperty("mail.password"));


        return sender;
    }
}
