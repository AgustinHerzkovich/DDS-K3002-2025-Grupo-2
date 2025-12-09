package aplicacion.domain.fuentesProxy.fuentesMetamapa;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import aplicacion.domain.fuentesProxy.FuenteProxy;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class FuenteMetamapa extends FuenteProxy {

    private String agregadorId;




    public FuenteMetamapa(String agregadorId) {
        this.agregadorId = agregadorId;
    }
    //private String endpointColecciones = "https://7239874289/agregador/coleccion/1";
    //private String endpointSolicitudes = "https://7239874289/agregador/solicitud";



   /* @Override
    public List<Hecho> importarHechos(DiscoveryClient discoveryClient) {


        RestTemplate restTemplate = new RestTemplate();
        //String url = "https://mocki.io/v1/66ea9586-9ada-4bab-a974-58abbe005292";
        try {
            String endpointHechos = endpointHechos(discoveryClient);
            System.out.println("\n\n" + endpointHechos + "\n\n");
            List<Hecho> hechos = List.of(restTemplate.getForObject(endpointHechos, Hecho[].class));
            System.out.println(hechos.isEmpty());
            return hechos;
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Error al obtener hechos desde el agregador con ID: " + agregadorID);
            return List.of();
        }
    }*/

    /*public String endpointHechos(DiscoveryClient discoveryClient) {
        String baseURL = discoveryClient.getInstances("AGREGADOR")
                .stream()
                .filter(instance -> instance.getMetadata().get("agregadorID").equals(agregadorID))
                .findFirst()
                .map(instance -> instance.getUri().toString())
                .orElseThrow(() -> new RuntimeException(" No se encontraron instancias para el servicio: " + agregadorID));
        return baseURL + "/agregador/hechosSinPaginar";
    }*/

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
*//*
    public void pedirHechos() {
        // No hace nada en Fuente Metamapa, ya que solo está para asegurar el polimorfismo en FuenteProxyService
    }
*/

}
