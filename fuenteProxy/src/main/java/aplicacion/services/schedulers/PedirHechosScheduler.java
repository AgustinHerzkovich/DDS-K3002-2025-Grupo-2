package aplicacion.services.schedulers;

import aplicacion.services.FuenteProxyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PedirHechosScheduler {
    private final FuenteProxyService fuenteProxyService;

    public PedirHechosScheduler(FuenteProxyService fuenteProxyService) {
        this.fuenteProxyService = fuenteProxyService;
    }

    @Scheduled(initialDelay = 10000, fixedRate = 3600000) // En Fuente Demo pide los hechos y se los guarda, y en Fuente Metamapa no hace nada
    public void pedirHechos() {
        fuenteProxyService.pedirHechos();
    }
}


