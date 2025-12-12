package aplicacion.controllers;

import aplicacion.config.ConfigService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import domain.helpers.UrlHelper;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping
public class CategoriaController {
    private final ConfigService configService;
    private final SolicitudesHttp solicitudesHttp;
    private final Cache<String, ResponseEntity<?>> cache = Caffeine.newBuilder().maximumSize(100000).expireAfterWrite(1, TimeUnit.MINUTES).build();

    public CategoriaController(@Lazy ConfigService configService) {
        this.configService = configService;
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @GetMapping("/categorias/index")
    public ResponseEntity<?> obtenerRecomendaciones(@RequestParam("search") String texto,
                                                    @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limite) {
        StringBuilder url = new StringBuilder(configService.getUrlAgregador() + "/categorias/index");
        UrlHelper.appendQueryParam(url, "search", texto);
        UrlHelper.appendQueryParam(url, "limit", limite);
        String key = texto + "|" + limite;
        ResponseEntity<?> rta = cache.getIfPresent(key);
        if(rta == null){
            ResponseEntity<?> respuesta = ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
            cache.put(key, respuesta);
            return respuesta;
        }
        return rta;
    }
}
