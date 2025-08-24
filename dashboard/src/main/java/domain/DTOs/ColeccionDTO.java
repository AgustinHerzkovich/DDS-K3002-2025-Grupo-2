package domain.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter

public class ColeccionDTO {
    public ColeccionDTO(){
        fuentes = new ArrayList<>();
        criteriosDePertenencia = new ArrayList<>();
    }
    private String titulo;
    private String descripcion;
    private List<CriterioDePertenenciaDTO> criteriosDePertenencia;
    private List<FuenteDTO> fuentes;
    private AlgoritmoConsensoDTO algoritmoConsenso;

}
