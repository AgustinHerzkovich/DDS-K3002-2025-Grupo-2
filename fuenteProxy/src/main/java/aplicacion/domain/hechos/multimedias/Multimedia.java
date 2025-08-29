package aplicacion.domain.hechos.multimedias;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// MULTIMEDIA
@NoArgsConstructor
@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,      // usamos el nombre para diferenciar subclases
        include = JsonTypeInfo.As.PROPERTY, // el tipo estará como propiedad en el JSON
        property = "tipo"                // nombre del campo que indica el tipo concreto
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Audio.class, name = "audio"),
        @JsonSubTypes.Type(value = Imagen.class, name = "imagen"),
        @JsonSubTypes.Type(value = Video.class, name = "video")
})
public abstract class Multimedia {
    private String formato;
    private Integer tamanio;

    public Multimedia(String formato, Integer tamanio){
        this.formato = formato;
        this.tamanio = tamanio;
    }

    public abstract void reproducir();
}