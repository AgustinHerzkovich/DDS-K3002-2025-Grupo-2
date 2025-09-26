package aplicacion.services;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionTiempo;
import aplicacion.domain.dimensiones.DimensionUbicacion;
import aplicacion.domain.facts.FactHecho;
import aplicacion.domain.hechosYSolicitudes.Hecho;
import aplicacion.domain.id.FactHechoId;
import aplicacion.repositorios.agregador.HechoRepository;
import aplicacion.repositorios.olap.*;
import aplicacion.repositorios.olap.ConfiguracionGlobalRepository;
import aplicacion.utils.ConfiguracionGlobal;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


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
            Pageable pageable = PageRequest.of(c, 1000);
            if(fechaConfiguracion != null) {
                hechosAImportar = hechoRepository.findByFechaAfter(LocalDateTime.parse(fechaConfiguracion.getValor()), pageable).getContent();
            } else {
                hechosAImportar = hechoRepository.findAll(pageable).getContent();
            }
            System.out.println("Analizando " + hechosAImportar.size() + " hechos. Página: " + c);
            sumarHechos(hechosAImportar);
            System.out.println("Tanda de hechos importada");
            c++;
        } while (hechosAImportar.size() == 1000);
    }


    public void sumarHechos(List<Hecho> hechos) {
        Set<FactHecho> factHechoSet = new HashSet<>();
        for (Hecho h : hechos) {
            FactHecho analizado = FactHecho.fromHecho(h);
            if (!factHechoSet.add(analizado))
                factHechoSet.stream().filter(factHecho -> factHecho.equals(analizado)).findFirst().ifPresent(factHecho -> {
                    factHecho.sumarOcurrencia();
                });
        }

        System.out.println("Registros de hechos agrupados: " + hechos.size());

        List<DimensionCategoria> dimensionCategoriaList = dimensionCategoriaRepository.findByNombreCategoria(factHechoSet.stream().map(factHecho -> factHecho.getDimensionCategoria().getNombre()).toList());
        List<DimensionTiempo> dimensionTiempoList = dimensionTiempoRepository.findByTiempo(factHechoSet.stream().map(factHecho -> factHecho.getDimensionTiempo().getCodigo()).toList());
        List<DimensionUbicacion> dimensionUbicacionList = dimensionUbicacionRepository.findByUbicaciones(factHechoSet.stream().map(factHecho -> factHecho.getDimensionUbicacion().getCodigo()).toList());
        // Usar maps hace que sea mas rapido, aunque mucho no se nota, ya que el principal cuello de botella esta con la BD
        Map<String, DimensionCategoria> categoriaMap = dimensionCategoriaList.stream()
                .collect(Collectors.toMap(DimensionCategoria::getNombre, Function.identity()));
        Map<String, DimensionTiempo> tiempoMap = dimensionTiempoList.stream()
                .collect(Collectors.toMap(DimensionTiempo::getCodigo, Function.identity()));
        Map<String, DimensionUbicacion> ubicacionMap = dimensionUbicacionList.stream()
                .collect(Collectors.toMap(DimensionUbicacion::getCodigo, Function.identity()));

        System.out.println("Dimensiones previas encontradas: " + dimensionCategoriaList.size() + " - " + dimensionTiempoList.size() + " - " + dimensionUbicacionList.size() + "         (Categoria - Tiempo - Ubicacion)");

        for (FactHecho factHecho : factHechoSet) {
            String codUbicacion = factHecho.getDimensionUbicacion().getCodigo();
            String codTiempo = factHecho.getDimensionTiempo().getCodigo();
            String nomCategoria = factHecho.getDimensionCategoria().getNombre();

            DimensionUbicacion dimensionUbicacion = ubicacionMap.get(codUbicacion);
            if (dimensionUbicacion == null) {
                dimensionUbicacion = dimensionUbicacionRepository.save(factHecho.getDimensionUbicacion());
                ubicacionMap.put(codUbicacion, dimensionUbicacion);
            }

            DimensionTiempo dimensionTiempo = tiempoMap.get(codTiempo);
            if (dimensionTiempo == null) {
                dimensionTiempo = dimensionTiempoRepository.save(factHecho.getDimensionTiempo());
                tiempoMap.put(codTiempo, dimensionTiempo);
            }

            DimensionCategoria dimensionCategoria = categoriaMap.get(nomCategoria);
            if (dimensionCategoria == null) {
                dimensionCategoria = dimensionCategoriaRepository.save(factHecho.getDimensionCategoria());
                categoriaMap.put(nomCategoria, dimensionCategoria);
            }

            factHecho.setDimensionUbicacion(dimensionUbicacion);
            factHecho.setDimensionTiempo(dimensionTiempo);
            factHecho.setDimensionCategoria(dimensionCategoria);
            factHecho.setId(new FactHechoId(
                    dimensionUbicacion.getId_ubicacion(),
                    dimensionTiempo.getIdTiempo(),
                    dimensionCategoria.getIdCategoria()
            ));
        }

        System.out.println("Guardando " + factHechoSet.size() + " hechos");

        Set<FactHechoId> ids = factHechoSet.stream() // Los que tengo ya tienen ID, separo todos los IDs
                .map(FactHecho::getId)
                .collect(Collectors.toSet());

        List<FactHecho> existentes = factHechoRepository.findAllById(ids);  // Busco todos los que ya existen
        Map<FactHechoId, FactHecho> existentesMap = existentes.stream()
                .collect(Collectors.toMap(FactHecho::getId, Function.identity()));  // Los meto en un Map

        List<FactHecho> hechosParaGuardar = new ArrayList<>();

        for (FactHecho f : factHechoSet) {
            if (existentesMap.containsKey(f.getId())) { // Los que existen...
                FactHecho existente = existentesMap.get(f.getId());
                existente.sumarOcurrencias(f.getCantidadDeHechos());    // Los actualizo
                hechosParaGuardar.add(existente);                       // Y lo agrego para actualizar
            } else {
                hechosParaGuardar.add(f);                               // Si es nuevo lo tengo que guardar despues tambien
            }
        }

        factHechoRepository.saveAll(hechosParaGuardar);                 // Guardo tod0 de una (menos querys)

        System.out.println("Registros de hechos persistidos: " + factHechoSet.size());
    }



}
