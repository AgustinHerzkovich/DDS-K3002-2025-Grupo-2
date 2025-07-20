import domain.AgregadorAplicacion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgregadorAplicacion.class)

// Este codigo deberia ejecutarse con las fuentes dinamicas y estaticas y el agregador ya levantados



public class AutomatizacionTest {


    private final String urlAgregador = "http://localhost:8084";
    private final String urlEstatica = "http://localhost:8081";
    private final String urlDinamica = "http://localhost:8082";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void CargarFuentesColeccionesYHechos() throws InterruptedException {
        System.out.println("Swagger disponible en: " + urlAgregador + "/documentacion-apis");
        System.out.println("H2 Console disponible en: " + urlAgregador + "/h2-console");
        System.out.println("JDBC URL: jdbc:h2:mem:testdb");
        System.out.println("Recordar cambiar el puerto para fuente estatica y dinamica");
        System.out.println("Agregador: " + urlAgregador);
        System.out.println("Estatica" + urlEstatica);
        System.out.println("Dinamica" + urlDinamica);


        levantarFuentesEstaticas();
        levantarFuenteDinamicaYPostearHecho();
        crearColeccionEnElAgregador();

        return;
    }

    private void levantarFuentesEstaticas() {
        List<String> archivos = List.of(
                "desastres_naturales_argentina.csv",
                "desastres_sanitarios_contaminacion_argentina.csv",
                "desastres_tecnologicos_argentina.csv"
        );

        String url = urlEstatica + "/fuentesEstaticas";
        ResponseEntity<String> response = restTemplate.postForEntity(url, archivos, String.class);
        System.out.println("Post fuenteEstática: " + response.getStatusCode());
    }

    private void levantarFuenteDinamicaYPostearHecho() {
        String fuenteDinamicaUrl = urlDinamica + "/fuentesDinamicas";
        ResponseEntity<String> response = restTemplate.postForEntity(fuenteDinamicaUrl, null, String.class);
        System.out.println("Post fuenteDinámica: " + response.getStatusCode());

        // Postear hecho
        String hechoUrl = urlDinamica + "/fuentesDinamicas/1/hechos";
        Map<String, Object> hecho = crearHechoDeEjemplo();
        restTemplate.postForEntity(hechoUrl, hecho, String.class);
        System.out.println("Hecho posteado a fuenteDinamica.");
    }

    private Map<String, Object> crearHechoDeEjemplo() {
        Map<String, Object> hecho = new HashMap<>();
        hecho.put("titulo", "Titulo ejemplo");
        hecho.put("descripcion", "Descripcion ejemplo");
        hecho.put("fechaAcontecimiento", "2025-07-10T19:28:54.832Z");
        hecho.put("origen", "CARGA_MANUAL");
        hecho.put("contenidoTexto", "Contenido texto ejemplo");
        hecho.put("anonimato", false);

        hecho.put("categoria", Map.of("nombre", "Categoria ejemplo"));
        hecho.put("ubicacion", Map.of("latitud", 0, "longitud", 0));

        Map<String, Object> multimedia = new HashMap<>();
        multimedia.put("tipo", "video");
        multimedia.put("formato", "mp4");
        multimedia.put("tamanio", 100);
        multimedia.put("resolucion", "1920x1080");
        multimedia.put("duracion", 30);
        hecho.put("contenidoMultimedia", List.of(multimedia));

        Map<String, Object> contribuyente = Map.of(
                "contribuyenteId", "Id ejemplo",
                "esAdministrador", false
        );

        Map<String, Object> autor = new HashMap<>();
        autor.put("nombre", "Nombre ejemplo");
        autor.put("apellido", "Apellido ejemplo");
        autor.put("fechaNacimiento", "2001-10-10");
        autor.put("contribuyente", contribuyente);
        hecho.put("autor", autor);

        return hecho;
    }

    private void crearColeccionEnElAgregador() {
        String url = urlAgregador + "/agregador/colecciones";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("titulo", "Coleccion 1");
        requestBody.put("descripcion", "Coleccion de colecciones jijo");

        requestBody.put("criteriosDePertenencia", List.of(Map.of(
                "tipo", "fecha",
                "fechaInicial", "2010-01-01T20:44:13.170",
                "fechaFinal", "2020-12-20T20:44:13.170"
        )));

        List<Map<String, Object>> fuentes = List.of(
                Map.of("id", Map.of("idExterno", 1), "tipo", "ESTATICA"),
                Map.of("id", Map.of("idExterno", 1), "tipo", "DINAMICA")
        );

        requestBody.put("fuentes", fuentes);
        requestBody.put("algoritmoConsenso", Map.of("tipo", "absoluto"));

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);
        // TODO: Hacer que esto no me tire un error 500 internal server error.
        //En agregador sale: org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing : domain.colecciones.Coleccion.algoritmoConsenso -> domain.algoritmos.AlgoritmoConsenso
        System.out.println("Colección creada: " + response.getStatusCode());
    }

}
