package com.example.demo.db.infoserv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.db.infoserv", entityManagerFactoryRef = "infoServEntityManager", transactionManagerRef = "infoServTransactionManager")
public class PersistInfoServAutoConfiguration {
    @Autowired
    private Environment env;

    public PersistInfoServAutoConfiguration() {
        super();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean infoServEntityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(infoServDataSource());
        em.setPackagesToScan("com.example.demo.db.infoserv");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.infoserv-datasource")
    public DataSource infoServDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager infoServTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(infoServEntityManager().getObject());
        return transactionManager;
    }

}
