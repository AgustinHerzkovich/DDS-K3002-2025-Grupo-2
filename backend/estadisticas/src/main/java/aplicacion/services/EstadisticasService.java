package aplicacion.services;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionColeccion;
import aplicacion.domain.hechosYSolicitudes.solicitudes.EstadoSolicitudSpam;
import aplicacion.dtos.*;
import aplicacion.repositories.olap.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstadisticasService {
    private final DimensionCategoriaRepository dimensionCategoriaRepository;
    private final FactHechoRepository factHechoRepository;
    private final FactColeccionRepository factColeccionRepository;
    private final FactSolicitudRepository factSolicitudRepository;
    private final DimensionColeccionRepository dimensionColeccionRepository;

    public EstadisticasService(FactHechoRepository factHechoRepository, FactColeccionRepository factColeccionRepository, FactSolicitudRepository factSolicitudRepository, DimensionColeccionRepository dimensionColeccionRepository, DimensionCategoriaRepository dimensionCategoriaRepository) {
        this.factSolicitudRepository = factSolicitudRepository;
        this.factHechoRepository = factHechoRepository;
        this.factColeccionRepository = factColeccionRepository;
        this.dimensionColeccionRepository = dimensionColeccionRepository;
        this.dimensionCategoriaRepository = dimensionCategoriaRepository;
    }

    public List<ProvinciaConMasHechosDeColeccionDTO> obtenerProvinciasConMasHechosDeUnaColeccion(String coleccion_id, Integer page, Integer size) {
        return factColeccionRepository.provinciasOrdenadasPorCantidadHechos(coleccion_id, PageRequest.of(page, size))
                .getContent();
    }

    public List<CategoriaConMasHechosDTO> obtenerCategoriaConMasHechos(Integer page, Integer size) {
        return factHechoRepository.categoriaConMasHechos(PageRequest.of(page, size))
                .getContent();
    }

    public List<ProvinciaConMasHechosDeCategoriaDTO> obtenerProvinciaConMasHechosPorCategoria(String nombreCategoria, Integer page, Integer size) {
        return factHechoRepository.provinciaConMasHechosDeCategoria(nombreCategoria, PageRequest.of(page, size))
                .getContent();
    }

    public List<HoraConMasHechosDeCategoriaDTO> obtenerHoraConMasHechosPorCategoria(String nombreCategoria, Integer page, Integer size) {
        return factHechoRepository.obtenerHoraConMasHechosDeCategoria(nombreCategoria, PageRequest.of(page, size))
                .getContent();
    }

    public List<CantidadSolicitudesPorTipo> obtenerCantidadSolicitudSpam() {
        List<String> tiposDeSolicitudes = List.of(
                "EstadoSolicitudAceptada",
                "EstadoSolicitudPendiente",
                "EstadoSolicitudRechazada",
                "EstadoSolicitudSpam",
                "EstadoSolicitudPrescripta");
        List<CantidadSolicitudesPorTipo> solicitudesPorTipos =
                new ArrayList<>(factSolicitudRepository.obtenerCantidadDeSolicitudesPorTipo()
                        .stream()
                        .map(CantidadSolicitudesPorTipo::new)
                        .toList());        for(String tipoDeSolicitud : tiposDeSolicitudes){
            if(!solicitudesPorTipos.stream().map(CantidadSolicitudesPorTipo::getTipoDeSolicitud).toList().contains(tipoDeSolicitud))
                solicitudesPorTipos.add(new CantidadSolicitudesPorTipo(tipoDeSolicitud, 0L));
        }
        return solicitudesPorTipos;
    }

    public List<String> obtenerTodasColeccionesDisponiblesIds() {
        return dimensionColeccionRepository.findAll().stream().map(DimensionColeccion::getIdColeccionAgregador).toList();
    }

    public List<String> obtenerTodasCategoriasDisponiblesIds() {
        return dimensionCategoriaRepository.findAll().stream().map(DimensionCategoria::getNombre).toList();
    }

    public Page<ColeccionDisponibleDTO> obtenerColeccionesDisponibles(int page, int limit, String search) {
        if(search == null)
            return dimensionColeccionRepository.findAll(PageRequest.of(page, limit)).map(dimensionColeccion -> new ColeccionDisponibleDTO(dimensionColeccion.getIdColeccionAgregador(), dimensionColeccion.getTitulo(), dimensionColeccion.getDescripcion()));
        else
            return dimensionColeccionRepository.findSearch(search, PageRequest.of(page, limit)).map(dimensionColeccion -> new ColeccionDisponibleDTO(dimensionColeccion.getIdColeccionAgregador(), dimensionColeccion.getTitulo(), dimensionColeccion.getDescripcion()));
    }

    public Page<CategoriaDisponibleDTO> obtenerCategoriasDisponibles(Integer page, Integer limit, String search) {
        if(search == null)
            return dimensionCategoriaRepository.findAll(PageRequest.of(page, limit)).map(dimensionCategoria -> new CategoriaDisponibleDTO(dimensionCategoria.getNombre()));
        else
            return dimensionCategoriaRepository.findSearch(search, PageRequest.of(page, limit)).map(dimensionCategoria -> new CategoriaDisponibleDTO(dimensionCategoria.getNombre()));
    }
}