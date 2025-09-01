package domain.dashboardDTOs.colecciones;

import domain.dashboardDTOs.colecciones.criterios.CriterioDePertenenciaDTO;
import domain.dashboardDTOs.fuentes.FuenteDTO;
import lombok.Getter;
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
    @Override
    public String toString() {
        return "\nTítulo: " + titulo +
                "\nDescripción: " + descripcion +
                "\nCriterios: " + criteriosDePertenencia +
                "\nFuentes: " + fuentes +
                "\nAlgoritmo: " + (algoritmoConsenso != null ? algoritmoConsenso.getTipo() : "null");
    }
}
