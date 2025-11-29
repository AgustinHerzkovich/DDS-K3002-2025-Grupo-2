package aplicacion.graphql.objects;

import java.time.OffsetDateTime;

public record HechoItem(
        String id,
        String titulo,
        String descripcion,
        Double latitud,
        Double longitud,
        String categoria,
        OffsetDateTime fechaCarga,
        OffsetDateTime fechaAcontecimiento,
        OffsetDateTime fechaUltimaModificacion,
        String contenidoTexto,
        Boolean anonimato
) {
}