package aplicacion.domain.dimensiones;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeExclude;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@EqualsAndHashCode
public class DimensionColeccion {
    @HashCodeExclude
    @Id
    @Column(name = "coleccionId")
    private Long idColeccion;
    private String idColeccionAgregador;
    private String titulo;
    private String descripcion;
}
