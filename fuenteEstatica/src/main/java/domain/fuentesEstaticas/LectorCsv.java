package domain.fuentesEstaticas;


import com.opencsv.CSVReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.exceptions.CsvException;
import domain.hechos.Hecho;
import domain.hechos.Categoria;
import domain.hechos.Origen;


// LECTOR CSV
public class LectorCsv{
    private List<Categoria> categorias;

    public LectorCsv() {
        categorias = new ArrayList<>();
    }

    public List<Hecho> leerHechos(Integer cantidad, String path) {
        List<Hecho> hechos = new ArrayList<Hecho>();

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            List<String[]> filas = reader.readAll();

            // Saltar header
            for (int i = 1; i < filas.size() && hechos.size() < cantidad; i++) {
                String[] fila = filas.get(i);
                if (fila.length >= 6) {
                    String titulo = fila[0];
                    String descripcion = fila[1];
                    String categoria = fila[2];
                    Double latitud = Double.parseDouble(fila[3]);
                    Double longitud = Double.parseDouble(fila[4]);
                    LocalDate fecha = LocalDate.parse(fila[5], DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                    borrarHecho(titulo, hechos); // Si ya existe un hecho con ese título, lo elimina

                    Categoria categoria_hecho = buscarCategoria(categoria);

                    hechos.add(formatearHecho(titulo, descripcion, categoria_hecho, latitud, longitud, fecha));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer el archivo: " + e.getMessage()); // Se arroja una excepción en caso de no poder leerse el archivo
        }
        return hechos;
    }

    public Hecho formatearHecho(String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fecha_hecho){
        return new Hecho(titulo, descripcion, categoria, latitud, longitud, fecha_hecho, Origen.DATASET, null, null, true, null);
    }

    // TODO: cuando hagamos la base de datos usar a los repositorios que nos da springboot y sacar la lista de categorias
    // mismo con todo tipo de repositorio o lista auxiliar que vayamos a usar. Recordar meter las dependencias del jpa para evitar problemas
    public void borrarHecho(String titulo, List<Hecho> hechos) {
        hechos.removeIf(hecho -> hecho.tieneMismoTitulo(titulo));
    }

    public Categoria buscarCategoria(String nombre_categoria) {
        return categorias.stream().filter(categoria -> categoria.esIdenticaA(nombre_categoria)).findFirst().orElse(new Categoria(nombre_categoria));
    }
}