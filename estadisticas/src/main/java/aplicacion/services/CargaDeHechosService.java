package aplicacion.services;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionTiempo;
import aplicacion.domain.dimensiones.DimensionUbicacion;
import aplicacion.domain.facts.FactHecho;
import aplicacion.domain.facts.FactHechoDTO;
import aplicacion.domain.hechosYSolicitudes.Hecho;
import aplicacion.domain.id.FactHechoId;
import aplicacion.repositorios.agregador.HechoRepository;
import aplicacion.repositorios.olap.*;
import aplicacion.repositorios.olap.ConfiguracionGlobalRepository;
import aplicacion.utils.ConfiguracionGlobal;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class CargaDeHechosService {
    private final DimensionCategoriaRepository dimensionCategoriaRepository;
    private final DimensionColeccionRepository dimensionColeccionRepository;
    private final DimensionTiempoRepository dimensionTiempoRepository;
    private final DimensionUbicacionRepository dimensionUbicacionRepository;
    private final FactColeccionRepository factColeccionRepository;
    private final FactHechoRepository factHechoRepository;
    private final HechoRepository hechoRepository;
    private final ConfiguracionGlobalRepository configuracionGlobalRepository;

    public CargaDeHechosService(
            DimensionCategoriaRepository dimensionCategoriaRepository,
            DimensionColeccionRepository dimensionColeccionRepository,
            DimensionTiempoRepository dimensionTiempoRepository,
            DimensionUbicacionRepository dimensionUbicacionRepository,
            FactColeccionRepository factColeccionRepository,
            FactHechoRepository factHechoRepository,
            HechoRepository hechoRepository,
            ConfiguracionGlobalRepository configuracionGlobalRepository
    ) {
        this.dimensionCategoriaRepository = dimensionCategoriaRepository;
        this.dimensionColeccionRepository = dimensionColeccionRepository;
        this.dimensionTiempoRepository = dimensionTiempoRepository;
        this.dimensionUbicacionRepository = dimensionUbicacionRepository;
        this.factColeccionRepository = factColeccionRepository;
        this.factHechoRepository = factHechoRepository;
        this.hechoRepository = hechoRepository;
        this.configuracionGlobalRepository = configuracionGlobalRepository;
    }

    public void actualizarHechos() {
        FactHecho factHecho= factHechoRepository.findById(new FactHechoId(1L, 1L, 1L)).orElseThrow();
        System.out.println(factHecho);
        List<Hecho> hechosAImportar;
        ConfiguracionGlobal fechaConfiguracion = configuracionGlobalRepository.findById("ultima_actualizacion_hechos").orElse(null);
        int c = 0;
        do {
            Pageable pageable = PageRequest.of(c, 100);
            if(fechaConfiguracion != null) {
                hechosAImportar = hechoRepository.findByFechaAfter(LocalDateTime.parse(fechaConfiguracion.getValor()), pageable).getContent();
            } else {
                hechosAImportar = hechoRepository.findAll(pageable).getContent();
            }
            System.out.println("Analizando " + hechosAImportar.size() + " hechos. Página: " + c);
            sumarHechos(hechosAImportar);
            System.out.println("Tanda de hechos importada");
            c++;
        } while (hechosAImportar.size() == 100);
    }


    public void sumarHechos(List<Hecho> hechos) {
        Set<FactHechoDTO> factHechoDTOSet = new HashSet<>();
        for (Hecho h : hechos) {
            FactHechoDTO analizado = FactHechoDTO.fromHecho(h);
            if (!factHechoDTOSet.add(analizado)) {
                factHechoDTOSet.stream().filter(factHecho -> factHecho.equals(analizado)).findFirst().ifPresent(factHecho -> {
                    factHecho.sumarOcurrencia();
                });
            }
        }

        System.out.println("Registros de hechos agrupados: " + hechos.size());

        List<DimensionCategoria> dimensionCategoriaList = dimensionCategoriaRepository.findByNombreCategoria(factHechoDTOSet.stream().map(factHecho -> factHecho.getDimensionCategoria().getNombre()).toList());
        List<DimensionTiempo> dimensionTiempoList = dimensionTiempoRepository.findByTiempo(factHechoDTOSet.stream().map(factHecho -> factHecho.getDimensionTiempo().getCodigo()).toList());
        List<DimensionUbicacion> dimensionUbicacionList = dimensionUbicacionRepository.findByUbicaciones(factHechoDTOSet.stream().map(factHecho -> factHecho.getDimensionUbicacion().getCodigo()).toList());

        System.out.println("Registros de dimensiones cargados: " + dimensionCategoriaList.size() + " - "+ dimensionTiempoList.size() + " - " + dimensionUbicacionList.size() + "   (Categorias - Tiempos - Ubicaciones)");
        int q = 0;
        Set<FactHecho> factHechoSet = new HashSet<>();
        for (FactHechoDTO factHechoDTO : factHechoDTOSet) {
            DimensionUbicacion ubi = dimensionUbicacionList.stream().filter(dimUbi -> dimUbi.getCodigo().equals(factHechoDTO.getDimensionUbicacion().getCodigo())).findFirst().orElse(dimensionUbicacionRepository.save(factHechoDTO.getDimensionUbicacion()));
            DimensionTiempo tie = dimensionTiempoList.stream().filter(dimTie -> dimTie.getCodigo().equals(factHechoDTO.getDimensionTiempo().getCodigo())).findFirst().orElse(dimensionTiempoRepository.save(factHechoDTO.getDimensionTiempo()));
            DimensionCategoria cat = dimensionCategoriaList.stream().filter(dimCat -> dimCat.getNombre().equals(factHechoDTO.getDimensionCategoria().getNombre())).findFirst().orElse(dimensionCategoriaRepository.save(factHechoDTO.getDimensionCategoria()));


            Optional<FactHecho> existingFactHecho = factHechoRepository.findById(new FactHechoId(ubi.getId_ubicacion(), tie.getIdTiempo(), cat.getIdCategoria())); //
            if (existingFactHecho.isPresent()) {
                existingFactHecho.get().sumarOcurrencias(factHechoDTO.getCantidadDeHechos());
                factHechoRepository.save(existingFactHecho.get());
            } else {
                factHechoRepository.save(new FactHecho(ubi, tie, cat, factHechoDTO.getCantidadDeHechos()));
                q++;
            }
        }

        System.out.println("Registros de hechos nuevos persistidos: " + q);
    }
}
