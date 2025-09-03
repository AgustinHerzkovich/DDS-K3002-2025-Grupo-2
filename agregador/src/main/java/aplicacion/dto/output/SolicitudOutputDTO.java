package aplicacion.dto.output;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.solicitudes.EstadoSolicitud;
import aplicacion.domain.usuarios.Contribuyente;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@AllArgsConstructor
public class SolicitudOutputDTO {
    private String hechoId;
    private String motivo;
    private Long id;
    private EstadoSolicitud estado;
    private LocalDateTime fechaSubida;
}
