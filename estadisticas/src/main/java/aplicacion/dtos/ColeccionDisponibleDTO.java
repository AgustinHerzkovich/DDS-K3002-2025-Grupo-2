package aplicacion.dtos;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "idColeccion", "tituloColeccion", "descripcionColeccion" })
public class ColeccionDisponibleDTO {
    public String idColeccion;
    public String tituloColeccion;
    public String descripcionColeccion;
}
