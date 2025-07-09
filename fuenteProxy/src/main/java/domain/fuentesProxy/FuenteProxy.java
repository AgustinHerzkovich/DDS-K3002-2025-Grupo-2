package domain.fuentesProxy;

import domain.hechos.Hecho;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// FUENTE PROXY
@Entity
@NoArgsConstructor
public abstract class FuenteProxy{
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract void pedirHechos();
    public abstract List<Hecho> importarHechos();
}