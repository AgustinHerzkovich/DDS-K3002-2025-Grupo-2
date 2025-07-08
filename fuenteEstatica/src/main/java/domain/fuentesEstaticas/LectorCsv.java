package domain.fuentesEstaticas;


import com.opencsv.CSVReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import domain.hechos.Hecho;
import domain.hechos.Categoria;
import domain.hechos.Origen;


// LECTOR CSV
public class LectorCsv{
    private final List<Categoria> categorias;

    public LectorCsv() {
        categorias = new ArrayList<>();
    }

    public List<Hecho> leerHechos(String path) {
        List<String[]> datos = extract(path);
        return load(transform(datos));
    }

    // ----------------------
    // EXTRACT
    // ----------------------
    private List<String[]> extract(String path) {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            List<String[]> filas = reader.readAll();
            return filas.subList(1, filas.size()); // Saltear header
        } catch (Exception e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ----------------------
    // TRANSFORM
    // ----------------------
    private List<HechoParcial> transform(List<String[]> filas) {
        List<HechoParcial> hechosParciales = new ArrayList<>();

        for (String[] fila : filas) {
            if (fila.length < 6) continue;

            try {
                String titulo = fila[0];
                String descripcion = fila[1];
                String nombreCategoria = fila[2];
                Double latitud = Double.parseDouble(fila[3]);
                Double longitud = Double.parseDouble(fila[4]);
                LocalDateTime fecha = LocalDateTime.parse(fila[5], DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                Categoria categoria = obtenerOCrearCategoria(nombreCategoria);

                hechosParciales.add(new HechoParcial(titulo, descripcion, categoria, latitud, longitud, fecha));
            } catch (Exception e) {
                System.out.println("Fila inválida: " + String.join(",", fila) + " - " + e.getMessage());
            }
        }

        return hechosParciales;
    }

    // ----------------------
    // LOAD
    // ----------------------
    private List<Hecho> load(List<HechoParcial> hechosParciales) {
        List<Hecho> hechos = new ArrayList<>();

        for (HechoParcial parcial : hechosParciales) {
            borrarHecho(parcial.titulo(), hechos);
            hechos.add(formatearHecho(parcial));
        }

        return hechos;
    }

    private Hecho formatearHecho(HechoParcial parcial) {
        return new Hecho(
                parcial.titulo(),
                parcial.descripcion(),
                parcial.categoria(),
                parcial.latitud(),
                parcial.longitud(),
                parcial.fecha(),
                Origen.DATASET
        );
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

    private void borrarHecho(String titulo, List<Hecho> hechos) {
        hechos.removeIf(hecho -> hecho.tieneMismoTitulo(titulo));
    }

    // Record auxiliar para transportar datos antes de crear un Hecho
    private record HechoParcial(
            String titulo,
            String descripcion,
            Categoria categoria,
            Double latitud,
            Double longitud,
            LocalDateTime fecha
    ) {}

    // TODO: cuando hagamos el agregador el chequeo de categoria se debe hacer ahi ya que ahi se almacenaran todas las categorias existentes en el sistema
}