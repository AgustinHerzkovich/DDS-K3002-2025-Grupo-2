package domain.apiClient;

import domain.dashboardDTOs.*;
import domain.connectionManager.Conexion;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ApiClient {

    private static final RestTemplate restTemplate;
    static {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
        restTemplate = new RestTemplate(factory);
    }
    public static void postColeccion(ColeccionDTO coleccion, Conexion conexion){
        String url = conexion.getUri() + "/apiAdministrativa/colecciones";
        restTemplate.postForLocation(url, coleccion);
    }

    public static void postHecho(HechoPostDTO hechoPostDTO, Conexion conexion){
        String url = conexion.getUri() + "/fuentesDinamicas/hechos";
        restTemplate.postForLocation(url, hechoPostDTO);
    }

    public static Integer postFuenteDinamica(Conexion conexion){
        String url = conexion.getUri() + "/fuenteDiamica/fuente";
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
        Map<String, Integer> rta = restTemplate.postForObject(url, contribuyenteDTO, Map.class);
        assert rta != null;
        return rta.get("contribuyenteId");
    }
    public static void patchIdentidad(IdentidadPatchDTO identidadPostDTO, Integer id, Conexion conexion){
        String url = conexion.getUri() + "/fuentesDinamicas/contribuyentes/"+id;
        restTemplate.patchForObject(url, identidadPostDTO, void.class);
    }
    public static void postSolicitud(SolicitudDTO solicitudDTO, Conexion conexion){
        String url = conexion.getUri() + "/apiPublica/solicitudes";
        restTemplate.postForObject(url, solicitudDTO, void.class);
    }

}