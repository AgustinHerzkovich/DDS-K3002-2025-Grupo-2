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
import aplicacion.utils.etl.HechoFactProcesor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class CargaDeHechosService {

    private final HechoRepository hechoRepository;
    private final ConfiguracionGlobalRepository configuracionGlobalRepository;
    private final HechoFactProcesor hechoFactProcesor;
    public CargaDeHechosService(
            DimensionCategoriaRepository dimensionCategoriaRepository,
            DimensionTiempoRepository dimensionTiempoRepository,
            DimensionUbicacionRepository dimensionUbicacionRepository,
            FactHechoRepository factHechoRepository,
            HechoRepository hechoRepository,
            ConfiguracionGlobalRepository configuracionGlobalRepository
    ) {
        this.hechoRepository = hechoRepository;
        this.configuracionGlobalRepository = configuracionGlobalRepository;
        hechoFactProcesor = new HechoFactProcesor(factHechoRepository, dimensionCategoriaRepository, dimensionTiempoRepository, dimensionUbicacionRepository);
    }


    public void actualizarHechos() {
        System.out.println("Actualizando hechos");
        Integer tamañoPagina = 3000;
        List<Hecho> hechosAImportar;
        ConfiguracionGlobal fechaConfiguracion = configuracionGlobalRepository.findById("ultima_actualizacion_hechos").orElse(null);
        long inicio = System.nanoTime();
        int c = 0;
        int q = 0;
        do {
            Pageable pageable = PageRequest.of(c, tamañoPagina);
            if(fechaConfiguracion != null) {
                hechosAImportar = hechoRepository.findByFechaAfter(LocalDateTime.parse(fechaConfiguracion.getValor()), pageable).getContent();
            } else {
                hechosAImportar = hechoRepository.findAll(pageable).getContent();
            }
            System.out.println("Analizando " + hechosAImportar.size() + " hechos. Página: " + c);
            sumarHechos(hechosAImportar);
            q+=hechosAImportar.size();
            c++;
        } while (hechosAImportar.size() == tamañoPagina);
        if(fechaConfiguracion != null)
            fechaConfiguracion.setValor(LocalDateTime.now().toString());
        else
            fechaConfiguracion = new ConfiguracionGlobal("ultima_actualizacion_hechos", LocalDateTime.now().toString());
        configuracionGlobalRepository.save(fechaConfiguracion);
        long fin = System.nanoTime();
        System.out.printf("Tiempo total: %3d ms - %d hechos - %d paginas - %1.2f hechos/segundo \n" , (fin-inicio)/1_000_000, q, c, q / (double) ( (fin-inicio) / 1_000_000_000 ) );
    /* Pruebas con 35000 hechos
    * - Páginas de 10:      167,46 hechos/segundo
    * - Páginas de 50:      333,33 hechos/segundo
    * - Páginas de 100:     397,73 hechos/segundo
    * - Páginas de 1000:    583,33 hechos/segundo
    * - Páginas de 3000:    625,00 hechos/segundo
    * - Páginas de 5000:    636,36 hechos/segundo
    * - Páginas de 15000:   648,15 hechos/segundo
    * - Páginas de 35000:   648,15 hechos/segundo
    *
    * Productividad marginal decreciente?
    * Paginas mas grandes siempre significa menos requests a la BD, por lo que es mas rapido, pero requiere de más RAN
    * Lo dejo en 3000 que parece bastante optimo
    * */
    }


    public void sumarHechos(List<Hecho> hechos) {
        hechoFactProcesor.procesar(hechos.stream().map(FactHecho::fromHecho).toList());
    }



}
