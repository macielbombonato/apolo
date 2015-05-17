package apolo.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = { "apolo" },
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager")
@EnableTransactionManagement
public class PersistenceConfig {

    private static final Logger log = LoggerFactory.getLogger(PersistenceConfig.class);

    @Bean
    public DataSource configureDataSource() {
        log.info("*************** Env Variables configureDataSource *************** ");

        String driver = System.getenv("APOLO_DATASOURCE_DRIVER_CLASS");
        String url = System.getenv("APOLO_DATASOURCE_URL");
        String username = System.getenv("APOLO_DATASOURCE_USERNAME");
        String password = System.getenv("APOLO_DATASOURCE_PASSWORD");

        log.info("dataSource.driverClassName:    " + driver);
        log.info("dataSource.url:                " + url);
        log.info("dataSource.username:           " + username);
        log.info("dataSource.password:           ***");

        log.info("*************** END Env Variables configureDataSource *************** ");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        log.info("*************** Env Variables entityManagerFactory *************** ");

        String dialect = System.getenv("APOLO_HIBERNATE_DIALECT");
        String hbm2ddlAuto = System.getenv("APOLO_HIBERNATE_HBM2DDL");
        boolean showAndFormatSQL = "true".equals(System.getenv("APOLO_HIBERNATE_SHOW_AND_FORMAT_SQL")) ? true : false;

        log.info("hibernate.dialect:             " + dialect);
        log.info("hibernate.hbm2ddl.auto:        " + hbm2ddlAuto);
        log.info("dataSource.password:           ***");
        log.info("hibernate.show.and.format.sql: " + showAndFormatSQL);

        log.info("*************** END Env Variables entityManagerFactory *************** ");

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(configureDataSource());
        entityManagerFactoryBean.setPackagesToScan("apolo");
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put(org.hibernate.cfg.Environment.DIALECT, dialect);
        jpaProperties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        jpaProperties.put(org.hibernate.cfg.Environment.ENABLE_LAZY_LOAD_NO_TRANS, true);
        jpaProperties.put(org.hibernate.cfg.Environment.SHOW_SQL, showAndFormatSQL);
        jpaProperties.put(org.hibernate.cfg.Environment.FORMAT_SQL, showAndFormatSQL);

        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory());
        return jpaTransactionManager;
    }
}
