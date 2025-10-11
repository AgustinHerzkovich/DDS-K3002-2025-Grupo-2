package aplicacion.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "aplicacion.repositorios.agregador",
        entityManagerFactoryRef = "agregadorEntityManagerFactory",
        transactionManagerRef = "agregadorTransactionManager"
)
public class AgregadorDataSourceConfig {
    @Autowired
    private AgregadorJpaProperties jpaProperties;

    @Bean(name = "agregadorDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.agregador")
    public DataSource agregadorDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "agregadorEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean agregadorEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("agregadorDataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("aplicacion.domain.hechosYSolicitudes")
                .persistenceUnit("agregadorPU")
                .properties(jpaProperties.getProperties())
                .build();
    }

    @Bean(name = "agregadorTransactionManager")
    public PlatformTransactionManager agregadorTransactionManager(
            @Qualifier("agregadorEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
