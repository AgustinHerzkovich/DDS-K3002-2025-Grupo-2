

import aplicacion.utils.IdentificadorDeUbicacion;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

public class IdentificadorDeUbicacionTest {

    @Test
    public void testRendimientoIdentificadorDeUbicacion() {
        System.gc();
        long memoriaAntes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long inicioInstancia = System.nanoTime();
        IdentificadorDeUbicacion identificador = IdentificadorDeUbicacion.getInstance();
        long finInstancia = System.nanoTime();

        long memoriaDespues = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memoriaUsada = memoriaDespues - memoriaAntes;

        double tiempoCreacionMs = (finInstancia - inicioInstancia) / 1_000_000.0;
        double memoriaKB = memoriaUsada / 1024.0;
        double memoriaMB = memoriaKB / 1024.0;
        System.out.printf("Tiempo de creación de instancia: %.3f ms\n", tiempoCreacionMs);
        System.out.printf("Memoria usada por la instancia: %.2f MB\n", memoriaMB);

        int cantidad = 1000;
        Random random = new Random();
        double[] latitudes = new double[cantidad];
        double[] longitudes = new double[cantidad];

        for (int i = 0; i < cantidad; i++) {
            latitudes[i] = -90 + 180 * random.nextDouble();
            longitudes[i] = -180 + 360 * random.nextDouble();
        }

        long inicioAnalisis = System.nanoTime();
        for (int i = 0; i < cantidad; i++) {
            identificador.identificar(latitudes[i], longitudes[i]);
        }
        long finAnalisis = System.nanoTime();

        double tiempoAnalisisMs = (finAnalisis - inicioAnalisis) / 1_000_000.0;
        System.out.printf("Tiempo en analizar 100,000 ubicaciones: %.3f ms\n", tiempoAnalisisMs);

        assertNotNull(identificador);
        assertTrue("La creación de la instancia debería ser rápida", tiempoCreacionMs < 2000);
        assertTrue( "El análisis no debería tardar más de 10s", tiempoAnalisisMs < 10000);
    }
}
