package com.example.demo.db.bhs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.db.bhs", entityManagerFactoryRef = "bhsEntityManager", transactionManagerRef = "bhsTransactionManager")
public class PersistBhsAutoConfiguration {
    @Autowired
    private Environment env;

    public PersistBhsAutoConfiguration() {
        super();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean bhsEntityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(bhsDataSource());
        em.setPackagesToScan("com.example.demo.db.bhs");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.bhs-datasource.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.bhs-datasource.hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @ConfigurationProperties(prefix="spring.bhs-datasource")
    public DataSource bhsDataSource() {
        return DataSourceBuilder.create()
                                .driverClassName(env.getProperty("spring.bhs-datasource.driver-class-name"))
                                .build();
    }


    @Bean
    public PlatformTransactionManager bhsTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(bhsEntityManager().getObject());
        return transactionManager;
    }

    @Bean
    @Qualifier("BhsJdbcTemplate")
    public JdbcTemplate bhsJdbcTemplate() {
        return new JdbcTemplate(bhsDataSource());
    }

}
