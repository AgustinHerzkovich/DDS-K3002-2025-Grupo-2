package aplicacion.repositories;

import aplicacion.domain.hechos.Hecho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HechoRepository extends JpaRepository<Hecho, String> {
    Optional<Hecho> findById(String id);

    @Query(
            value = "SELECT * FROM hecho h WHERE MD5(CONCAT_WS('|', " +
                    "titulo, descripcion, categoria_id, latitud, longitud, fecha_acontecimiento, IFNULL(contenido_texto, ''))) IN (:codigos)",
            nativeQuery = true
    )
    List<Hecho> findByCodigoHasheadoIn(@Param("codigos") List<String> codigos);

    @Query(value = "SELECT * FROM hecho " +
            "WHERE MATCH(titulo, descripcion, contenido_texto) " +
            "AGAINST(:textoLibre IN NATURAL LANGUAGE MODE) AND (hecho.visible > 0)",
            countQuery = "SELECT COUNT(*) FROM hecho " + // Corregido countQuery
                    "WHERE MATCH(titulo, descripcion, contenido_texto) " +
                    "AGAINST(:textoLibre IN NATURAL LANGUAGE MODE) AND (hecho.visible > 0)",
            nativeQuery = true)
    Page<Hecho> findByTextoLibre(@Param("textoLibre") String textoLibre, Pageable pageable);

    Page<Hecho> findByAutorIdOrderByVisibleDescFechaCargaDesc(String autorId, Pageable pageable);

    @Query(value = """
    SELECT h.*, c.id as cat_id
    FROM hecho h
    JOIN categoria c ON c.id = h.categoria_id
    WHERE LOWER(h.titulo) = LOWER(:textoLibre)
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga > :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga < :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento > :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento < :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND h.visible > 0
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM hecho h
    JOIN categoria c ON c.id = h.categoria_id
    WHERE LOWER(h.titulo) = LOWER(:textoLibre)
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga > :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga < :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento > :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento < :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND h.visible > 0
    """,
            nativeQuery = true)
    Page<Hecho> buscarPorTituloExacto(
            @Param("textoLibre") String textoLibre,
            @Param("categoria") String categoria,
            @Param("fechaReporteDesde") LocalDateTime fechaReporteDesde,
            @Param("fechaReporteHasta") LocalDateTime fechaReporteHasta,
            @Param("fechaAcontecimientoDesde") LocalDateTime fechaAcontecimientoDesde,
            @Param("fechaAcontecimientoHasta") LocalDateTime fechaAcontecimientoHasta,
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("radio") Double radio,
            Pageable pageable
    );

    @Query("""
        SELECT h FROM Hecho h
        WHERE (:categoria IS NULL OR LOWER(h.categoria.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
          AND (:fechaReporteDesde IS NULL OR h.fechaCarga > :fechaReporteDesde)
          AND (:fechaReporteHasta IS NULL OR h.fechaCarga < :fechaReporteHasta)
          AND (:fechaAcontecimientoDesde IS NULL OR h.fechaAcontecimiento > :fechaAcontecimientoDesde)
          AND (:fechaAcontecimientoHasta IS NULL OR h.fechaAcontecimiento < :fechaAcontecimientoHasta)
          AND (:latitud IS NULL OR h.ubicacion.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
          AND (:longitud IS NULL OR h.ubicacion.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
             AND (h.visible = true )
         ORDER BY h.fechaCarga DESC
    """)
    Page<Hecho> filtrarHechos(
            @Param("categoria") String categoria,
            @Param("fechaReporteDesde") LocalDateTime fechaReporteDesde,
            @Param("fechaReporteHasta") LocalDateTime fechaReporteHasta,
            @Param("fechaAcontecimientoDesde") LocalDateTime fechaAcontecimientoDesde,
            @Param("fechaAcontecimientoHasta") LocalDateTime fechaAcontecimientoHasta,
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("radio") Double radio,
            Pageable pageable
    );

    @Query(value = """
    SELECT h.*, c.id as cat_id
    FROM hecho h
    JOIN categoria c ON c.id = h.categoria_id
    WHERE (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga > :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga < :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento > :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento < :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND (
           :textoLibre IS NULL OR
           MATCH(h.titulo, h.descripcion, h.contenido_texto)
           AGAINST(:textoLibre IN NATURAL LANGUAGE MODE)
      )
      AND (h.visible > 0)
    """,
            countQuery = """
    SELECT COUNT(*) FROM hecho h
    JOIN categoria c ON c.id = h.categoria_id
    WHERE (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga > :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga < :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento > :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento < :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND (
           :textoLibre IS NULL OR
           MATCH(h.titulo, h.descripcion, h.contenido_texto)
           AGAINST(:textoLibre IN NATURAL LANGUAGE MODE)
      )
      AND (h.visible > 0)
    """,
            nativeQuery = true)
    Page<Hecho> filtrarHechosPorTextoLibre(
            @Param("categoria") String categoria,
            @Param("fechaReporteDesde") LocalDateTime fechaReporteDesde,
            @Param("fechaReporteHasta") LocalDateTime fechaReporteHasta,
            @Param("fechaAcontecimientoDesde") LocalDateTime fechaAcontecimientoDesde,
            @Param("fechaAcontecimientoHasta") LocalDateTime fechaAcontecimientoHasta,
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("radio") Double radio,
            @Param("textoLibre") String textoLibre,
            Pageable pageable
    );

    @Query("""
    SELECT h
    FROM Hecho h
    JOIN h.categoria c
    JOIN HechoXColeccion hc ON h.id = hc.hecho.id
    WHERE hc.coleccion.id = :idColeccion
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fechaCarga >= :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fechaCarga <= :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fechaAcontecimiento >= :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fechaAcontecimiento <= :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.ubicacion.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.ubicacion.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
        AND (h.visible = true)
        """)
    Page<Hecho> findByFiltrosYColeccionIrrestrictos(
            @Param("idColeccion") String idColeccion,
            @Param("categoria") String categoria,
            @Param("fechaReporteDesde") LocalDateTime fechaReporteDesde,
            @Param("fechaReporteHasta") LocalDateTime fechaReporteHasta,
            @Param("fechaAcontecimientoDesde") LocalDateTime fechaAcontecimientoDesde,
            @Param("fechaAcontecimientoHasta") LocalDateTime fechaAcontecimientoHasta,
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("radio") Double radio,
            Pageable pageable
    );

    @Query(value = """
    SELECT h.*, c.id as cat_id
    FROM hecho h
    JOIN hecho_coleccion hc ON h.id = hc.hecho_id
    JOIN categoria c ON c.id = h.categoria_id
    WHERE hc.coleccion_id = :idColeccion
      AND LOWER(h.titulo) = LOWER(:textoLibre)
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga >= :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga <= :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento >= :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento <= :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND (h.visible > 0)
    """,
            countQuery = """
    SELECT COUNT(h.id)
    FROM hecho h
    JOIN hecho_coleccion hc ON h.id = hc.hecho_id
    JOIN categoria c ON c.id = h.categoria_id
    WHERE hc.coleccion_id = :idColeccion
      AND LOWER(h.titulo) = LOWER(:textoLibre)
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga >= :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga <= :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento >= :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento <= :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND (h.visible > 0)
    """,
            nativeQuery = true)
    Page<Hecho> findByFiltrosYColeccionIrrestrictos_TituloExacto(
            @Param("idColeccion") String idColeccion,
            @Param("textoLibre") String textoLibre,
            @Param("categoria") String categoria,
            @Param("fechaReporteDesde") LocalDateTime fechaReporteDesde,
            @Param("fechaReporteHasta") LocalDateTime fechaReporteHasta,
            @Param("fechaAcontecimientoDesde") LocalDateTime fechaAcontecimientoDesde,
            @Param("fechaAcontecimientoHasta") LocalDateTime fechaAcontecimientoHasta,
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("radio") Double radio,
            Pageable pageable
    );

    @Query(value = """
    SELECT h.*, c.id as cat_id
    FROM hecho h
    JOIN hecho_coleccion hc ON h.id = hc.hecho_id
    JOIN categoria c ON c.id = h.categoria_id
    WHERE hc.coleccion_id = :idColeccion
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga >= :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga <= :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento >= :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento <= :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND (:textoLibre IS NULL OR MATCH(h.titulo, h.descripcion, h.contenido_texto) AGAINST(:textoLibre IN NATURAL LANGUAGE MODE))
       AND (h.visible > 0)
   \s""",
            countQuery = """
    SELECT count(h.id) FROM hecho h
    JOIN categoria c ON c.id = h.categoria_id
    JOIN hecho_coleccion hc ON h.id = hc.hecho_id
    WHERE hc.coleccion_id = :idColeccion
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga >= :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga <= :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento >= :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento <= :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND (:textoLibre IS NULL OR MATCH(h.titulo, h.descripcion, h.contenido_texto) AGAINST(:textoLibre IN NATURAL LANGUAGE MODE))
        AND (h.visible > 0)
        """,
            nativeQuery = true)
    Page<Hecho> findByFiltrosYColeccionIrrestrictosYTextoLibre(
            @Param("idColeccion") String idColeccion,
            @Param("categoria") String categoria,
            @Param("fechaReporteDesde") LocalDateTime fechaReporteDesde,
            @Param("fechaReporteHasta") LocalDateTime fechaReporteHasta,
            @Param("fechaAcontecimientoDesde") LocalDateTime fechaAcontecimientoDesde,
            @Param("fechaAcontecimientoHasta") LocalDateTime fechaAcontecimientoHasta,
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("radio") Double radio,
            @Param("textoLibre") String textoLibre,
            Pageable pageable
    );

    @Query("""
    SELECT h
    FROM Hecho h
    JOIN h.categoria c
    JOIN HechoXColeccion hc ON h.id = hc.hecho.id
    WHERE hc.coleccion.id = :idColeccion
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fechaCarga >= :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fechaCarga <= :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fechaAcontecimiento >= :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fechaAcontecimiento <= :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.ubicacion.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.ubicacion.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND hc.consensuado = true
         AND (h.visible = true)
    """)
    Page<Hecho> findByFiltrosYColeccionCurados(
            @Param("idColeccion") String idColeccion,
            @Param("categoria") String categoria,
            @Param("fechaReporteDesde") LocalDateTime fechaReporteDesde,
            @Param("fechaReporteHasta") LocalDateTime fechaReporteHasta,
            @Param("fechaAcontecimientoDesde") LocalDateTime fechaAcontecimientoDesde,
            @Param("fechaAcontecimientoHasta") LocalDateTime fechaAcontecimientoHasta,
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("radio") Double radio,
            Pageable pageable
    );

    @Query(value = """
    SELECT h.*, c.id as cat_id
    FROM hecho h
    JOIN hecho_coleccion hc ON h.id = hc.hecho_id
    JOIN categoria c ON c.id = h.categoria_id
    WHERE hc.coleccion_id = :idColeccion
      AND hc.consensuado = true
      AND LOWER(h.titulo) = LOWER(:textoLibre)
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga >= :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga <= :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento >= :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento <= :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND (h.visible > 0)
    """,
            countQuery = """
    SELECT COUNT(h.id)
    FROM hecho h
    JOIN hecho_coleccion hc ON h.id = hc.hecho_id
    JOIN categoria c ON c.id = h.categoria_id
    WHERE hc.coleccion_id = :idColeccion
      AND hc.consensuado = true
      AND LOWER(h.titulo) = LOWER(:textoLibre)
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga >= :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga <= :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento >= :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento <= :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND (h.visible > 0)
    """,
            nativeQuery = true)
    Page<Hecho> findByFiltrosYColeccionCurados_TituloExacto(
            @Param("idColeccion") String idColeccion,
            @Param("textoLibre") String textoLibre,
            @Param("categoria") String categoria,
            @Param("fechaReporteDesde") LocalDateTime fechaReporteDesde,
            @Param("fechaReporteHasta") LocalDateTime fechaReporteHasta,
            @Param("fechaAcontecimientoDesde") LocalDateTime fechaAcontecimientoDesde,
            @Param("fechaAcontecimientoHasta") LocalDateTime fechaAcontecimientoHasta,
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("radio") Double radio,
            Pageable pageable
    );

    @Query(value = """
    SELECT h.*, c.id as cat_id
    FROM hecho h
    JOIN categoria c ON c.id = h.categoria_id
    JOIN hecho_coleccion hc ON h.id = hc.hecho_id
    WHERE hc.coleccion_id = :idColeccion
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga >= :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga <= :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento >= :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento <= :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND (:textoLibre IS NULL OR MATCH(h.titulo, h.descripcion, h.contenido_texto) AGAINST(:textoLibre IN NATURAL LANGUAGE MODE))
      AND hc.consensuado = true
        AND (h.visible > 0)
    """,
            countQuery = """
    SELECT count(h.id) FROM hecho h
    JOIN categoria c ON c.id = h.categoria_id
    JOIN hecho_coleccion hc ON h.id = hc.hecho_id
    WHERE hc.coleccion_id = :idColeccion
      AND (:categoria IS NULL OR LOWER(c.nombre) LIKE CONCAT('%', LOWER(:categoria), '%'))
      AND (:fechaReporteDesde IS NULL OR h.fecha_carga >= :fechaReporteDesde)
      AND (:fechaReporteHasta IS NULL OR h.fecha_carga <= :fechaReporteHasta)
      AND (:fechaAcontecimientoDesde IS NULL OR h.fecha_acontecimiento >= :fechaAcontecimientoDesde)
      AND (:fechaAcontecimientoHasta IS NULL OR h.fecha_acontecimiento <= :fechaAcontecimientoHasta)
      AND (:latitud IS NULL OR h.latitud BETWEEN (:latitud - :radio) AND (:latitud + :radio))
      AND (:longitud IS NULL OR h.longitud BETWEEN (:longitud - :radio) AND (:longitud + :radio))
      AND (:textoLibre IS NULL OR MATCH(h.titulo, h.descripcion, h.contenido_texto) AGAINST(:textoLibre IN NATURAL LANGUAGE MODE))
      AND hc.consensuado = true
        AND (h.visible > 0)
    """,
            nativeQuery = true)
    Page<Hecho> findByFiltrosYColeccionCuradosYTextoLibre(
            @Param("idColeccion") String idColeccion,
            @Param("categoria") String categoria,
            @Param("fechaReporteDesde") LocalDateTime fechaReporteDesde,
            @Param("fechaReporteHasta") LocalDateTime fechaReporteHasta,
            @Param("fechaAcontecimientoDesde") LocalDateTime fechaAcontecimientoDesde,
            @Param("fechaAcontecimientoHasta") LocalDateTime fechaAcontecimientoHasta,
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("radio") Double radio,
            @Param("textoLibre") String textoLibre,
            Pageable pageable
    );

    @Query(value = """
        SELECT h.titulo
        FROM hecho h
        WHERE (:textoLibre IS NULL
               OR MATCH(h.titulo) AGAINST(CONCAT(:textoLibre, '*') IN BOOLEAN MODE))
        ORDER BY MATCH(h.titulo) AGAINST(CONCAT(:textoLibre, '*') IN BOOLEAN MODE) DESC
        LIMIT :limit
        """,
            nativeQuery = true)
    List<String> findAutocompletado(
            @Param("textoLibre") String textoLibre,
            @Param("limit") int limit
    );

    @Query(value = """
        SELECT
            h.titulo
        FROM hecho h
        WHERE h.titulo LIKE CONCAT(:textoLibre, '%')
        ORDER BY h.titulo
        LIMIT :limit
        """, nativeQuery = true)
    List<String> findAutocompletadoLike(
            @Param("textoLibre") String textoLibre,
            @Param("limit") int limit
    );
}