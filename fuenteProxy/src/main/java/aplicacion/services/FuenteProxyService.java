package aplicacion.services;

import aplicacion.domain.FuenteProxy;
import aplicacion.repositorios.RepositorioDeFuentesProxy;
import aplicacion.services.excepciones.FuenteNoEncontradaException;
import org.springframework.stereotype.Service;
import aplicacion.domain.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;

@Service
public class FuenteProxyService {
    private final RepositorioDeFuentesProxy repositorioDeFuentesProxy;

    public FuenteProxyService(RepositorioDeFuentesProxy repositorioDeFuentesProxy) {
        this.repositorioDeFuentesProxy = repositorioDeFuentesProxy;
    }

    public void pedirHechos() {
        List<FuenteProxy> fuentesProxy = repositorioDeFuentesProxy.findAll();
        for (FuenteProxy fuente : fuentesProxy) {
            fuente.pedirHechos();
        }
    }

    public List<Hecho> importarHechos() {
        List<FuenteProxy> fuentesProxy = repositorioDeFuentesProxy.findAll();
        List<Hecho> listaDeHechosADevolver = new ArrayList<>();

        for (FuenteProxy fuente : fuentesProxy) {
            listaDeHechosADevolver.addAll(fuente.importarHechos());
        }
       return listaDeHechosADevolver;
    }

    public List<Hecho> importarHechosDeFuente(Long id) throws FuenteNoEncontradaException {
        FuenteProxy fuente = repositorioDeFuentesProxy.findById(id).orElseThrow(() -> new FuenteNoEncontradaException("Fuente " + id + "no encontrada"));
        return fuente.importarHechos();
    }

    public void guardarFuente(FuenteProxy fuenteProxy) {
        repositorioDeFuentesProxy.save(fuenteProxy);
    }
}
