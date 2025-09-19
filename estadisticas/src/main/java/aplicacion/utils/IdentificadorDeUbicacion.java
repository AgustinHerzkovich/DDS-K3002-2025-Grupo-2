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
import java.util.Scanner;

public class IdentificadorDeUbicacion {

    private static IdentificadorDeUbicacion instance;

    private FeatureCollection fc;
    // El "Mapa" con las provincias

    private final GeoJSONReader reader = new GeoJSONReader();
    //

    private final GeometryFactory gf = new GeometryFactory();

    public static IdentificadorDeUbicacion getInstance() {
        if (instance == null) {
            instance = new IdentificadorDeUbicacion();
        }
        return instance;
    }

    private IdentificadorDeUbicacion() {
        String geoJsonContent = readGeoJsonFromResources("gadm41_ARG_1.json");
        // Lee el geoJson
        GeoJSON gj = GeoJSONFactory.create(geoJsonContent);
        // Crea un geoJson
        if (!(gj instanceof FeatureCollection)) {
            throw new IllegalArgumentException("Esperaba un FeatureCollection de provincias");
        }
        //Verifica que sea un "FeatureCollection"
        this.fc = (FeatureCollection) gj;
        //Guarda la FeatureCollection
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

    public String identificar(double latitud, double longitud) {
        Point punto = gf.createPoint(new Coordinate(longitud, latitud));
        // Crea un punto en la ubicacion para ver si esta en una provincia
        for (Feature feature : fc.getFeatures()) {
            // Por cada provincia (feature) del mapa
            org.wololo.geojson.Geometry ggeom = feature.getGeometry();
            // Obtiene la geometria de la feature
            Geometry geomJts = reader.read(ggeom);
            // Convierte la provincia en un objeto de JTS para poder usar metodos de analisis de la geometria
            if (geomJts.contains(punto)) {
                // Si el punto esta en la geometria
                Object nombre = feature.getProperties().get("NAME_1");
                // Obtengo el nombre
                return nombre != null ? nombre.toString() : "Provincia desconocida";
                // Si no lo encuentro, devuelvo nombre desconocido
            }
        }
        return "No se encontró ninguna provincia para esa ubicación.";
        // Si llegue aca, es que no esta en el mapa
    }
}
