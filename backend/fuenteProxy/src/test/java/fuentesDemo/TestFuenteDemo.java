package fuentesDemo;

import aplicacion.domain.fuentesProxy.fuentesDemo.Conexion;
import aplicacion.domain.fuentesProxy.fuentesDemo.ConexionPrueba;
import aplicacion.domain.fuentesProxy.fuentesDemo.FuenteDemo;
import aplicacion.domain.fuentesProxy.fuentesMetamapa.FuenteMetamapa;
import aplicacion.domain.hechos.Hecho;
import aplicacion.repositories.FuenteProxyRepository;
import aplicacion.services.FuenteProxyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
class ServiceTest{
    @Autowired
    private FuenteProxyService fuenteProxyService;


    @Test
    void testPedirHechosFuenteDemo() {
        FuenteDemo fuenteDemo = new FuenteDemo( new ConexionPrueba(),"http://mocki.io/v1/66ea9586-9ada-4bab-a974-58abbe005292");
        fuenteProxyService.pedirHechosFuente(fuenteDemo);
        for (Hecho hecho : fuenteDemo.getHechos()) {
            assert hecho != null;
            assert hecho.getTitulo().equals("Primer hecho") ;
        }
    }
}
/*{
        "titulo": "Coleccion 1",
        "descripcion": "Coleccion de colecciones jijo",
        "criteriosDePertenencia": [
        {
        "tipo": "fecha",
        "fechaInicial": "2010-01-01T20:44:13.170",
        "fechaFinal": "2020-12-20T20:44:13.170"
        }
        ],
        "fuentes": [
        {
        "id": {
        "tipo": "ESTATICA",
        "idExterno": 1
        }
        },
        {
        "id": {
        "tipo": "DINAMICA",
        "idExterno": 1
        }
        }
        ],
        "algoritmoConsenso": {
        "tipo": "absoluto"
        }
}
*/
