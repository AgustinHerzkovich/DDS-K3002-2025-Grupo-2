package testUtils;

import aplicacion.domain.hechos.*;
import aplicacion.domain.usuarios.Contribuyente;

import java.time.LocalDateTime;
import java.util.*;

public class HechoFactory {

    private static final Random random = new Random();

    public static Hecho crearHechoAleatorio() {
        String titulo = RandomThingsGenerator.generarOracionAleatoria();
        String descripcion = RandomThingsGenerator.generarTextoAleatorio();
        Categoria categoria = CategoriaFactory.obtenerCategoriaAleatoria();
        Ubicacion ubicacion = RandomThingsGenerator.generarUbicacionAleatoria();
        LocalDateTime fechaAcontecimiento = LocalDateTime.now().minusDays(random.nextInt(100));
        Origen origen = RandomThingsGenerator.generarOrigenAleatorio();
        String contenidoTexto = "Contenido de texto generado aleatoriamente " + UUID.randomUUID();
        Boolean anonimato = random.nextBoolean();
        Contribuyente autor = anonimato ? null : ContribuyenteFactory.crearContribuyenteAleatorio();

        return new Hecho(
                null, // JPA autogenerará el UUID
                titulo,
                descripcion,
                categoria,
                ubicacion,
                fechaAcontecimiento,
                origen,
                contenidoTexto,
                List.of(new Multimedia(RandomThingsGenerator.generarLinkYoutubeAleatorio())),
                anonimato,
                autor
        );
    }

    public static List<Hecho> crearHechosAleatorios(int cantidad) {
        List<Hecho> hechos = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            hechos.add(crearHechoAleatorio());
        }
        return hechos;
    }
}

