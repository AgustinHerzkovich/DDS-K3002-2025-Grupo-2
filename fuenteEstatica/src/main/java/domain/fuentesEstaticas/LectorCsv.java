package domain.fuentesEstaticas;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import domain.hechos.Hecho;
import domain.hechos.Categoria;
import domain.hechos.Origen;


// LECTOR CSV
public class LectorCsv{
    private List<Categoria> categorias;
    public List<Hecho> leerHechos(Integer cantidad, String path) {
        List<Hecho> hechos = new ArrayList<Hecho>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            int contador = 0;

            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",");

                for (int i = 0; i <= campos.length - 6; i += 6) { // suponiendo que los hechos vienen por completo en los csv y no faltan
                    //nignunas de estas categorias
                    String titulo = campos[i];
                    String descripcion = campos[i + 1];
                    String categoria = campos[i + 2];
                    String latitud = campos[i + 3];
                    String longitud = campos[i + 4];
                    String fecha = campos[i + 5];
                    // temporalmente creamos una nueva categoria por cada una, pero en un futuro tenemos que validar que no exista ya
                    // TODO: corregir esto de la categoria
                    hechos.add(formatearHecho(titulo, descripcion, new Categoria(categoria), Double.parseDouble(latitud), Double.parseDouble(longitud), LocalDateTime.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage()); // Se arroja una excepción en caso de no poder leerse el archivo
        }
        return hechos;
    }

    public Hecho formatearHecho(String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDateTime fecha_hecho){
        return new Hecho(titulo, descripcion, categoria, latitud, longitud, fecha_hecho, Origen.DATASET, null, null, true, null);
    }
}
