package aplicacion.dtos;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
public class HoraConMasHechosDeCategoriaDTO {
    Long horaConMasHechosDeCategoria;
    Long cantidadDeHechos;
    private String categoria;
}
