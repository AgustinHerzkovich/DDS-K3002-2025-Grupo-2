package domain.controllers;

import domain.fuentesDinamicas.FuenteDinamica;
import domain.hechos.Categoria;
import domain.hechos.Hecho;
import domain.hechos.Origen;
import domain.repositorios.RepositorioDeHechos;
import domain.repositorios.RepositorioDeSolicitudes;
import domain.services.FuenteService;
import domain.solicitudes.DetectorDeSpamPrueba;
import domain.solicitudes.SolicitudEliminacion;
import domain.usuarios.Contribuyente;
import domain.usuarios.IdentidadContribuyente;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


// TODO: Implementar posteo de solicitudes de eliminacion
// HechoController
// SolicitudController
// HechoService
// SolicitudService
// Persistencia de Hechos y Solicitudes
@RestController // Le decimos que esta clase es un controlador REST
// Esto significa que es lo que *expone la api* para que sea consumido
@RequestMapping("/fuentesDinamicas") // Define ruta base para todos los endpoints de esta clase
public class FuenteController {
    private final FuenteService fuenteService;

    public FuenteController(FuenteService fuenteService) {
        this.fuenteService = fuenteService;
        RepositorioDeHechos repoHecho = new RepositorioDeHechos();
        RepositorioDeSolicitudes repoSolicitudes = new RepositorioDeSolicitudes();
        FuenteDinamica fuente = new FuenteDinamica(repoHecho, repoSolicitudes);

        Contribuyente juanceto01 = new Contribuyente("juanceto01", false);
        Hecho hecho = new Hecho("Titulo prueba","Descripcion prueba",new Categoria("soyCategoria"),13.0,14.5, LocalDateTime.parse("2004-07-08"), Origen.CONTRIBUYENTE,"hola soy un contenido texto :v",null,false, new IdentidadContribuyente("pepe","gonzalez", LocalDate.parse("2004-10-31"), juanceto01));
        fuente.agregarHecho(hecho);  //String nombre, String apellido, LocalDateTime fecha_nacimiento,  Contribuyente contribuyente
        SolicitudEliminacion solicitud = new SolicitudEliminacion(juanceto01, hecho,"momito fue sin querer sacame el ban porque quiero seguir comentando wasd", new DetectorDeSpamPrueba());
        fuente.agregarSolicitud(solicitud);

        fuentes.put(fuente.getId(), fuente);
    }

    @PostMapping
    public void crearFuente(@RequestBody FuenteDinamica fuente) {
        fuenteService.guardarFuente(fuente);
    }

    @DeleteMapping
    public void eliminarFuente(@RequestBody FuenteDinamica fuente) {
        fuenteService.eliminarFuente(fuente);
    }
}
