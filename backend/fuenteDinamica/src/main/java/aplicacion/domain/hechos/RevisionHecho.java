package aplicacion.domain.hechos;

import aplicacion.domain.usuarios.Contribuyente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class RevisionHecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Contribuyente administrador;
    @ManyToOne
    private Hecho hecho;
    private LocalDateTime fecha;

    public RevisionHecho(Contribuyente administrador, Hecho hecho) {
        this.administrador = administrador;
        this.hecho = hecho;
        this.fecha = LocalDateTime.now();
    }
}
    /*
    Basicamente, sirve para guardar y registrar revisiones pasadas sobre un hecho. No se aclara si es una por hecho o pueden ser varias
    En un futuro pueden persistirse o las dejamos de usar.
    */