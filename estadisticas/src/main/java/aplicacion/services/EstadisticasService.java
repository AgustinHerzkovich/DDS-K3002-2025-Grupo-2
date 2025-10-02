package aplicacion.services;

import aplicacion.domain.dimensiones.DimensionColeccion;
import aplicacion.dtos.*;
import aplicacion.repositorios.olap.DimensionColeccionRepository;
import aplicacion.repositorios.olap.FactColeccionRepository;
import aplicacion.repositorios.olap.FactHechoRepository;
import aplicacion.repositorios.olap.FactSolicitudRepository;
import aplicacion.utils.Provincia;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {
    private FactHechoRepository factHechoRepository;
    private FactColeccionRepository factColeccionRepository;
    private FactSolicitudRepository factSolicitudRepository;
    private DimensionColeccionRepository dimensionColeccionRepository;

    public EstadisticasService(FactHechoRepository factHechoRepository, FactColeccionRepository factColeccionRepository, FactSolicitudRepository factSolicitudRepository, DimensionColeccionRepository dimensionColeccionRepository) {
        this.factSolicitudRepository = factSolicitudRepository;
        this.factHechoRepository = factHechoRepository;
        this.factColeccionRepository = factColeccionRepository;
        this.dimensionColeccionRepository = dimensionColeccionRepository;
    }

    public List<ProvinciaConMasHechosDeColeccionDTO> obtenerProvinciasConMasHechosDeUnaColeccion(String coleccion_id, Integer page, Integer size) {
        return factColeccionRepository.provinciasOrdenadasPorCantidadHechos(coleccion_id, PageRequest.of(page, size))
                .getContent();
    }

    public List<CategoriaConMasHechosDTO> obtenerCategoriaConMasHechos(Integer page, Integer size) {
        return factHechoRepository.categoriaConMasHechos(PageRequest.of(page, size))
                .getContent();
    }

    public List<ProvinciaConMasHechosDTO> obtenerProvinciaConMasHechosPorCategoria(String nombreCategoria, Integer page, Integer size) {
        return factHechoRepository.provinciaConMasHechosDeCategoria(nombreCategoria, PageRequest.of(page, size))
                .getContent();
    }

    public List<HoraConMasHechosDeCategoriaDTO> obtenerHoraConMasHechosPorCategoria(String nombreCategoria, Integer page, Integer size) {
        return factHechoRepository.obtenerHoraConMasHechosDeCategoria(nombreCategoria, PageRequest.of(page, size))
                .getContent();
    }

    public CantidadSolicitudesSpamDTO obtenerCantidadSolicitudSpam() {
        Long solicitudes_spam = factSolicitudRepository.obtenerCantidadSolicitudesSpam();
        Long solicitudes_totales = factSolicitudRepository.obtenerCantidadSolicitudesTotal();
        if (solicitudes_spam == null) solicitudes_spam = 0L;
        if (solicitudes_totales == null) solicitudes_totales = 0L;
        return new CantidadSolicitudesSpamDTO(solicitudes_spam, solicitudes_totales);
    }

    public List<ColeccionDisponibleDTO> obtenerColeccionesDisponibles(int page, int limit) {
        return dimensionColeccionRepository.findAll(PageRequest.of(page, limit)).getContent().stream().map(dimensionColeccion -> new ColeccionDisponibleDTO(dimensionColeccion.getIdColeccionAgregador(), dimensionColeccion.getTitulo(), dimensionColeccion.getDescripcion())).toList();
    }
    public List<String> obtenerTodasColeccionesDisponiblesIds() {
        return dimensionColeccionRepository.findAll().stream().map(DimensionColeccion::getIdColeccionAgregador).toList();
    }
}