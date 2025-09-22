package aplicacion.domain.hechosYSolicitudes.solicitudes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "solicitud_eliminacion")

public class SolicitudEliminacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera un ID autoincremental
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estado_id")
    private EstadoSolicitud estado;
    @Getter
    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;
    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    public SolicitudEliminacion() {
    }
}