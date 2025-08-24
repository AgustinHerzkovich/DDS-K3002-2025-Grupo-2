package domain.apiClient;


import domain.DTOs.ColeccionDTO;
import domain.DTOs.FuenteDinamicaDTO;
import domain.DTOs.HechoDTO;
import domain.connectionManager.Conexion;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;

public class ApiClient {
    public static void postColeccion(ColeccionDTO coleccion, Conexion conexion){
        conexion.getWebClient().post().uri("/apiAdministrativa/colecciones").retrieve().bodyToMono(Void.class).block();
    }
    public static void postHecho(HechoDTO hechoDTO, Conexion conexion){
        conexion.getWebClient().post().uri("/fuentesProxy/hechos").retrieve().bodyToMono(Void.class).block();
    }
    public static Integer postFuenteDinamica(Conexion conexion){
        try {
        return conexion.getWebClient().post().uri("/fuente").retrieve().bodyToMono(FuenteDinamicaDTO.class).block().getId();
        }catch (Exception e){
        throw new RuntimeException("No se obtuvo la ID de respuesta");
    }
    }
}
