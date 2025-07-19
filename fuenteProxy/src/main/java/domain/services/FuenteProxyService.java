package domain.services;

import domain.fuentesProxy.FuenteProxy;
import domain.repositorios.RepositorioDeFuentesProxy;
import org.springframework.stereotype.Service;
import domain.hechos.Hecho;

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

    public void guardarFuente(FuenteProxy fuenteProxy) {
        repositorioDeFuentesProxy.save(fuenteProxy);
    }
}
