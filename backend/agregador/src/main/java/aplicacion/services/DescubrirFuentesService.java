package aplicacion.services;

import aplicacion.domain.colecciones.fuentes.FuenteDinamica;
import aplicacion.domain.colecciones.fuentes.FuenteEstatica;
import aplicacion.domain.colecciones.fuentes.FuenteProxy;
import aplicacion.domain.conexiones.Conexion;
import aplicacion.domain.conexiones.ConexionFuenteDinamica;
import aplicacion.domain.conexiones.ConexionFuenteEstatica;
import aplicacion.domain.conexiones.ConexionFuenteProxy;
import aplicacion.dto.input.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DescubrirFuentesService {
    private final ConexionService conexionService;
    private final FuenteService fuenteService;
    public DescubrirFuentesService(ConexionService conexionService, FuenteService fuenteService) {
        this.conexionService = conexionService;
        this.fuenteService = fuenteService;
    }
    @PostConstruct
    public void descubrimientoInicial(){
        descubrirConexionesFuentes();
        cargarFuentes();
    }

    public void descubrirConexionesFuentes(){

        //TODO: Realizar metodo para descubrir automaticamente nuevas conexiones de fuentes, y poder identificar las conexiones univocamente desde su ID
        // Ese id serviria como "id externo". No se si el service discovery te dará un id, pero caso contrario, hay que configurar un endpoint para eso
        // La idea es que cada fuente tiene un id propio. Este id esta persistido, y no cambia aunque cambie su IP o puerto
        // Cuando se ejecuta esta funcion, o bien no se hace nada, o se modifica el ip y puerto de la Conexion con los nuevos datos descubiertos, manteniendo la ID actual

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " - Descubriendo fuentes");
        conexionService.agregarOActualizarConexion(new ConexionFuenteEstatica("conexionFuenteEstaticaUnicaDeEjemploID", "localhost", 8081));
        conexionService.agregarOActualizarConexion(new ConexionFuenteDinamica("conexionFuenteDinamicaUnicaDeEjemploID", "localhost", 8082));
        conexionService.agregarOActualizarConexion(new ConexionFuenteProxy("conexionFuenteProxyUnicaDeEjemploID", "localhost", 8083));
        System.out.println("3 fuentes descubiertas");
    }

    public void cargarFuentes(){

        // Esta funcion lo que hace es cargar las nuevas fuentes y dejarlas vacias en la BD
        // Ademas, las conexiones offline se marcan como offline, para que al importar hechos, solo se importen de las fuentes online
        // Tambien, si una fuente pasa a estar en otra conexion (otro nodo con la fuente de mismo id), se actualiza la fuente para que use la nueva conexion para obtenerla
        System.out.println("\nCargando fuentes nuevas:\n");
        RestTemplate restTemplate = new RestTemplate();
        List<Conexion> conexionList = conexionService.findAllConexiones();
        for(Conexion conexion : conexionList){
            System.out.print("Evaluando conexion: " + conexion.construirURI() + "   -   ");
            try {
                System.out.println("Estado: " +  restTemplate.getForEntity(conexion.construirURI() + "/health", Map.class ).getBody().get("status").toString().toUpperCase());
                conexion.setOnlineUltimaVez(true);
            }catch (Exception e){
                System.out.println("Estado: Desconectado");
                conexion.setOnlineUltimaVez(false);
                System.out.println();
                continue;
            }
            if(conexion instanceof ConexionFuenteEstatica conexionFuenteEstatica) {
                System.out.println("Tipo: Fuente estática");
                System.out.println("FUENTES:");
                try {
                    List<FuenteEstaticaDisponibleInputDto> fuentes = List.of(Objects.requireNonNull(restTemplate.getForEntity(conexionFuenteEstatica.construirURI() + "/fuentesEstaticas", FuenteEstaticaDisponibleInputDto[].class).getBody()));
                    fuentes.forEach(f -> System.out.println("ID: " + f.getId() + "   -   " + f.getArchivos()));
                    fuentes.forEach(f -> fuenteService.guardarOActualizarConexion(new FuenteEstatica(f.getId(), conexion)));
                } catch (Exception e) {
                    System.err.println("Error al descubrir nuevas fuentes en fuente estatica " + conexionFuenteEstatica.construirURI() + ": " + e.getMessage());
                    e.printStackTrace(System.err);
                }
            }
            if(conexion instanceof ConexionFuenteDinamica conexionFuenteDinamica) {
                System.out.println("Tipo: Fuente dinamica");
                try {
                    String id = restTemplate.getForEntity(conexionFuenteDinamica.construirURI() + "/fuenteDinamicaId", String.class).getBody();
                    System.out.println("ID: " + id);
                    fuenteService.guardarOActualizarConexion(new FuenteDinamica(id, conexion));
                } catch (Exception e) {
                    System.err.println("Error al descubrir nuevas fuentes en fuente dinamica " + conexionFuenteDinamica.construirURI() + ": " + e.getMessage());
                }
            }
            if(conexion instanceof ConexionFuenteProxy conexionFuenteProxy) {
                System.out.println("Tipo: Fuente proxy");
                try {
                    List<FuenteProxyDisponibleInputDto> fuentes = List.of(Objects.requireNonNull(restTemplate.getForEntity(conexionFuenteProxy.construirURI() + "/fuentesProxy", FuenteProxyDisponibleInputDto[].class).getBody()));
                    fuentes.forEach(f -> System.out.println("ID: " + f.getId()));
                    fuentes.forEach(f -> fuenteService.guardarOActualizarConexion(new FuenteProxy(f.getId(), conexion)));
                } catch (Exception e) {
                    System.err.println("Error al descubrir nuevas fuentes en fuente proxy " + conexionFuenteProxy.construirURI() + ": " + e.getMessage());
                }
            }
            System.out.println();
        }


    }
}
