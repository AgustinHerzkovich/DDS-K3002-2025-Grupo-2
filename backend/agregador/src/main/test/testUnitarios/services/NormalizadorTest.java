package testUnitarios.services;

import aplicacion.domain.colecciones.fuentes.FuenteEstatica;
import aplicacion.domain.hechos.Etiqueta;
import aplicacion.excepciones.EtiquetaNoEncontradaException;
import aplicacion.repositorios.RepositorioDeCategorias;
import aplicacion.repositorios.RepositorioDeEtiquetas;
import aplicacion.services.CategoriaService;
import aplicacion.services.EtiquetaService;
import aplicacion.services.normalizador.NormalizadorDeHechos;
import aplicacion.services.normalizador.NormalizadorDeTerminos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testUtils.CategoriaFactory;
import testUtils.HechoFactory;
import testUtils.RandomThingsGenerator;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NormalizadorTest {


    @Mock
    private EtiquetaService etiquetaService;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private NormalizadorDeHechos normalizadorDeHechos;

    @Test
    @DisplayName("Debe normalizar terminos correctamente")
    public void testNormalizadorDeTerminos(){
        NormalizadorDeTerminos norm = new NormalizadorDeTerminos(1);
        List<String> terminos = new ArrayList<>();
        terminos.add("Manzana");
        terminos.add("Banana");
        terminos.add("Pera");
        terminos.add("Anana");
        terminos.add("Damasco");
        terminos.add("Uva");
        terminos.add("Uba");
        terminos.add("uva");
        terminos.add("Anona");
        terminos.add("Manzan");
        terminos.add("Uy");
        List<String> terminosNormalizados = new ArrayList<>();
        for (String t : terminos){
            String out = norm.normalizarTermino(t);
            if(out == null){
                norm.agregarTermino(t);
                terminosNormalizados.add(t);
            }
            else {
                terminosNormalizados.add(out);
            }
        }

        assert (Objects.equals(terminosNormalizados.get(7), "Uva"));
        assert (Objects.equals(terminosNormalizados.getLast(), "Uy"));


    }
    @Test
    @DisplayName("Debe normalizar hechos correctamente")
    public void testNormalizadorDeHechos() throws EtiquetaNoEncontradaException {
        when(categoriaService.obtenerCategoriaPorNombre(anyString())).thenReturn(CategoriaFactory.obtenerCategoriaAleatoria());


        normalizadorDeHechos.normalizarTodos(Map.of(new FuenteEstatica(), HechoFactory.crearHechosAleatorios(5000)));

    }
}
