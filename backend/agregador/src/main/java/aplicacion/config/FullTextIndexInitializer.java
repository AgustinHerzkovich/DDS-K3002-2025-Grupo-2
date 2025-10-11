package aplicacion.config;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FullTextIndexInitializer {

    private final JdbcTemplate jdbcTemplate;

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

            System.out.println("FullText indexes checked/created successfully!");
        } catch (Exception e) {
            System.err.println("Error creating FullText indexes: " + e.getMessage());
        }
    }
}