package aplicacion.graphql.objects;

import java.time.OffsetDateTime;

public record HechoFiltros(OffsetDateTime fechaReporteDesde,
                           OffsetDateTime fechaReporteHasta,
                           OffsetDateTime fechaAcontecimientoDesde,
                           OffsetDateTime fechaAcontecimientoHasta,
                           Double latitud,
                           Double longitud,
                           String categoria,
                           String search) {
}