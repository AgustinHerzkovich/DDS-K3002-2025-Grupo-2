package aplicacion.domain;

import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// FUENTE PROXY
@Getter
@Entity
@NoArgsConstructor
public abstract class FuenteProxy{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract void pedirHechos();
    public abstract List<Hecho> importarHechos();
}