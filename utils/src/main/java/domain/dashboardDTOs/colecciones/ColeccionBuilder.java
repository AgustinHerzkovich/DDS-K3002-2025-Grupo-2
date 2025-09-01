package domain.dashboardDTOs.colecciones;

import domain.dashboardDTOs.colecciones.criterios.CriterioDeFechaDTO;
import domain.dashboardDTOs.fuentes.FuenteDTO;
import domain.dashboardDTOs.fuentes.FuenteIdDTO;

import java.time.LocalDateTime;

public class ColeccionBuilder {
    ColeccionDTO coleccionDTO = new ColeccionDTO();


    public ColeccionDTO buildColeccion(){
        coleccionDTO.getCriteriosDePertenencia().add(
                new CriterioDeFechaDTO(
                        "fecha",
                        LocalDateTime.parse("2010-01-01T20:44:13.170"),
                        LocalDateTime.parse("2020-12-20T20:44:13.170")
                ));
        return coleccionDTO;
    }

    public ColeccionBuilder setEstatica(Integer ID){
        coleccionDTO.getFuentes().add(new FuenteDTO(new FuenteIdDTO("ESTATICA", ID)));
        return this;
    }

    public ColeccionBuilder setDinamica(Integer ID){
        coleccionDTO.getFuentes().add(new FuenteDTO(new FuenteIdDTO("DINAMICA", ID)));
        return this;
    }
    public ColeccionBuilder setAlgoritmo(String algoritmo){
        coleccionDTO.setAlgoritmoConsenso(new AlgoritmoConsensoDTO(algoritmo));
        return this;
    }

    public ColeccionBuilder setTitulo(String titulo){
        coleccionDTO.setTitulo(titulo);
        return this;
    }
    public ColeccionBuilder setDescripcion(String desc){
        coleccionDTO.setDescripcion(desc);
        return this;
    }

}
