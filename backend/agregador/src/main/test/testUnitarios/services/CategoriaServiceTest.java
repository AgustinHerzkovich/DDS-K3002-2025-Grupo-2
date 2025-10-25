package testUnitarios.services;

import aplicacion.domain.hechos.Categoria;
import aplicacion.excepciones.CategoriaNoEncontradaException;
import aplicacion.repositorios.RepositorioDeCategorias;
import aplicacion.services.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private RepositorioDeCategorias repositorioDeCategorias;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria("Seguridad");
    }

    @Test
    @DisplayName("Debe devolver una categoría existente por nombre")
    void obtenerCategoriaPorNombreDevuelveCategoriaExistente() throws CategoriaNoEncontradaException {
        when(repositorioDeCategorias.findByNombre("Seguridad")).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.obtenerCategoriaPorNombre("Seguridad");

        assertEquals(categoria, resultado);
        verify(repositorioDeCategorias).findByNombre("Seguridad");
    }

    @Test
    @DisplayName("Debe lanzar excepción si la categoría no existe")
    void obtenerCategoriaPorNombreLanzaExcepcionSiNoExiste() {
        when(repositorioDeCategorias.findByNombre("Inexistente")).thenReturn(Optional.empty());

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.obtenerCategoriaPorNombre("Inexistente"));
    }

    @Test
    @DisplayName("Debe agregar una nueva categoría y guardarla en el repositorio")
    void agregarCategoriaGuardaCategoria() {
        when(repositorioDeCategorias.save(any(Categoria.class))).thenReturn(categoria);

        Categoria resultado = categoriaService.agregarCategoria("Seguridad");

        assertEquals("Seguridad", resultado.getNombre());
        verify(repositorioDeCategorias).save(any(Categoria.class));
    }
}
