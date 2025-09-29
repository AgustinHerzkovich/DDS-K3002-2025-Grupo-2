package aplicacion.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class HoraConMasHechosDeCategoriaDTO {
    Integer horaConMasHechosDeCategoria;
    Long cantidadDeHechos;
    private String categoria;
}
