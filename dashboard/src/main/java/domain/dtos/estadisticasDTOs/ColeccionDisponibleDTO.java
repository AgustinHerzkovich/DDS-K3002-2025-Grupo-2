package domain.dtos.estadisticasDTOs;


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
    public String toString(){
        return "->  " + idColeccion + "   -   " + tituloColeccion + "\n       - " + (descripcionColeccion.length() < 50 ? descripcionColeccion : descripcionColeccion.substring(0, 47) + "...");
    }
}
