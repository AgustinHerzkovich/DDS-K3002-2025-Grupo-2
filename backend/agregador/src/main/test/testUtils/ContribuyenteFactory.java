package testUtils;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;

public class ContribuyenteFactory {
    public static Contribuyente crearContribuyenteAleatorio() {
        return new Contribuyente(false, new IdentidadContribuyente(RandomThingsGenerator.generarNombreAleatorio(), RandomThingsGenerator.generarApellidoAleatorio(), RandomThingsGenerator.generarFechaDeNacimientoAleatoria()), "correo@example.com");
    }
}
