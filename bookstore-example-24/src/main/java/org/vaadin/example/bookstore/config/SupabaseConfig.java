package org.vaadin.example.bookstore.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
    basePackages = "org.vaadin.example.bookstore.config.supabase",
    entityManagerFactoryRef = "supabaseEntityManagerFactory",
    transactionManagerRef = "supabaseTransactionManager"
)
public class SupabaseConfig {

    @Bean(name = "supabaseDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.supabase")
    public DataSource supabaseDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "supabaseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean supabaseEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("supabaseDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.supabase") // package chứa entity Supabase
                .persistenceUnit("supabase")
                .build();
    }

    @Bean(name = "supabaseTransactionManager")
    public PlatformTransactionManager supabaseTransactionManager(
            @Qualifier("supabaseEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

