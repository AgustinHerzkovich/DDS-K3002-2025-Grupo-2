package domain.controllers;

import domain.fuentesDinamicas.Fuente;
import domain.fuentesDinamicas.FuenteDinamica;
import domain.hechos.Categoria;
import domain.hechos.Hecho;
import domain.hechos.Origen;
import domain.repositorios.RepositorioDeHechos;
import domain.repositorios.RepositorioDeSolicitudes;
import domain.solicitudes.DetectorDeSpamPrueba;
import domain.solicitudes.SolicitudEliminacion;
import domain.usuarios.Contribuyente;
import domain.usuarios.IdentidadContribuyente;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
public class FuenteDinamicaController {
    private final Map<Long, FuenteDinamica> fuentes = new HashMap<>(); // Registro simulado (en memoria)

    public FuenteDinamicaController() {
        RepositorioDeHechos repoHecho = new RepositorioDeHechos();
        RepositorioDeSolicitudes repoSolicitudes = new RepositorioDeSolicitudes();
        FuenteDinamica fuente = new FuenteDinamica(repoHecho, repoSolicitudes, 1L);

        Contribuyente juanceto01 = new Contribuyente("juanceto01", false);
        Hecho hecho = new Hecho("Titulo prueba","Descripcion prueba",new Categoria("soyCategoria"),13.0,14.5,LocalDate.parse("2004-07-08"), Origen.CONTRIBUYENTE,"hola soy un contenido texto :v",null,false, new IdentidadContribuyente("pepe","gonzalez", LocalDate.parse("2004-10-31"), juanceto01));
        fuente.agregarHecho(hecho);  //String nombre, String apellido, LocalDate fecha_nacimiento,  Contribuyente contribuyente
        SolicitudEliminacion solicitud = new SolicitudEliminacion(juanceto01, hecho,"momito fue sin querer sacame el ban porque quiero seguir comentando wasd", new DetectorDeSpamPrueba());
        fuente.agregarSolicitud(solicitud);

        fuentes.put(fuente.getId(), fuente);
    }

    @GetMapping
    public List<Hecho> todaslasfuentes() {
        List <Hecho> hechos = new ArrayList<Hecho>();
        for (Fuente f : fuentes.values()) {
            hechos.addAll(f.importarHechos());
        }
        return hechos;
    }

    @GetMapping("/{id}/hechos") //Se ejecuta al hacer GET en este id
    public List<Hecho> hechos(@PathVariable("id") Long id) {
        FuenteDinamica fuente = fuentes.get(id);
        if (fuente == null){
            throw new NoSuchElementException("No se encontro la fuente con id " + id);// TODO: mas adelante implementar exceptions personalizados
        }
        return fuente.importarHechos();
    }

    @GetMapping("/{id}/solicitudes")
    public List<SolicitudEliminacion> solicitudes(@PathVariable("id") Long id) {
        FuenteDinamica fuente = fuentes.get(id);
        if (fuente == null){
            throw new NoSuchElementException("No se encontro la fuente con id " + id);// TODO: mas adelante implementar exceptions personalizados
        }
        return fuente.buscarSolicitudes();
    }
}
