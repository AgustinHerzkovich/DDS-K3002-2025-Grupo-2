package aplicacion.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionDisponibleDTO {
    public String idColeccion;
    public String tituloColeccion;
    public String descripcionColeccion;
}
