package aplicacion.utils;

import de.westnordost.countryboundaries.CountryBoundaries;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class IdentificadorDeUbicacion {
    private static IdentificadorDeUbicacion instance;
    CountryBoundaries boundaries;

    private IdentificadorDeUbicacion() {
        try{
            byte[] bytes = Files.readAllBytes(new File("boundaries360x180.ser").toPath());
             boundaries = CountryBoundaries.load(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            throw new RuntimeException("No se encontro el archivo de las ubicaciones");
        }
    }

    public static IdentificadorDeUbicacion getInstance(){
        if(instance == null)
            instance = new IdentificadorDeUbicacion();
        return instance;
    }

    public String identificar(Integer latitud, Integer longitud){
        return boundaries.getIds(latitud, longitud).getFirst();
    }

}
