package domain.apiClient;


import domain.DTOs.*;
import domain.connectionManager.Conexion;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
public class ApiClient {

    private static final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    public static void postColeccion(ColeccionDTO coleccion, Conexion conexion){
        String url = conexion.getUri() + "/apiAdministrativa/colecciones";
        restTemplate.postForLocation(url, coleccion);
    }

    public static void postHecho(HechoPostDTO hechoPostDTO, Conexion conexion){
        String url = conexion.getUri() + "/fuentesDinamicas/hechos";
        restTemplate.postForLocation(url, hechoPostDTO);
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

    public static Integer postContribuyente(PostContribuyenteDTO contribuyenteDTO, Conexion conexion){
        String url = conexion.getUri() + "/fuentesDinamicas/contribuyentes";
        return restTemplate.postForObject(url, contribuyenteDTO, Integer.class);
    }
    public static void patchIdentidad(IdentidadPatchDTO identidadPostDTO, Integer id, Conexion conexion){
        String url = conexion.getUri() + "/fuentesDinamicas/contribuyentes/"+id;
        restTemplate.patchForObject(url, identidadPostDTO, void.class);
    }

}