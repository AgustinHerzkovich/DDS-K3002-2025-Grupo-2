package aplicacion.services;

import aplicacion.dtos.CantidadSolicitudesSpamDTO;
import aplicacion.dtos.CategoriaConMasHechosDTO;
import aplicacion.dtos.HoraConMasHechosDeCategoriaDTO;
import aplicacion.dtos.ProvinciaConMasHechosDTO;
import aplicacion.dtos.ProvinciaConMasHechosDeColeccionDTO;
import aplicacion.repositorios.olap.FactColeccionRepository;
import aplicacion.repositorios.olap.FactHechoRepository;
import aplicacion.repositorios.olap.FactSolicitudRepository;
import aplicacion.utils.Provincia;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasService {
    private FactHechoRepository factHechoRepository;
    private FactColeccionRepository factColeccionRepository;
    private FactSolicitudRepository factSolicitudRepository;

    public EstadisticasService(FactHechoRepository factHechoRepository, FactColeccionRepository factColeccionRepository, FactSolicitudRepository factSolicitudRepository) {
        this.factSolicitudRepository = factSolicitudRepository;
        this.factHechoRepository = factHechoRepository;
        this.factColeccionRepository = factColeccionRepository;
    }

    public ProvinciaConMasHechosDeColeccionDTO obtenerProvinciasConMasHechosDeUnaColeccion(String coleccion_id) {
        return factColeccionRepository.provinciasOrdenadasPorCantidadHechos(coleccion_id, PageRequest.of(0, 1))
                .getContent().stream()
                .findFirst().orElse(null);
    }

    public CategoriaConMasHechosDTO obtenerCategoriaConMasHechos() {
        return factHechoRepository.categoriaConMasHechos(PageRequest.of(0, 1)).getContent().stream().findFirst().orElse(null);
    }

    public ProvinciaConMasHechosDTO obtenerProvinciaConMasHechosPorCategoria(String nombreCategoria) {
        return factHechoRepository.provinciaConMasHechosDeCategoria(nombreCategoria, PageRequest.of(0, 1))
                .getContent().stream()
                .findFirst().orElse(null);
    }

    public HoraConMasHechosDeCategoriaDTO obtenerHoraConMasHechosPorCategoria(String nombreCategoria) {
        return factHechoRepository.obtenerHoraConMasHechosDeCategoria(nombreCategoria, PageRequest.of(0, 1))
                .getContent().stream()
                .findFirst().orElse(null);
    }

    public CantidadSolicitudesSpamDTO obtenerCantidadSolicitudSpam() {
        Long solicitudes_spam = factSolicitudRepository.obtenerCantidadSolicitudesSpam();
        Long solicitudes_totales = factSolicitudRepository.obtenerCantidadSolicitudesTotal();
        return new CantidadSolicitudesSpamDTO(solicitudes_spam, solicitudes_totales);
    }
}