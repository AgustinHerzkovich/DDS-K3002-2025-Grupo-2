package aplicacion.domain.hechosYSolicitudes;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// HECHO
@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo se incluyen los minimos para diferenciar a dos hechos. Fechas de cargas, solicitudes de eliminacion y otras cosas no
@NoArgsConstructor
@AllArgsConstructor
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @EqualsAndHashCode.Include
    private Categoria categoria;
    @ManyToOne
    @EqualsAndHashCode.Include
    private Ubicacion ubicacion;
    @EqualsAndHashCode.Include
    private LocalDateTime fechaAcontecimiento;


    public Hecho(Categoria categoria,
                 Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento) {
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
    }

    public String toString() {
        return "Hecho{" +
                "id='" + id + '\'' +
                ", categoria=" + categoria.getNombre() +
                ", ubicacion=(" + ubicacion.getLatitud() + ", " + ubicacion.getLongitud() + ")" +
                ", fechaAcontecimiento=" + fechaAcontecimiento +
                '}';
    }
}