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
        basePackages = "aplicacion.repositorios.olap",
        entityManagerFactoryRef = "estadisticasEntityManagerFactory",
        transactionManagerRef = "estadisticasTransactionManager"
)
public class EstadisticasDataSourceConfig {

    @Autowired
    private EstadisticasJpaProperties jpaProperties;

    @Bean(name = "estadisticasDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.estadisticas")
    public DataSource estadisticasDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "estadisticasEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean estadisticasEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("estadisticasDataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("aplicacion.domain.facts", "aplicacion.domain.dimensiones", "aplicacion.domain.id", "aplicacion.utils")
                .persistenceUnit("estadisticasPU")
                .properties(jpaProperties.getProperties())
                .build();
    }

    @Bean(name = "estadisticasTransactionManager")
    public PlatformTransactionManager estadisticasTransactionManager(
            @Qualifier("estadisticasEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
