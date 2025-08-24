package domain.apiClient;


import domain.DTOs.ColeccionDTO;
import domain.connectionManager.Conexion;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;

public class ApiClient {
    public static void postColeccion(ColeccionDTO coleccion, Conexion conexion){
        conexion.getWebClient().post().uri("/apiAdministrativa/colecciones").retrieve().bodyToMono(Void.class).block();
    }
}
