package aplicacion.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"categoria",  "pais", "nombreProvincia", "cantidadHechos" })
public class ProvinciaConMasHechosDeCategoriaDTO {
    @JsonIgnore
    private Long provinciaId;
    private String nombreProvincia;
    private String pais;
    private Long cantidadHechos;
    private String categoria;

}
