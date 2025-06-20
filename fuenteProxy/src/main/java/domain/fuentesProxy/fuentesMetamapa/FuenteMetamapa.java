package domain.fuentesProxy.fuentesMetamapa;

import domain.hechos.Hecho;
import domain.fuentesProxy.FuenteProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

// METAMAPA EXTERNO
public class FuenteMetamapa extends FuenteProxy {
    public FuenteMetamapa(Long id) {
        super(id);
    }

    public List<Hecho> importarHechos() {
        String url = "https://mocki.io/v1/66ea9586-9ada-4bab-a974-58abbe005292";
        RestTemplate restTemplate = new RestTemplate();
        List<Hecho> hechos = List.of(restTemplate.getForObject(url, Hecho[].class));
        return hechos;
    }

    public List<Hecho> obtenerHechosColeccion(String identificador) {
        String url = "https://mocki.io/v1/536a4049-29e5-4cb2-89c7-76c1b068a1a1";
        RestTemplate restTemplate = new RestTemplate();
        List<Hecho> hechos = List.of(restTemplate.getForObject(url, Hecho[].class));
        return hechos;
    }

    public void postearSolicitud(String json) {
        String url ="https://mocki.io/v1/536a4049-29e5-4cb2-89c7-76c1b068a1a1";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();//crea encabezados http y le indica al servidor que el contenido se enviara es de tipo json
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(json, headers);
        ResponseEntity<String> response =restTemplate.postForEntity(url, request, String.class);
        //Muestro la respuesta del serivdor
        System.out.println("codigo de respuesta: " + response.getStatusCode());
        System.out.println("Respuesta: "+ response.getBody());
    }

    public void pedirHechos() {
        return; // Temporal para asegurar el polimorfismo
    }
}
