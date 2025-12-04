package aplicacion.services;

import aplicacion.domain.dimensiones.DimensionColeccion;
import aplicacion.domain.facts.FactColeccion;
import aplicacion.domain.hechosYSolicitudes.Coleccion;
import aplicacion.domain.hechosYSolicitudes.HechoXColeccion;
import aplicacion.repositories.agregador.ColeccionRepository;
import aplicacion.repositories.agregador.HechoXColeccionRepository;
import aplicacion.repositories.olap.*;
import aplicacion.utils.etl.ColeccionFactProcesor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CargaDeColeccionesService {

    private final HechoXColeccionRepository hechoXColeccionRepository;
    private final ColeccionRepository coleccionRepository;
    private final FactColeccionRepository factColeccionRepository;
    private final ColeccionFactProcesor coleccionFactProcesor;
    private final DimensionColeccionRepository dimensionColeccionRepository;
    public CargaDeColeccionesService(HechoXColeccionRepository hechoXColeccionRepository,
                                     ColeccionRepository coleccionRepository, DimensionColeccionRepository dimensionColeccionRepository,
                                     DimensionCategoriaRepository dimensionCategoriaRepository,
                                     DimensionUbicacionRepository dimensionUbicacionRepository,
                                     FactColeccionRepository factColeccionRepository, DimensionColeccionRepository dimensionColeccionRepository1) {
        this.dimensionColeccionRepository = dimensionColeccionRepository1;

        this.coleccionFactProcesor = new ColeccionFactProcesor(factColeccionRepository, dimensionUbicacionRepository, dimensionCategoriaRepository, dimensionColeccionRepository);
    this.hechoXColeccionRepository = hechoXColeccionRepository;
    this.coleccionRepository = coleccionRepository;
    this.factColeccionRepository = factColeccionRepository;
    }

    public void actualizarColecciones() {
        System.out.println("Actualizando hechos segun las colecciones");
        Integer tamañoPagina = 3000;
        List<HechoXColeccion> hechosPorColeccionAImportar;
        long inicio = System.nanoTime();
        int c = 0;
        int q = 0;
        factColeccionRepository.deleteAll();
        dimensionColeccionRepository.deleteAll();
        List<Coleccion> coleccions = coleccionRepository.findAll();
        do {
            Pageable pageable = PageRequest.of(c, tamañoPagina);
            hechosPorColeccionAImportar = hechoXColeccionRepository.findAll(pageable).getContent();
            System.out.println("Analizando " + hechosPorColeccionAImportar.size() + " hechos. Página: " + c);
            sumarHechosColecciones(hechosPorColeccionAImportar);
            q+=hechosPorColeccionAImportar.size();
            c++;
        } while (hechosPorColeccionAImportar.size() == tamañoPagina);
        long fin = System.nanoTime();
        System.out.printf("Tiempo total: %3d ms - %d hechos - %d paginas - %1.2f hechos/segundo \n" , (fin-inicio)/1_000_000, q, c, q / (double) ( (fin-inicio) / 1_000_000_000 ) );    }

    public void sumarHechosColecciones(List<HechoXColeccion> hechosPorColeccionAImportar){
        List<FactColeccion> factColecciones = new ArrayList<>();
        for(HechoXColeccion hechoXcoleccion : hechosPorColeccionAImportar){
            factColecciones.add(FactColeccion.fromHechoYColeccion(hechoXcoleccion.getHecho(), DimensionColeccion.fromColeccion(hechoXcoleccion.getColeccion())));
        }
        coleccionFactProcesor.procesar(factColecciones);
    }
}
