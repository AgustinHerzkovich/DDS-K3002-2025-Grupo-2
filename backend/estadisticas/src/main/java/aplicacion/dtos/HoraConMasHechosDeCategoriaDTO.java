package aplicacion.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({ "categoria", "horaConMasHechosDeCategoria", "cantidadDeHechos" })
public class HoraConMasHechosDeCategoriaDTO {
    Integer horaConMasHechosDeCategoria;
    Long cantidadDeHechos;
    private String categoria;
}
