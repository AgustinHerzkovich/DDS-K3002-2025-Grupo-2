package domain.apiClient;


import domain.DTOs.ColeccionDTO;
import domain.DTOs.FuenteDinamicaDTO;
import domain.DTOs.HechoDTO;
import domain.connectionManager.Conexion;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
public class ApiClient {

    private static final RestTemplate restTemplate = new RestTemplate();

    public static void postColeccion(ColeccionDTO coleccion, Conexion conexion){
        String url = conexion.getUri() + "/apiAdministrativa/colecciones";
        restTemplate.postForLocation(url, coleccion);
    }

    public static void postHecho(HechoDTO hechoDTO, Conexion conexion){
        String url = conexion.getUri() + "/fuentesDinamicas/hechos";
        restTemplate.postForLocation(url, hechoDTO);
    }

    public static Integer postFuenteDinamica(Conexion conexion){
        String url = conexion.getUri() + "/apiAdministrativa/fuente";
        FuenteDinamicaDTO fuente = restTemplate.postForObject(url, null, FuenteDinamicaDTO.class);
        if(fuente == null) {
            throw new RuntimeException("No se obtuvo la ID de respuesta");
        }
        return fuente.getId();
    }

    public static List<HechoDTO> getHechosIrrestrictos(String id, Conexion conexion){
        String url = conexion.getUri() + "/apiPublica/colecciones/" + id + "/hechosIrrestrictos";
        HechoDTO[] hechosArray = restTemplate.getForObject(url, HechoDTO[].class);
        return Arrays.asList(hechosArray);
    }

    public static List<HechoDTO> getHechosCurados(String id, Conexion conexion){
        String url = conexion.getUri() + "/apiPublica/colecciones/" + id + "/hechosCurados";
        HechoDTO[] hechosArray = restTemplate.getForObject(url, HechoDTO[].class);
        return Arrays.asList(hechosArray);
    }

    public static List<ColeccionDTO> getColecciones(Conexion conexion){
        String url = conexion.getUri() + "/apiPublica/colecciones";
        ColeccionDTO[] coleccionesArray = restTemplate.getForObject(url, ColeccionDTO[].class);
        return Arrays.asList(coleccionesArray);
    }
}