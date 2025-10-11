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
@Table(name = "hecho")
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @EqualsAndHashCode.Include
    private Categoria categoria;
    @Embedded
    @EqualsAndHashCode.Include
    private Ubicacion ubicacion;
    @EqualsAndHashCode.Include
    @Column(name = "fecha_acontecimiento")
    private LocalDateTime fechaAcontecimiento;
    @Column(name = "fecha_carga")
    private LocalDateTime fechaCarga;




    public Hecho(Categoria categoria,
                 Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento,
                 LocalDateTime fechaCarga) {
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
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