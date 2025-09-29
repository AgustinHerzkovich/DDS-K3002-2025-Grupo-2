package domain.apiClient;

import domain.connectionManager.Conexion;
import domain.dashboardDTOs.colecciones.ColeccionDTO;
import domain.dashboardDTOs.fuentes.FuenteDinamicaDTO;
import domain.dashboardDTOs.hechos.HechoDTO;
import domain.dashboardDTOs.hechos.HechoPostDTO;
import domain.dashboardDTOs.solicitudes.SolicitudDTO;
import domain.dashboardDTOs.usuarios.ContribuyenteDTO;
import domain.dashboardDTOs.usuarios.IdentidadDTO;
import domain.dashboardDTOs.usuarios.IdentidadPatchDTO;
import domain.dtos.estadisticasDTOs.*;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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

    public static ContribuyenteDTO postContribuyente(ContribuyenteDTO contribuyenteDTO, Conexion conexion){
        String url = conexion.getUri() + "/fuentesDinamicas/contribuyentes";
        ContribuyenteDTO rta = restTemplate.postForObject(url, contribuyenteDTO, ContribuyenteDTO.class);
        assert rta != null;
        return rta;
    }
    public static IdentidadDTO postIdentidad(IdentidadPatchDTO identidadPostDTO, Integer id, Conexion conexion){
        String url = conexion.getUri() + "/fuentesDinamicas/contribuyentes/"+id + "/identidades";
        return restTemplate.postForObject(url, identidadPostDTO, IdentidadDTO.class);
    }
    public static void postSolicitud(SolicitudDTO solicitudDTO, Conexion conexion){
        String url = conexion.getUri() + "/apiPublica/solicitudes";
        restTemplate.postForObject(url, solicitudDTO, void.class);
    }


    public static void postActualizarEstadisticas(Conexion conexion) {
        String url = conexion.getUri() + "/estadisticas/actualizar";
        restTemplate.postForObject(url, null, Void.class);
    }

    public static List<ProvinciaConMasHechosDeColeccionDTO> getProvinciasConMasHechosDeColeccion(String idColeccion, int page, int limit, Conexion conexion) {
        String url = String.format("%s/estadisticas/provinciasConMasHechosDeColeccion?idColeccion=%s&page=%d&limit=%d", conexion.getUri(), idColeccion, page, limit);
        ProvinciaConMasHechosDeColeccionDTO[] response = restTemplate.getForObject(url, ProvinciaConMasHechosDeColeccionDTO[].class);
        return Arrays.asList(response);
    }

    public static List<CategoriaConMasHechosDTO> getCategoriasConMasHechos(int page, int limit, Conexion conexion) {
        String url = String.format("%s/estadisticas/categoriasConMasHechos?page=%d&limit=%d", conexion.getUri(), page, limit);
        CategoriaConMasHechosDTO[] response = restTemplate.getForObject(url, CategoriaConMasHechosDTO[].class);
        return Arrays.asList(response);
    }

    public static List<ProvinciaConMasHechosDTO> getProvinciasConMasHechosDeCategoria(String nombreCategoria, int page, int limit, Conexion conexion) {
        String url = String.format("%s/estadisticas/provinciasConMasHechosDeCategoria?nombreCategoria=%s&page=%d&limit=%d", conexion.getUri(), nombreCategoria, page, limit);
        ProvinciaConMasHechosDTO[] response = restTemplate.getForObject(url, ProvinciaConMasHechosDTO[].class);
        return Arrays.asList(response);
    }

    public static List<HoraConMasHechosDeCategoriaDTO> getHoraConMasHechosDeCategoria(String nombreCategoria, int page, int limit, Conexion conexion) {
        String url = String.format("%s/estadisticas/horaConMasHechosDeCategoria?nombreCategoria=%s&page=%d&limit=%d", conexion.getUri(), nombreCategoria, page, limit);
        HoraConMasHechosDeCategoriaDTO[] response = restTemplate.getForObject(url, HoraConMasHechosDeCategoriaDTO[].class);
        return Arrays.asList(response);
    }

    public static CantidadSolicitudesSpamDTO getSolicitudesDeEliminacionSpam(Conexion conexion) {
        String url = conexion.getUri() + "/estadisticas/solicitudesDeEliminacionSpam";
        return restTemplate.getForObject(url, CantidadSolicitudesSpamDTO.class);
    }

    public static List<ColeccionDisponibleDTO> getColeccionesDisponiblesDTO(Conexion conexion, int page, int limit) {
        String url = String.format("%s/estadisticas/coleccionesDisponibles?&page=%d&limit=%d", conexion.getUri(), page, limit);
        return Arrays.stream(restTemplate.getForObject(url, ColeccionDisponibleDTO[].class)).toList();
    }




}