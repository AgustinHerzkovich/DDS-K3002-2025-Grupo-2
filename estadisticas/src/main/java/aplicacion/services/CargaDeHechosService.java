package aplicacion.services;

import aplicacion.domain.facts.FactHecho;
import aplicacion.domain.hechosYSolicitudes.Hecho;
import aplicacion.repositorios.agregador.HechoRepository;
import aplicacion.repositorios.olap.*;
import aplicacion.repositorios.olap.ConfiguracionGlobalRepository;
import aplicacion.utils.ConfiguracionGlobal;
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
        List<Hecho> hechosAImportar;
        ConfiguracionGlobal fechaConfiguracion = configuracionGlobalRepository.findById("ultima_actualizacion_hechos").orElse(null);
        int c = 0;
        do {
            Pageable pageable = PageRequest.of(c, 100);
            if(fechaConfiguracion != null) {
                hechosAImportar = hechoRepository.findByFechaAfter(LocalDateTime.parse(fechaConfiguracion.getValor()), pageable );
            } else {
                hechosAImportar = hechoRepository.findAll();
            }
            System.out.println("Analizando " + hechosAImportar.size() + " hechos. Página: " + c);
            sumarHechos(hechosAImportar);
            System.out.println("Tanda de hechos importada");
            c++;
        } while (hechosAImportar.size() == 100);
    }


    public void sumarHechos(List<Hecho> hechos) {
        Set<FactHecho> factHechoSet = new HashSet<>();
        for (Hecho h : hechos) {
            FactHecho analizado = FactHecho.fromHecho(h);
            if (factHechoSet.add(analizado)) {
                factHechoSet.stream().filter(factHecho -> factHecho.equals(analizado)).findFirst().ifPresent(factHecho -> {
                    factHecho.sumarOcurrencia();
                });
            } else {
                factHechoSet.add(analizado);
            }
        }

        System.out.println("Registros de hechos agrupados: " + hechos.size());

        for (FactHecho factHecho : factHechoSet) {
            FactHecho existingFactHecho = factHechoRepository.findById(factHecho.getId()).orElse(null); //
            if (existingFactHecho != null) {
                existingFactHecho.sumarOcurrencias(factHecho.getCantidadDeHechos());
                factHechoRepository.save(existingFactHecho);
            } else {
                factHechoRepository.save(factHecho);
            }
        }

        System.out.println("Registros de hechos persistidos: " + factHechoSet.size());
    }
}
