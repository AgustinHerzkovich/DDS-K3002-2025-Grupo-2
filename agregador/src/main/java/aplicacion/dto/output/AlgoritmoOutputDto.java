package aplicacion.dto.output;

import aplicacion.domain.algoritmos.AlgoritmoConsensoAbsoluto;
import aplicacion.domain.algoritmos.AlgoritmoConsensoIrrestricto;
import aplicacion.domain.algoritmos.AlgoritmoConsensoMayoriaSimple;
import aplicacion.domain.algoritmos.AlgoritmoConsensoMultiplesMenciones;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
public class AlgoritmoOutputDto {
    private String nombre;

    public String getNombre() {
        return nombre;
    }
}
