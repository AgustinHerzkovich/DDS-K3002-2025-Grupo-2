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

    @Scheduled(cron = "0 0 * * * *") // En Fuente Demo pide los hechos y se los guarda, y en Fuente Metamapa no hace nada
    public void pedirHechos() {
        fuenteProxyService.pedirHechos();
        // delegar logica a fuenteProxyService
        /*
        //String url = "https://mocki.io/v1/66ea9586-9ada-4bab-a974-58abbe005292";
        //RestTemplate restTemplate = new RestTemplate();
        List<Hecho> hechos = List.of(restTemplate.getForObject(endpointHechos, Hecho[].class));
        return hechos;*/
    }
}


