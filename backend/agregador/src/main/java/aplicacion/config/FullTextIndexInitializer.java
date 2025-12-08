package aplicacion.config;

import aplicacion.controllers.HechoController;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FullTextIndexInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(FullTextIndexInitializer.class);

    public FullTextIndexInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void createFullTextIndexes() {
        try {
            // Hecho
            boolean hechoIndexExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT COUNT(1) FROM INFORMATION_SCHEMA.STATISTICS " +
                            "WHERE table_schema = DATABASE() AND table_name = 'hecho' AND index_name = 'idx_fulltext_hecho'",
                    Boolean.class
            ));

            if (!hechoIndexExists) {
                jdbcTemplate.execute("CREATE FULLTEXT INDEX idx_fulltext_hecho ON hecho (titulo, descripcion, contenido_texto)");
            }

            // Coleccion
            boolean coleccionIndexExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT COUNT(1) FROM INFORMATION_SCHEMA.STATISTICS " +
                            "WHERE table_schema = DATABASE() AND table_name = 'coleccion' AND index_name = 'idx_fulltext_coleccion'",
                    Boolean.class
            ));

            if (!coleccionIndexExists) {
                jdbcTemplate.execute("CREATE FULLTEXT INDEX idx_fulltext_coleccion ON coleccion (titulo, descripcion)");
            }

            logger.info("FullText indexes checked/created successfully!");
        } catch (Exception e) {
            logger.warn("Error creating FullText indexes: {}", e.getMessage());
        }
    }
    @PostConstruct
    public void createFullTextIndexesPeroSoloDelTitulo() {
        try {
            // Hecho
            boolean hechoIndexExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT COUNT(1) FROM INFORMATION_SCHEMA.STATISTICS " +
                            "WHERE table_schema = DATABASE() AND table_name = 'hecho' AND index_name = 'idx_fulltext_hecho_titulo'",
                    Boolean.class
            ));

            if (!hechoIndexExists) {
                jdbcTemplate.execute("CREATE FULLTEXT INDEX idx_fulltext_hecho_titulo ON hecho (titulo)");
            }

            // Coleccion
            boolean coleccionIndexExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT COUNT(1) FROM INFORMATION_SCHEMA.STATISTICS " +
                            "WHERE table_schema = DATABASE() AND table_name = 'coleccion' AND index_name = 'idx_fulltext_coleccion_titulo'",
                    Boolean.class
            ));

            if (!coleccionIndexExists) {
                jdbcTemplate.execute("CREATE FULLTEXT INDEX idx_fulltext_coleccion_titulo ON coleccion (titulo)");
            }

            logger.info("FullText indexes checked/created successfully!");
        } catch (Exception e) {
            logger.warn("Error creating FullText indexes: {}", e.getMessage());
        }
    }
}