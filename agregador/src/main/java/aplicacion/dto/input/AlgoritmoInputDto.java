package aplicacion.dto.input;

import aplicacion.domain.algoritmos.AlgoritmoConsensoAbsoluto;
import aplicacion.domain.algoritmos.AlgoritmoConsensoIrrestricto;
import aplicacion.domain.algoritmos.AlgoritmoConsensoMayoriaSimple;
import aplicacion.domain.algoritmos.AlgoritmoConsensoMultiplesMenciones;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlgoritmoConsensoIrrestricto.class, name = "irrestricto"),
        @JsonSubTypes.Type(value = AlgoritmoConsensoAbsoluto.class, name = "absoluto"),
        @JsonSubTypes.Type(value = AlgoritmoConsensoMayoriaSimple.class, name = "mayoriaSimple"),
        @JsonSubTypes.Type(value = AlgoritmoConsensoMultiplesMenciones.class, name = "multiplesMenciones")
})
public class AlgoritmoInputDto {
    private final String nombre;

    public AlgoritmoInputDto(String nombre) {
        this.nombre = nombre;
    }
}
