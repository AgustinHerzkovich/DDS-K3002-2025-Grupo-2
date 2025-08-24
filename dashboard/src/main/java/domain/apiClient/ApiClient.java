package domain.apiClient;


import domain.DTOs.ColeccionDTO;
import domain.DTOs.FuenteDinamicaDTO;
import domain.DTOs.HechoDTO;
import domain.connectionManager.Conexion;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;

public class ApiClient {
    public static void postColeccion(ColeccionDTO coleccion, Conexion conexion){
        conexion.getWebClient().post().uri("/apiAdministrativa/colecciones").retrieve().bodyToMono(Void.class).block();
    }
    public static void postHecho(HechoDTO hechoDTO, Conexion conexion){
        conexion.getWebClient().post().uri("/fuentesProxy/hechos").retrieve().bodyToMono(Void.class).block();
    }
    public static Integer postFuenteDinamica(Conexion conexion){
        try {
        return conexion.getWebClient().post().uri("/apiAdministrativa/fuente").retrieve().bodyToMono(FuenteDinamicaDTO.class).block().getId();
        }catch (Exception e){
        throw new RuntimeException("No se obtuvo la ID de respuesta");
    }
    }
    public static List<HechoDTO> getHechosIrrestrictos(String id, Conexion conexion){
       return conexion.getWebClient().get().uri("/apiPublica/colecciones/"+id+"/hechosIrrestrictos").retrieve().bodyToMono(new ParameterizedTypeReference<List<HechoDTO>>() {}).block();
    }
    public static List<HechoDTO> getHechosCurados(String id, Conexion conexion){
        return conexion.getWebClient().get().uri("/apiPublica/colecciones/"+id+"/hechosCurados").retrieve().bodyToMono(new ParameterizedTypeReference<List<HechoDTO>>() {}).block();
    }
    public static List<ColeccionDTO> getColecciones(Conexion conexion){
        return conexion.getWebClient().get().uri("/apiPublica/colecciones").retrieve().bodyToMono(new ParameterizedTypeReference<List<ColeccionDTO>>() {}).block();
    }
}
