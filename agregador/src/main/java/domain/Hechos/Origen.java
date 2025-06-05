package domain.Hechos;

// ORIGEN
public enum Origen {
    CARGA_MANUAL, // No sabemos bien qué es, puede ser que lo carga un administrador directamente
    DATASET, // Proveniente de una fuente estática
    CONTRIBUYENTE, // Publicado por un contribuyente mediante una fuente dinámica
    EXTERNO     // Proveniente de una fuente proxy
}