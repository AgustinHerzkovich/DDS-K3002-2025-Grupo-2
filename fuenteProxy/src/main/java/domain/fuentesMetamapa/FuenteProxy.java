package domain.fuentesMetamapa;

import domain.hechos.Hecho;
import org.springframework.web.client.RestTemplate;

import java.util.List;

// FUENTE PROXY
public abstract class FuenteProxy extends Fuente {
    //public List<Hecho> importarHechos(){
    // TODO
    //}     REVISAR SI DEBE O NO SACARSE ESTE METODO




    private static final RestTemplate restTemplate = new RestTemplate(); //esto se usa para luego hacer las peticiones HTTP (get/post..)

    protected static <T> T get(String url, Class<T> clase_de_respuesta) {// este metodo hace una peticion get,Usa generics (<T>) para que el tipo de dato que devuelve sea flexible.

        return restTemplate.getForObject(url, clase_de_respuesta);
    }
}
