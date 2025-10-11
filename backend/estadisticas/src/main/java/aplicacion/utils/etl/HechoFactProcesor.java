package aplicacion.utils.etl;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionTiempo;
import aplicacion.domain.dimensiones.DimensionUbicacion;
import aplicacion.domain.facts.FactHecho;
import aplicacion.domain.id.FactHechoId;
import aplicacion.repositorios.olap.DimensionCategoriaRepository;
import aplicacion.repositorios.olap.DimensionTiempoRepository;
import aplicacion.repositorios.olap.DimensionUbicacionRepository;
import aplicacion.repositorios.olap.FactHechoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HechoFactProcesor extends AbstractFactProcessor<FactHecho, FactHechoId>{

    FactHechoRepository factHechoRepository;
    DimensionCategoriaRepository dimensionCategoriaRepository;
    DimensionTiempoRepository dimensionTiempoRepository;
    DimensionUbicacionRepository dimensionUbicacionRepository;

    public HechoFactProcesor(FactHechoRepository factHechoRepository, DimensionCategoriaRepository dimensionCategoriaRepository, DimensionTiempoRepository dimensionTiempoRepository, DimensionUbicacionRepository dimensionUbicacionRepository) {
        this.factHechoRepository = factHechoRepository;
        DimensionHandler<DimensionCategoria, String, FactHecho> categoriaHandler = new DimensionHandler<DimensionCategoria, String, FactHecho>(
                DimensionCategoria::getNombre,
                dimensionCategoriaRepository::findByNombreCategoria,
                dimensionCategoriaRepository::saveAll,
                FactHecho::getDimensionCategoria,
                FactHecho::setDimensionCategoria
        );
        DimensionHandler<DimensionTiempo, String, FactHecho> tiempoHandler = new DimensionHandler<DimensionTiempo, String, FactHecho>(
                DimensionTiempo::getCodigo,
                dimensionTiempoRepository::findByTiempo,
                dimensionTiempoRepository::saveAll,
                FactHecho::getDimensionTiempo,
                FactHecho::setDimensionTiempo
        );
        DimensionHandler<DimensionUbicacion, String, FactHecho> ubicacionHandler = new DimensionHandler<DimensionUbicacion, String, FactHecho>(
                DimensionUbicacion::getCodigo,
                dimensionUbicacionRepository::findByUbicaciones,
                dimensionUbicacionRepository::saveAll,
                FactHecho::getDimensionUbicacion,
                FactHecho::setDimensionUbicacion
        );
        this.dimensionHandlers = List.of(categoriaHandler, tiempoHandler, ubicacionHandler);


    }

    @Override
    protected FactHechoId getId(FactHecho h) {
        return h.getId();
    }

    @Override
    protected void asignarId(FactHecho h) {
        h.setId(new FactHechoId(h.getDimensionUbicacion().getId_ubicacion(), h.getDimensionTiempo().getIdTiempo(), h.getDimensionCategoria().getIdCategoria()));
    }

    @Override
    protected Map<FactHechoId, FactHecho> buscarExistentes(Set<FactHechoId> factHechoIds) {
        return factHechoRepository.findAllById(factHechoIds).stream().collect(Collectors.toMap(FactHecho::getId, Function.identity()));
    }

    @Override
    protected void guardarTodos(List<FactHecho> hechos) {
        factHechoRepository.saveAll(hechos);
    }

    @Override
    protected void sumarOcurrencias(FactHecho existente, FactHecho nuevo) {
        existente.sumarOcurrencias(nuevo.getCantidadDeHechos());
    }

}
