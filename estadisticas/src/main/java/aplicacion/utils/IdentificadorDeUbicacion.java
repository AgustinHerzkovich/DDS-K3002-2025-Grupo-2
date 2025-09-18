package aplicacion.utils;


public class IdentificadorDeUbicacion {
    private static IdentificadorDeUbicacion instance;

    private IdentificadorDeUbicacion() {
    }

    public static IdentificadorDeUbicacion getInstance() {
        if(instance == null)
            instance = new IdentificadorDeUbicacion();
        return instance;
    }

    public String identificar(double latitud, double longitud) {
        return "na";
    }
}
