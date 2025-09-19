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

    private FeatureCollection fc; // no se inicializa aún
    private final GeoJSONReader reader = new GeoJSONReader();
    private final GeometryFactory gf = new GeometryFactory();

    // Singleton
    public static IdentificadorDeUbicacion getInstance() {
        if (instance == null) {
            instance = new IdentificadorDeUbicacion();
        }
        return instance;
    }

    // Constructor
    private IdentificadorDeUbicacion() {
        String geoJsonContent = readGeoJsonFromResources("gadm41_ARG_1.json");
        GeoJSON gj = GeoJSONFactory.create(geoJsonContent);
        if (!(gj instanceof FeatureCollection)) {
            throw new IllegalArgumentException("Esperaba un FeatureCollection de provincias");
        }
        this.fc = (FeatureCollection) gj;
    }

    // Leer archivo desde resources
    private static String readGeoJsonFromResources(String fileName) {
        InputStream inputStream = IdentificadorDeUbicacion.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new RuntimeException("Archivo no encontrado en resources: " + fileName);
        }
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    // Verificar si el punto está dentro de una provincia
    public String identificar(double latitud, double longitud) {
        Point punto = gf.createPoint(new Coordinate(longitud, latitud));
        for (Feature feature : fc.getFeatures()) {
            org.wololo.geojson.Geometry ggeom = feature.getGeometry();
            Geometry geomJts = reader.read(ggeom);
            if (geomJts.contains(punto)) {
                Object nombre = feature.getProperties().get("NAME_1");
                return nombre != null ? nombre.toString() : "Provincia desconocida";
            }
        }
        return "No se encontró ninguna provincia para esa ubicación.";
    }
}
