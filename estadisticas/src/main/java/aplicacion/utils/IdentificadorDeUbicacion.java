package aplicacion.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSON;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;




/*
*
* https://gadm.org/data.html
*
* Archivos geoJson de varios paises
*
* https://mapshaper.org/
*
* Sitio para reducir tamaño de los mapas
*
* Actualmente, con solo las provincias argentinas guardadas a la resolución por defecto, puede identificar 1.000.000 de provincias en menos de 200 ms
* No deberia dar problemas de rendimiento, siempre y cuando no le demos demasiados datasets de provincias
*/





public class IdentificadorDeUbicacion {

    private static IdentificadorDeUbicacion instance;
    private List<Provincia> provinciasCache = new ArrayList<>();
    // Lista de provincias en memoria
    private final GeometryFactory gf = new GeometryFactory();
    // Factory necesario para crear puntos y evaluarlos

    public static IdentificadorDeUbicacion getInstance() {
        if (instance == null) {
            instance = new IdentificadorDeUbicacion();
        }
        return instance;
    }

    private IdentificadorDeUbicacion() {
        String geoJsonContent = readGeoJsonFromResources("provincias.geojson");
        // Lee el geoJson
        GeoJSON gj = GeoJSONFactory.create(geoJsonContent);
        // Crea un geoJson
        if (!(gj instanceof FeatureCollection)) {
            throw new IllegalArgumentException("Esperaba un FeatureCollection de provincias");
        }
        //Verifica que sea un "FeatureCollection"
        FeatureCollection fc = (FeatureCollection) gj;
        GeoJSONReader reader = new GeoJSONReader();
        int idx = 0;
        for (Feature feature : fc.getFeatures()) {
            // Por cada "feature" (provincia)
            Geometry geomJts;

            org.wololo.geojson.Geometry ggeom = feature.getGeometry();
            if (ggeom == null) {
                System.err.println("Feature index " + idx + " tiene geometría nula. Se ignora.");
                idx++;
                continue;
            }
            geomJts = reader.read(ggeom);
            // La transforma a geometria de JTS, para poder usar contains()
            String provincia = (String) feature.getProperties().get("NAME_1");
            String pais = (String) feature.getProperties().get("COUNTRY");
            String iso = (String) feature.getProperties().get("ISO_1");
            // Busca el nombre de la provincia
            provinciasCache.add(new Provincia(geomJts, provincia, pais, iso));
            // Guarda la provincia, sus nombres y geometria convertida a memoria
        }


    }

    private static String readGeoJsonFromResources(String fileName) {
        InputStream inputStream = IdentificadorDeUbicacion.class.getClassLoader().getResourceAsStream(fileName);
        //Saca el geoJson de resources
        if (inputStream == null) {
            throw new RuntimeException("Archivo no encontrado en resources: " + fileName);
        }
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").next();
        }
        // Convierte el geoJson a un string
    }

    public Provincia identificar(double latitud, double longitud) {
        Point punto = gf.createPoint(new Coordinate(longitud, latitud));
        // Crea un punto en la ubicacion para ver si esta en una provincia
        for (Provincia provincia : provinciasCache) {
            if (provincia.getGeom().contains(punto)) {
                return provincia;
                // Si esta devuelve la provincia
            }
        }
        return null;
        // Si llegue aca, es que no esta en el mapa
    }
}
