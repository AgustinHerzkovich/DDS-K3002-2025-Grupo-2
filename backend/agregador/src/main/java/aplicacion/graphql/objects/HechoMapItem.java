package aplicacion.graphql.objects;

import java.time.OffsetDateTime;

public record HechoMapItem(
        String id,
        String titulo,
        Double latitud,
        Double longitud,
        String categoria,
        OffsetDateTime fechaCarga
) {
}