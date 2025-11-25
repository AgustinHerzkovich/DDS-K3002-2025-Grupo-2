package aplicacion.utils.etl;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionColeccion;
import aplicacion.domain.dimensiones.DimensionUbicacion;
import aplicacion.domain.facts.FactColeccion;
import aplicacion.domain.id.FactColeccionId;
import aplicacion.repositories.olap.DimensionCategoriaRepository;
import aplicacion.repositories.olap.DimensionColeccionRepository;
import aplicacion.repositories.olap.DimensionUbicacionRepository;
import aplicacion.repositories.olap.FactColeccionRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ColeccionFactProcesor extends AbstractFactProcessor<FactColeccion, FactColeccionId> {
    private FactColeccionRepository factColeccionRepository;

    public ColeccionFactProcesor(FactColeccionRepository factColeccionRepository, DimensionUbicacionRepository dimensionUbicacionRepository, DimensionCategoriaRepository dimensionCategoriaRepository, DimensionColeccionRepository dimensionColeccionRepository){
        this.factColeccionRepository = factColeccionRepository;
        DimensionHandler<DimensionUbicacion, String, FactColeccion> ubicacionHandler = new DimensionHandler<DimensionUbicacion, String, FactColeccion>(
                DimensionUbicacion::getCodigo,
                dimensionUbicacionRepository::findByUbicaciones,
                dimensionUbicacionRepository::saveAll,
                FactColeccion::getDimensionUbicacion,
                FactColeccion::setDimensionUbicacion
        );
        DimensionHandler<DimensionCategoria, String, FactColeccion> categoriaHandler = new DimensionHandler<DimensionCategoria, String, FactColeccion>(
                DimensionCategoria::getNombre,
                dimensionCategoriaRepository::findByNombreCategoria,
                dimensionCategoriaRepository::saveAll,
                FactColeccion::getDimensionCategoria,
                FactColeccion::setDimensionCategoria
        );
        DimensionHandler<DimensionColeccion, String, FactColeccion> coleccionHandler = new DimensionHandler<DimensionColeccion, String, FactColeccion>(
                DimensionColeccion::getIdColeccionAgregador,
                dimensionColeccionRepository::findByIdColeccionAgregadorIn,
                dimensionColeccionRepository::saveAll,
                FactColeccion::getDimensionColeccion,
                FactColeccion::setDimensionColeccion
        );
        this.dimensionHandlers = List.of(coleccionHandler, ubicacionHandler, categoriaHandler);
    }
    @Override
    protected Map<FactColeccionId, FactColeccion> buscarExistentes(Set<FactColeccionId> set) {
        return factColeccionRepository.findAllById(set).stream().collect(Collectors.toMap(FactColeccion::getId, Function.identity()));
    }

    @Override
    protected void guardarTodos(List<FactColeccion> hechos) {
        factColeccionRepository.saveAll(hechos);
    }


    @Override
    protected FactColeccionId getId(FactColeccion h) {
        return h.getId();
    }

    @Override
    protected void asignarId(FactColeccion h) {
        h.setId(new FactColeccionId(h.getDimensionColeccion().getIdColeccion(), h.getDimensionUbicacion().getId_ubicacion(), h.getDimensionCategoria().getIdCategoria()));
    }

    @Override
    protected void sumarOcurrencias(FactColeccion existente, FactColeccion nuevo) {
        existente.sumarOcurrencias(nuevo.getCantidadHechos());
    }
}
