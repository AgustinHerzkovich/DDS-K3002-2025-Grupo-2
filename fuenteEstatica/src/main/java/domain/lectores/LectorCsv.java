package domain.lectores;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import domain.hechos.Hecho;
import domain.hechos.Categoria;
import domain.hechos.Origen;

// LECTOR CSV
public class LectorCsv implements LectorArchivo {

    private final List<Categoria> categorias = new ArrayList<>();

    @Override
    public Boolean soportaExtension(String extension) {
        return extension.equalsIgnoreCase("csv");
    }

    @Override
    public List<Hecho> leerHechos(InputStream inputStream) {
        List<Hecho> hechos = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> filas = reader.readAll();
            if (filas.isEmpty()) return hechos;

            String[] headers = filas.getFirst();
            List<String[]> dataRows = filas.subList(1, filas.size());

            for (String[] fila : dataRows) {
                try {
                    Hecho hecho = crearHechoDesdeFila(headers, fila);
                    hechos.add(hecho);
                } catch (Exception e) {
                    System.out.println("Error en fila: " + String.join(",", fila) + " → " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Error al leer CSV: " + e.getMessage());
        }

        return hechos;
    }

    private Hecho crearHechoDesdeFila(String[] headers, String[] fila) {
        String titulo = "", descripcion = "";
        double latitud = 0, longitud = 0;
        LocalDateTime fecha = LocalDateTime.now();
        Categoria categoria = new Categoria("Default");

        for (int i = 0; i < headers.length && i < fila.length; i++) {
            String key = normalizar(headers[i]);
            String value = fila[i].trim();

            try {
                switch (key) {
                    case "titulo" -> titulo = value;
                    case "descripcion" -> descripcion = value;
                    case "latitud" -> latitud = Double.parseDouble(value);
                    case "longitud" -> longitud = Double.parseDouble(value);
                    case "categoria" -> categoria = obtenerOCrearCategoria(value);
                    case "fechadelhecho" -> fecha = LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                }
            } catch (Exception e) {
                System.out.println("Error en columna: " + key + " = " + value + " → " + e.getMessage());
            }
        }

        if (titulo.isBlank() && descripcion.isBlank()) {
            throw new IllegalArgumentException("Hecho sin título ni descripción");
        }

        return new Hecho(titulo, descripcion, categoria, latitud, longitud, fecha, Origen.DATASET);
    }

    private Categoria obtenerOCrearCategoria(String nombre) {
        return categorias.stream()
                .filter(cat -> cat.esIdenticaA(nombre))
                .findFirst()
                .orElseGet(() -> {
                    Categoria nueva = new Categoria(nombre);
                    categorias.add(nueva);
                    return nueva;
                });
    }

    private String normalizar(String texto) {
        return texto.trim().toLowerCase()
                .replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o").replace("ú", "u")
                .replaceAll("[^a-z0-9]", "");
    }
}