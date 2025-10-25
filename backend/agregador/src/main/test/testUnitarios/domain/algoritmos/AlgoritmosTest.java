package testUnitarios.domain.algoritmos;

import aplicacion.domain.algoritmos.AlgoritmoConsensoAbsoluto;
import aplicacion.domain.algoritmos.AlgoritmoConsensoIrrestricto;
import aplicacion.domain.algoritmos.AlgoritmoConsensoMayoriaSimple;
import aplicacion.domain.algoritmos.AlgoritmoConsensoMultiplesMenciones;
import aplicacion.domain.hechos.Hecho;
import org.junit.jupiter.api.*;
import testUtils.HechoFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlgoritmosTest {
    static Map<Hecho, Long> hechos = new HashMap<>();
    @BeforeAll
    public static void setup(){
        hechos.put(HechoFactory.crearHechoAleatorio(), 1L);
        hechos.put(HechoFactory.crearHechoAleatorio(), 2L);
        hechos.put(HechoFactory.crearHechoAleatorio(), 3L);
        hechos.put(HechoFactory.crearHechoAleatorio(), 4L);
        hechos.put(HechoFactory.crearHechoAleatorio(), 5L);
        hechos.put(HechoFactory.crearHechoAleatorio(), 6L);
        hechos.put(HechoFactory.crearHechoAleatorio(), 7L);
        hechos.put(HechoFactory.crearHechoAleatorio(), 8L);
        hechos.put(HechoFactory.crearHechoAleatorio(), 9L);
        hechos.put(HechoFactory.crearHechoAleatorio(), 10L);

    }
    @Test
    @DisplayName("Debe poder ejecutarse el algoritmo de consenso absoluto")
    public void algoritmoAbsolutoFunciona(){
        List<Hecho> ret = new AlgoritmoConsensoAbsoluto().curarHechos(hechos, 10L);
        Assertions.assertEquals(1, ret.size(), "Algoritmo absoluto falló");
    }
    @Test
    @DisplayName("Debe poder ejecutarse el algoritmo de consenso irrestricto")
    public void algoritmoIrrestrictoFunciona(){
        List<Hecho> ret = new AlgoritmoConsensoIrrestricto().curarHechos(hechos, 10L);
        Assertions.assertEquals(10, ret.size(), "Algoritmo irrestricto falló");
    }
    @Test
    @DisplayName("Debe poder ejecutarse el algoritmo de consenso de mayoria simple")
    public void algoritmoMayoriaSimpleFunciona(){
        List<Hecho> ret = new AlgoritmoConsensoMayoriaSimple().curarHechos(hechos, 10L);
        Assertions.assertEquals(6, ret.size(), "Algoritmo mayoria simple falló");
        Assertions.assertTrue(hechos.get(ret.getFirst())>=5, "Algoritmo mayoria simple falló");
    }
    @Test
    @DisplayName("Debe poder ejecutarse el algoritmo de consenso de multiples menciones")
    public void algoritmoMultiplesMencionesFunciona(){
        List<Hecho> ret = new AlgoritmoConsensoMultiplesMenciones().curarHechos(hechos, 10L);
        Assertions.assertEquals(9, ret.size(), "Algoritmo multiples menciones falló");
    }
}
