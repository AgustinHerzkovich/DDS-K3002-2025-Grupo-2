package domain.fuentesProxy.fuentesDemo;

import domain.hechos.Categoria;
import domain.hechos.Hecho;
import domain.hechos.Origen;
import domain.hechos.multimedias.Multimedia;
import domain.usuarios.IdentidadContribuyente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class HechoBuilder {
    public Hecho construirHecho(Map<String, Object> datos) {
        if(datos.isEmpty()){return null;}
        String titulo = (String) datos.get("titulo");
        String descripcion = (String) datos.get("descripcion");
        Categoria categoria = (Categoria) datos.get("categoria"); // o convertilo si viene como otra cosa
        Double latitud = (Double) datos.get("latitud");
        Double longitud = (Double) datos.get("longitud");
        LocalDateTime fechaAcontecimiento = (LocalDate) datos.get("fecha_acontecimiento");
        Origen origen = (Origen) datos.get("origen");
        String contenidoTexto = (String) datos.get("contenido_texto");
        List<Multimedia> contenidoMultimedia = (List<Multimedia>) datos.get("contenido_multimedia");
        Boolean anonimato = (Boolean) datos.get("anonimato");
        IdentidadContribuyente autor = (IdentidadContribuyente) datos.get("autor");

        return new Hecho(
                titulo,
                descripcion,
                categoria,
                latitud,
                longitud,
                fechaAcontecimiento,
                origen,
                contenidoTexto,
                contenidoMultimedia,
                anonimato,
                autor
        );
    }
}
