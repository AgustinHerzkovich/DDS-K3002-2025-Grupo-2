package aplicacion.domain.fuentesMetamapa;

import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import aplicacion.domain.FuenteProxy;

@Entity
@NoArgsConstructor
public class FuenteMetamapa extends FuenteProxy {

    private String endpointHechos;

    public String getEndpointHechos() {
        return endpointHechos;
    }

    public void setEndpointHechos(String endpointHechos) {
        this.endpointHechos = endpointHechos;
    }

    public FuenteMetamapa(String endpointHechos) {
        this.endpointHechos = endpointHechos;
    }
    //private String endpointColecciones = "https://7239874289/agregador/coleccion/1";
    //private String endpointSolicitudes = "https://7239874289/agregador/solicitud";

    public List<Hecho> importarHechos() {
        RestTemplate restTemplate = new RestTemplate();
        //String url = "https://mocki.io/v1/66ea9586-9ada-4bab-a974-58abbe005292";
        List<Hecho> hechos = List.of(restTemplate.getForObject(endpointHechos, Hecho[].class));
        return hechos;
    }

   /* public List<Hecho> obtenerHechosColeccion(Long identificador) {
        //String url = "https://mocki.io/v1/536a4049-29e5-4cb2-89c7-76c1b068a1a1";
        // RestTemplate restTemplate = new RestTemplate();
        List<Hecho> hechos = List.of(restTemplate.getForObject(endpointColecciones, Hecho[].class));
        return hechos;
    }

    public void postearSolicitud(String json) {
        //String url ="https://mocki.io/v1/536a4049-29e5-4cb2-89c7-76c1b068a1a1";
        //  RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();//crea encabezados http y le indica al servidor que el contenido se enviara es de tipo json
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(json, headers);
        ResponseEntity<String> response =restTemplate.postForEntity(endpointSolicitudes, request, String.class);
        //Muestro la respuesta del serivdor
        System.out.println("codigo de respuesta: " + response.getStatusCode());
        System.out.println("Respuesta: "+ response.getBody());
    }
*/
    public void pedirHechos() {
        // No hace nada en Fuente Metamapa, ya que solo está para asegurar el polimorfismo en FuenteProxyService
    }
}
