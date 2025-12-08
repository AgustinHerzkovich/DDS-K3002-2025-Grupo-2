package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
import domain.peticiones.ResponseWrapper;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import domain.peticiones.SolicitudesHttp;
import org.springframework.web.client.ResourceAccessException;

@RestController
@RequestMapping("/apiPublica")
public class ContribuyenteController {
    private final String urlBaseAgregador;
    private final String urlBaseDinamicas;
    private final SolicitudesHttp solicitudesHttp;
    private final Logger logger = LoggerFactory.getLogger(ContribuyenteController.class);

    public ContribuyenteController(@Lazy ConfigService configService) {
        this.urlBaseAgregador = configService.getUrlAgregador();
        this.urlBaseDinamicas = configService.getUrlFuentesDinamicas();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/contribuyentes")
    public ResponseEntity<?> agregarContribuyente(@RequestBody String body) {
        try {
            solicitudesHttp.post(urlBaseDinamicas + "/contribuyentes", body, String.class);
        }catch (ResourceAccessException e){
            logger.warn("No fue posible conectarse con fuente dinamica durante un POST de contribuyente");
        } catch (Exception e){
            logger.error("Hubo un error posteando un contribuyente a fuente dinamica. ", e);
        }

        return ResponseWrapper.wrapResponse(solicitudesHttp.post(urlBaseAgregador + "/contribuyentes", body, String.class)); // Se postea tanto en fuente dinámica como en agregador
    }

    @GetMapping("/contribuyentes/{id}/hechos")
    public ResponseEntity<?> obtenerHechosContribuyente(@PathVariable(name = "id") String id,
                                                        @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(name = "size", defaultValue = "20") Integer size) { // Para que un usuario vea sus hechos
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/contribuyentes/" + id + "/hechos");
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "size", size);
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }

    @GetMapping("/contribuyentes")
    public  ResponseEntity<?> obtenerContribuyentes(@RequestParam(name = "mail", required = false) String mail) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/contribuyentes");
        UrlHelper.appendQueryParam(url, "mail", mail);
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }

    @PatchMapping("/contribuyentes/{id}/identidad")
    public ResponseEntity<?> modificarIdentidadAContribuyente(@PathVariable(name = "id") String id, @RequestBody String body) {
        solicitudesHttp.patch(urlBaseDinamicas + "/contribuyentes/" + id + "/identidad", body, String.class); // Se patchea tanto en fuente dinámica como en agregador
        return ResponseWrapper.wrapResponse(solicitudesHttp.patch(urlBaseAgregador + "/contribuyentes/" + id + "/identidad", body, String.class));
    }
}
